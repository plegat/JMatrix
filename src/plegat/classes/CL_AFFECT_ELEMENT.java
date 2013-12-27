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
import plegat.solver.ElementGroup;
import plegat.solver.Mesh;
import plegat.solver.Property;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_AFFECT_ELEMENT {

    public String read(BufferedReader br, Mesh mesh) {

        System.out.println("test AFFECT_ELEMENT ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte = texte.trim();

                if (texte.startsWith("*")) {

                    System.out.println("code retour envoi fonction AFFECT_ELEMENT: " + texte);

                    return texte;
                } else {

                    System.out.println("ligne: " + texte);

                    // code specifique AFFECT_ELEMENT
                    String[] data = texte.split(",");

                    ElementGroup grp = mesh.getElementGroupByName(data[1]);
                    Property prop = mesh.getPropertyByName(data[0]);

                    if (grp == null) {
                        System.out.println("grp null");
                        System.out.println("name: "+data[1]);
                        mesh.listElementGroup();
                    }
                    if (prop == null) {
                        System.out.println("prop null");
                    }

                    System.out.println("groupe:   " + grp.getId());
                    System.out.println("property: " + prop.getName());

                    mesh.addElementGroupProperty(grp, prop);
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CL_AFFECT_ELEMENT.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
