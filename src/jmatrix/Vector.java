/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

import java.util.ArrayList;

/**
 *
 * @author jmb2
 */
public class Vector {

    private final ArrayList<Integer> ranks;
    private final ArrayList<Double> values;
    
    
    public Vector() {
        
        this.ranks=new ArrayList<>();
        this.values=new ArrayList<>();
        
    }

    public void init() {
        
        this.ranks.clear();
        this.values.clear();
        
    }
    
    
    public double getVal(int numRow) {
    
        int rank = this.ranks.indexOf(numRow);

        if (rank < 0) {
            return 0.;
        } else {
            return this.values.get(rank);
        }
    
        
        
    }

    public void setVal(int numRow, double value) {
    
        int rank = this.ranks.indexOf(numRow);

        if (rank < 0) {
            this.ranks.add(numRow);
            rank = this.ranks.indexOf(numRow);
            
            this.values.add(rank,value);
            
        } else {
            this.values.set(rank,value);
        }
    
    }

    public int getValNumber() {
        return this.values.size();
    }
    
    public int getRankValue(int rank) {
        return this.ranks.get(rank);
    }
    
    public double getValueAtRank(int rank) {
        return this.values.get(rank);
    }
    
    public void sort() {
        
        int nbElem=this.ranks.size();
        
        for (int i = 1; i < nbElem; i++) {
            
            int rank=this.ranks.get(i);
            
            int targetRank=i;
            
            while ((targetRank>0)&&(this.ranks.get(targetRank-1)>rank)) {
                targetRank--;
            }
            
            if (targetRank<i) {
                
                double value=this.values.get(i);
                
                this.ranks.remove(i);
                this.values.remove(i);
                
                this.ranks.add(targetRank, rank);
                this.values.add(targetRank, value);
                
            }
            
        }
        
        
        
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb=new StringBuilder();
        
        int nbElem=this.ranks.size();
        
        for (int i = 0; i < nbElem; i++) {
            
            sb.append(this.ranks.get(i)).append(": ").append(this.values.get(i)).append("\n");
            
        }
        
        return sb.toString();
    }
    
    public int getIndexOf(int rank) {

        for (Integer current : ranks) {
            if (current==rank) {
                return this.ranks.indexOf(current);
            }
        }
        
        return -1;
    }
    
    
    
    public static void main(String[] args) {
        
        Vector vect=new Vector();
        
        vect.setVal(10, 10);
        vect.setVal(5, 5);
        vect.setVal(7, 7);
        vect.setVal(2, 2);
        vect.setVal(15, 15);

        System.out.println("vecteur:");
        System.out.println(vect.toString());
        
        for (int i = 1; i < 20; i++) {
            System.out.println("rang "+i+": "+vect.getIndexOf(i));
        }
        
        
    }
    
    
}
