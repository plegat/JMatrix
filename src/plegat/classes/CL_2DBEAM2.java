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
public class CL_2DBEAM2 {
    
    public String read(BufferedReader br, Mesh mesh) {
        
        System.out.println("test 2DBEAM2 ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte=texte.trim();
                
                if (texte.startsWith("*")) {
                    
                    System.out.println("code retour envoi fonction 2DBEAM2: "+texte);
                    
                    return texte;
                } else {
                    
                    System.out.println("ligne: "+texte);
                    
                    // code specifique 2DBAR
                    
                    String[] data=texte.split(",");
                    
                    Node[] nodes=new Node[2];
                    nodes[0]=mesh.getNodeByID(data[1].trim());
                    nodes[1]=mesh.getNodeByID(data[2].trim());
                    
                    Element elm=new Element(data[0].trim(), nodes, Element.BEAM2);
                    
                    mesh.addElement(elm);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CL_2DBEAM2.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;

    }

}
