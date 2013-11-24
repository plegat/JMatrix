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
public class ElementGroup {
    
    private ArrayList<Element> elements;
    private Property prop;
    private String id;

    public ElementGroup(String id) {
        this.id = id;
    }

    public ElementGroup(String id, ArrayList<Element> elements) {
        this.id = id;
        this.elements = elements;
        
    }

    public ElementGroup(String id, ArrayList<Element> elements, Property prop) {
        this.id = id;
        this.elements = elements;
        this.prop = prop;
        
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void setElements(ArrayList<Element> elements) {
        this.elements = elements;
    }

    public Property getProp() {
        return prop;
    }

    public void setProp(Property prop) {
        this.prop = prop;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    
    
    
}
