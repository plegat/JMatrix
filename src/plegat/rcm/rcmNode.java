/*
 * Cette œuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.rcm;

//Reverse Cuthill McKee ordering

import java.util.ArrayList;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class rcmNode implements Comparable<rcmNode> {
    
    String id;
    ArrayList<rcmNode> adjacents;

    public rcmNode(String id) {
        this.id = id;
        this.adjacents=new ArrayList<>();
    }
    
    public String getId() {
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
