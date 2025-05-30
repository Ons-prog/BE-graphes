package org.insa.graphs.algorithm.shortestpath;
import org.insa.graphs.model.*;

public class LabelStar extends Label {

    private double coutEstime;
   

    public LabelStar(Node sommetCourant,boolean marque, float coutRealise, Arc arcEntrantPlusCourtChemin,float coutEstime){
        super(sommetCourant,marque,coutRealise,arcEntrantPlusCourtChemin);
        this.coutEstime=coutEstime;
    }

    public LabelStar(Node sommetcourant, double coutRealise,double coutEstime){
        super(sommetcourant,coutRealise);
        this.coutEstime=coutEstime;
    }

     public void setCoutEstime(double coutEstime) {
        this.coutEstime = coutEstime;
    }
    
    @Override
    public double getCoutEstime() {
        return coutEstime;
    }
    

    @Override
    public double getTotalCost(){
        double totalcost;
        totalcost=this.coutEstime+super.getCoutRealise();
        return totalcost;
    }

    
   

   
}
