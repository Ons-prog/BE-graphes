package org.insa.graphs.gui.simple;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;


import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.algorithm.shortestpath.AStarAlgorithm;
import org.insa.graphs.algorithm.shortestpath.BellmanFordAlgorithm;
import org.insa.graphs.algorithm.shortestpath.DijkstraAlgorithm;
import org.insa.graphs.algorithm.shortestpath.ShortestPathData;
import org.insa.graphs.algorithm.shortestpath.ShortestPathSolution;
import org.insa.graphs.gui.drawing.Drawing;
import org.insa.graphs.gui.drawing.components.BasicDrawing;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;

public class Launch {

    /**
     * Create a new Drawing inside a JFrame an return it.
     *
     * @return The created drawing.
     * @throws Exception if something wrong happens when creating the graph.
     */
    public static Drawing createDrawing() throws Exception {
        BasicDrawing basicDrawing = new BasicDrawing();
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("BE Graphes - Launch");
                frame.setLayout(new BorderLayout());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
                frame.setSize(new Dimension(800, 600));
                frame.setContentPane(basicDrawing);
                frame.validate();
            }
        });
        return basicDrawing;
    }

    /*-------------------------------Définition du scénario -------------------------*/
    static class TestScenario {
        final String mapFile;    // graphe
        final String pathFile;   // path
        final int filterIndex; // index pour la narure du cout donc distance ou temps
        final int origin, dest;  

        TestScenario(String mapFile, String pathFile, int filterIndex, int origin, int dest) {
            this.mapFile  = mapFile;
            this.pathFile = pathFile;
            this.filterIndex=filterIndex;
            this.origin   = origin;
            this.dest     = dest;
        }

        @Override public String toString() {
            return String.format("%s [%s] %d -> %d",
                mapFile, (filterIndex==0? "DISTANCE" : "TIME"),origin, dest);
        }
    }

    /*-------------------------------Liste des scenarios-------------------------*/



    public static void main(String[] args) throws Exception {


    /*----------------------------Les Différents Scénarios--------------------------- */
        TestScenario[] scenarios = new TestScenario[] {
            new TestScenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr", 
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path", 
                0, 552, 526), //On teste le chemin entre R2 et rangueil sans filtre (tous les moyens sont possibles)
            new TestScenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr",
                null, 1, 14531, 5910), //On teste le chemin entre Insa et Bikini en termes de distance en voiture (ne doit pas passer par le canal)
            new TestScenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/toulouse.mapgr",
                null, 3, 14531, 5910), //On teste le chemin entre Insa et Bikini en termes de temps (pedesterian)
            new TestScenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/paris.mapgr", 
                null, 0, 8626, 8626), //On teste un chemin de longueur nulle
            new TestScenario("/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Maps/insa.mapgr", 
                "/mnt/commetud/3eme Annee MIC/Graphes-et-Algorithmes/Paths/path_fr31insa_rangueil_r2.path", 
                1, 552, 526), //On teste le chemin entre R2 et rangueil pour les voitures (Chemin inexistant)
            };

        

    /*--------------------------- Pour chaque scénario--------------------------
    on lit la carte, on lance les 3 algos, on lit le .path de référence, et on compare coûts*/

        for (TestScenario s : scenarios) {
            System.out.println("Scenario : " + s );
        
            Graph graph;
            Path expectedPath=null;

            // create a graph reader
            try (final GraphReader reader = new BinaryGraphReader(new DataInputStream(
                    new BufferedInputStream(new FileInputStream(s.mapFile))))) {

                graph = reader.read();
            }

            ShortestPathData data = new ShortestPathData(
                    graph,
                    graph.get(s.origin),
                    graph.get(s.dest),
                    ArcInspectorFactory.getAllFilters().get(s.filterIndex)
            );

             //On lance Dijkstra
            DijkstraAlgorithm dijkstraAlgorithm  = new DijkstraAlgorithm(data);
            ShortestPathSolution solutionDijkstra = dijkstraAlgorithm.run();
            Path dijikstraPath=solutionDijkstra.getPath();

            //On lance BellmanFord 
            BellmanFordAlgorithm bellmanFordAlgorithm = new BellmanFordAlgorithm(data);
            ShortestPathSolution solutionBellmanFord = bellmanFordAlgorithm.run();
            Path bellmanPath=solutionBellmanFord.getPath();

            //on lance A*
            AStarAlgorithm aStarAlgorithm = new AStarAlgorithm(data);
            ShortestPathSolution solutionAstar = aStarAlgorithm.run();
            Path aStarPath=solutionAstar.getPath();

            /*----------------Tests pour comparer les chemins données par algos et le expected path dans la classe Path -------------------------------- */

            double coutDijikstra=0,coutBellman=0,coutAStar=0;

            if (dijikstraPath!=null){
                coutDijikstra = (s.filterIndex == 0||s.filterIndex == 1) ? dijikstraPath.getLength(): dijikstraPath.getMinimumTravelTime();
                System.out.println("cout du chemin avec Dijkstra : " + coutDijikstra +"\n" );
            }
            if (bellmanPath!=null){
                coutBellman = (s.filterIndex == 0||s.filterIndex == 1) ? bellmanPath.getLength(): bellmanPath.getMinimumTravelTime();
                System.out.println("cout du chemin avec bellman : " + coutBellman +"\n" );
            }
            if (aStarPath!=null){
                coutAStar = (s.filterIndex == 0||s.filterIndex == 1) ? aStarPath.getLength(): aStarPath.getMinimumTravelTime();
                System.out.println("cout du chemin avec A*: " + coutAStar +"\n" );
            }

            if(s.pathFile!=null){
                // create a path reader
                try (PathReader pathReader = new BinaryPathReader
                    (new DataInputStream
                        (new BufferedInputStream
                            (new FileInputStream(s.pathFile) )))) {
            
                    // read the path
                    expectedPath = pathReader.readPath(graph);
                }

                // On Compare que le cout du chemin des algos est le meme que celui calculé par la classe Path
                double seuil  = 1e-2;
                double expectedCost = (s.filterIndex == 0||s.filterIndex == 1) ? expectedPath.getLength(): expectedPath.getMinimumTravelTime();

                //On compare les couts avec le cout de expected path 
                if(dijikstraPath!=null){
                    if (Math.abs(coutDijikstra - expectedCost) > seuil) 
                    throw new RuntimeException("Dijkstra coût incorrect ");
                }else{
                    System.out.println("Dijkstra: Chemin inexistant");
                }
                if(bellmanPath!=null){
                    if (Math.abs(coutBellman - expectedCost) > seuil) 
                    throw new RuntimeException("Bellman coût incorrect ");
                }else{
                    System.out.println("Bellman: Chemin inexistant");
                }
                if(aStarPath!=null){
                    if (Math.abs(coutAStar - expectedCost) > seuil) 
                    throw new RuntimeException("AStar coût incorrect ");
                }else{
                    System.out.println("A*: Chemin inexistant");
                }
            }
            
            // create the drawing for a specific scenario
            //change the index of the array to draw a specefic one
            // Le scenario (2) permet d'illustrer la différence entre le chemin obtenu avec Dijkstra et A*,le cout de A* est inférieur à celui de Disjkstra 
            
            
                Drawing drawing = createDrawing();
                drawing.drawGraph(graph);
                if (dijikstraPath!=null) drawing.drawPath(dijikstraPath);
                if (bellmanPath!=null) drawing.drawPath(bellmanPath);
                if (aStarPath!=null) drawing.drawPath(aStarPath);

                if (s.pathFile!=null)
                        drawing.drawPath(expectedPath);
            
                

        }
    }
}

