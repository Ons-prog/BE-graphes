package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.*;
import org.insa.graphs.model.*;
import java.util.ArrayList;
import java.util.Collections;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {
    // tableau qui associe chaque noeud a un label
    private Label [] tabLabels;
    

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
        this.tabLabels= new Label[data.getGraph().size()];
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
            tabLabels[node.getId()]= new Label(node,Double.POSITIVE_INFINITY);
        }

        // Label de la source
        int idSource = data.getOrigin().getId();
        tabLabels[idSource].setCoutRealise(0.0);
        tas.insert(tabLabels[idSource]);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        //Itérations
        while (!tas.isEmpty()){

            Label minLabel=tas.deleteMin();
            minLabel.setMarque(true);
            //Notify the observer that a node has been marked
            notifyNodeMarked(minLabel.getSommetCourant());

            for (Arc successeur : minLabel.getSommetCourant().getSuccessors()){
                Label y=tabLabels[successeur.getDestination().getId()];
                if (!y.isMarque()&& data.isAllowed(successeur)) {
                    if (y.getCoutRealise()>minLabel.getCoutRealise()+data.getCost(successeur)) {
                        if (Double.isFinite(y.getCoutRealise())) {
                            tas.remove(y);
                        } else {
                            //notify all observers that a node has been reached for the first time
                            notifyNodeReached(minLabel.getSommetCourant());
                        }
                        y.setCoutRealise(minLabel.getCoutRealise()+data.getCost(successeur));
                        //si c'est le PPC en temps, ca sera "plus long en distance " que le PCC en distance mais plus rapide
                        y.setArcEntrantPlusCourtChemin(successeur);
                        tas.insert(y); 
                        
                    }
                }
            }
        }

        //1)On recupere la destination et on verifie s'il existe un PCC entre origine et destination
        Label destLabel = tabLabels[data.getDestination().getId()];
        if (Double.isInfinite(destLabel.getCoutRealise())) {
            return new ShortestPathSolution(data, Status.INFEASIBLE);
        }

        // The destination has been found, notify the observers.
        notifyDestinationReached(data.getDestination());

        //Si la solution existe on remonte les arcs
        Label labelCourant= destLabel;
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
