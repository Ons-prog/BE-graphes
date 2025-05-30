package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {

    //attributs
    private Node sommetCourant;

    private boolean marque;
    
    private double coutRealise;

    private Arc arcEntrantPlusCourtChemin;
    

    public Label(Node sommetCourant,boolean marque, float coutRealise, Arc arcEntrantPlusCourtChemin){
        this.sommetCourant=sommetCourant;
        this.marque=marque;
        this.coutRealise=coutRealise;
        this.arcEntrantPlusCourtChemin=arcEntrantPlusCourtChemin;
    }

    public Label(Node sommetCourant,Double coutRealise){
        this.sommetCourant=sommetCourant;
        this.marque=false;
        this.coutRealise=coutRealise;
        this.arcEntrantPlusCourtChemin=null;
    }

    public void setArcEntrantPlusCourtChemin(Arc arcEntrantPlusCourtChemin) {
        this.arcEntrantPlusCourtChemin = arcEntrantPlusCourtChemin;
    }


    public void setCoutRealise(Double coutRealise) {
        this.coutRealise = coutRealise;
    }


    public void setMarque(boolean marque) {
        this.marque = marque;
    }

    public void setSommetCourant(Node sommetCourant) {
        this.sommetCourant = sommetCourant;
    }

    

    public Arc getArcEntrantPlusCourtChemin() {
        return arcEntrantPlusCourtChemin;
    }

    
    public Node getSommetCourant() {
        return sommetCourant;
    }

    public double getCoutRealise() {
        return coutRealise;
    }

    public boolean isMarque() {
        return marque;
    }

    public double getTotalCost() {
        double cout;
        cout = this.coutRealise;
        return cout;
    }

    public double getCoutEstime() {
        return 0.0;  // pas d'estimation par défaut, à redéfinir dans LabelStar
    }

    

    public int compareTo(Label autre) {
        double thisTotal = this.getTotalCost();
        double otherTotal = autre.getTotalCost();

        if (thisTotal < otherTotal) {
            return -1;
        } else if (thisTotal > otherTotal) {
            return 1;
        } else {
            // Si totalCost égal, on compare coutEstime
            double thisEstime = this.getCoutEstime();
            double otherEstime = autre.getCoutEstime();

            if (thisEstime < otherEstime) {
                return -1;
            } else if (thisEstime > otherEstime) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
