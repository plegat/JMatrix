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
public class NodalBCL {
    
    private int type;
    public static int NODAL_DISP=1;
    public static int NODAL_FORCE=2;
    private ArrayList<Node> zone;

    public NodalBCL(int type) {
        this.type = type;
    }

    public NodalBCL(int type, ArrayList zone) {
        this.type = type;
        this.zone = zone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<Node> getZone() {
        return zone;
    }

    public void setZone(ArrayList<Node> zone) {
        this.zone = zone;
    }
    
    
    public void addNode(Node node) {
        if (this.zone.indexOf(node)<0) {
            this.zone.add(node);
        }
    }
    
    
    
}
