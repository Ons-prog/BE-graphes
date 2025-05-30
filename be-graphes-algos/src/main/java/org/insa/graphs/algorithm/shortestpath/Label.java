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
    

    public int compareTo(Label autre){
        int retour;
        if (this.getTotalCost()>autre.getTotalCost())
            retour = 1;
        else if (this.getTotalCost()==autre.getTotalCost())
            retour = 0;
        else 
            retour =-1;
        return retour;
    }
}
