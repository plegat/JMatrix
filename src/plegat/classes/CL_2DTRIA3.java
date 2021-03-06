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
public class CL_2DTRIA3 {

    public String read(BufferedReader br, Mesh mesh) {

        System.out.println("test 2DTRIA3 ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte = texte.trim();

                if (texte.startsWith("*")) {

                    System.out.println("code retour envoi fonction 2DTRIA3: " + texte);

                    return texte;
                } else {

                    if (!texte.startsWith("$")) {
                        System.out.println("ligne: " + texte);

                    // code specifique 2DTRIA3
                        String[] data = texte.split(",");

                        Node[] nodes = new Node[3];
                        nodes[0] = mesh.getNodeByID(data[1].trim());
                        nodes[1] = mesh.getNodeByID(data[2].trim());
                        nodes[2] = mesh.getNodeByID(data[3].trim());

                        Element elm = new Element(data[0].trim(), nodes, Element.TRIA3);

                        mesh.addElement(elm);
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CL_2DTRIA3.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
