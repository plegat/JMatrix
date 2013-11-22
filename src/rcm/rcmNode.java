/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rcm;

//Reverse Cuthill McKee ordering

import java.util.ArrayList;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class rcmNode implements Comparable<rcmNode> {
    
    int id;
    ArrayList<rcmNode> adjacents;

    public rcmNode(int id) {
        this.id = id;
        this.adjacents=new ArrayList<>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public void addAdjacent(rcmNode adj) {
        
        if (this.adjacents.indexOf(adj)==-1) {
            this.adjacents.add(adj);
        }
    }

    public rcmNode[] getAdjacents() {
        return this.adjacents.toArray(new rcmNode[this.adjacents.size()]);
    }
    
    public int getDegree() {
        return this.adjacents.size();
    }
    
    @Override
    public int compareTo(rcmNode o) {
        
        if (this.getDegree()<o.getDegree()) {
            return -1;
        } else if (this.getDegree()>o.getDegree()) {
            return 1;
        } else {
            return 0;
        } 
    }

    
    
    
    
    
}
