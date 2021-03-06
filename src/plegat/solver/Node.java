/*
 * Cette oeuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.solver;

import java.util.ArrayList;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Node {
    
    private double x,y;
    private String id;
    ArrayList<Node> adjacents;
    private int rcmRank;


    public Node(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.adjacents=new ArrayList<>();
        
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public void addAdjacent(Node adj) {
        
        if (this.adjacents.indexOf(adj)==-1) {
            this.adjacents.add(adj);
        }
    }

    public Node[] getAdjacents() {
        return this.adjacents.toArray(new Node[this.adjacents.size()]);
    }

    public int getRcmRank() {
        return rcmRank;
    }

    public void setRcmRank(int rcmRank) {
        this.rcmRank = rcmRank;
    }
    
    public double getDistanceTo(Node nd2) {
        
        return Math.sqrt(Math.pow(nd2.x-this.x,2)+Math.pow(nd2.y-this.y,2));
        
    }
    
    
}
