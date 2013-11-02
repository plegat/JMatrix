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
    
}
