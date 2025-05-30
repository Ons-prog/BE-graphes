package org.insa.graphs.algorithm.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.*;

public class AStarAlgorithm extends DijkstraAlgorithm {

    // tableau qui associe chaque noeud a un label
    private LabelStar [] tabLabels;

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
        this.tabLabels= new LabelStar[data.getGraph().size()];
    }



    @Override
    protected ShortestPathSolution doRun() {

        BinaryHeap<Label> tas = new BinaryHeap<Label>();
        ArrayList<Arc> solutionArcs=new ArrayList<Arc>();
        

        // retrieve data from the input problem (getInputData() is inherited from the
        // parent class ShortestPathAlgorithm)
        final ShortestPathData data = getInputData();

        // variable that will contain the solution of the shortest path problem
        ShortestPathSolution solution = null;

        //Initialisation

        for (Node node : data.getGraph().getNodes()){
            float coutEstime = (float) data.getDestination().getPoint().distanceTo(node.getPoint());  // Heuristique 
            tabLabels[node.getId()]= new LabelStar(node,Float.POSITIVE_INFINITY,coutEstime);
        }

        // Label de la source
        int idSource = data.getOrigin().getId();
        tabLabels[idSource].setCoutRealise(0.0);
        tas.insert(tabLabels[idSource]);


        //Itérations
        while (!tas.isEmpty()){
            LabelStar minLabel=(LabelStar)tas.deleteMin(); // demander pourquoi
            minLabel.setMarque(true);
            for (Arc successeur : minLabel.getSommetCourant().getSuccessors()){
                LabelStar y=tabLabels[successeur.getDestination().getId()];
                if (!y.isMarque()) {
                    double nouveauCout =minLabel.getCoutRealise() + data.getCost(successeur);
                    if (y.getCoutRealise()>nouveauCout) {
                        if (Double.isFinite(y.getCoutRealise())) {
                            tas.remove(y);
                        }
                        y.setCoutRealise(nouveauCout);
                        y.setArcEntrantPlusCourtChemin(successeur);
                        tas.insert(y); 
                    }
                }
            }
        }

        //1)On recupere la destination et on verifie s'il existe un PCC entre origine et destination
        LabelStar destLabel = tabLabels[data.getDestination().getId()];
        if (Double.isInfinite(destLabel.getCoutRealise())) {
            return new ShortestPathSolution(data, Status.INFEASIBLE);
        }

        //Si la solution existe on remonte les arcs
        LabelStar labelCourant= destLabel;
        while (labelCourant.getArcEntrantPlusCourtChemin()!=null) {
            Arc arcCourant=labelCourant.getArcEntrantPlusCourtChemin();
            solutionArcs.add(arcCourant);
            labelCourant=tabLabels[arcCourant.getOrigin().getId()];
        }


        //On Inverse la liste
        Collections.reverse(solutionArcs);
        solution = new ShortestPathSolution(data, Status.OPTIMAL,new Path(data.getGraph(),solutionArcs));

        // when the algorithm terminates, return the solution that has been found
        return solution;
    }

}
