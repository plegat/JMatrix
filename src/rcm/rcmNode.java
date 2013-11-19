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
    ArrayList<Integer> adjacents;

    public rcmNode(int id) {
        this.id = id;
        this.adjacents=new ArrayList<>();
    }
    
    public void addAdjacent(int adj) {
        
        if (this.adjacents.indexOf(adj)==-1) {
            this.adjacents.add(adj);
        }
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

    
    
    public static void main(String[] args) {
        
        rcmNode node1=new rcmNode(1);
        
        rcmNode node2=new rcmNode(1);
        node2.addAdjacent(2);
        node2.addAdjacent(3);
        node2.addAdjacent(4);
        
        System.out.println(node1.compareTo(node2));
        
        
        
    }
    
    
    
    
}
