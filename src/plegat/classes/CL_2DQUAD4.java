/*
 * Cette oeuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.classes;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import plegat.solver.Element;
import plegat.solver.Mesh;
import plegat.solver.Node;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_2DQUAD4 {
    
    public String read(BufferedReader br, Mesh mesh) {
        
        System.out.println("test CL_2DQUAD4 ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte=texte.trim();
                
                if (texte.startsWith("*")) {
                    
                    System.out.println("code retour envoi fonction CL_2DQUAD4: "+texte);
                    
                    return texte;
                } else {
                    
                    System.out.println("ligne: "+texte);
                    
                    // code specifique CL_2DQUAD4
                    
                    String[] data=texte.split(",");
                    
                    Node[] nodes=new Node[4];
                    nodes[0]=mesh.getNodeByID(Integer.parseInt(data[1]));
                    nodes[1]=mesh.getNodeByID(Integer.parseInt(data[2]));
                    nodes[2]=mesh.getNodeByID(Integer.parseInt(data[3]));
                    nodes[3]=mesh.getNodeByID(Integer.parseInt(data[4]));
                    
                    Element elm=new Element(Integer.parseInt(data[0]), nodes, Element.QUAD4);
                    
                    mesh.addElement(elm);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CL_2DNODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;

    }

}
