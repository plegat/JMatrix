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
public class Loadcase {
    
    private String id;
    private ArrayList<NodalBCL> bcl;

    public Loadcase(String id) {
        this.id = id;
        this.bcl=new ArrayList<>();
    }

    public Loadcase(String id, ArrayList<NodalBCL> bcl) {
        this.id = id;
        this.bcl = bcl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<NodalBCL> getBcl() {
        return bcl;
    }

    public void setBcl(ArrayList<NodalBCL> bcl) {
        this.bcl = bcl;
    }
    
    public void addBCL(NodalBCL newBcl) {
        
        if (!this.bcl.contains(newBcl)) {
            this.bcl.add(newBcl);
        }
        
    }
    
}
