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
import plegat.solver.Material;
import plegat.solver.Mesh;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_MAT_ISO_LIN {

    public String read(BufferedReader br, Mesh mesh) {

        System.out.println("test MAT_ISO_LIN ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte = texte.trim();

                if (texte.startsWith("*")) {

                    System.out.println("code retour envoi fonction MAT_ISO_LIN: " + texte);

                    return texte;
                } else {

                    if (!texte.startsWith("$")) {
                        System.out.println("ligne: " + texte);

                    // code specifique MAT_ISO_LIN
                        String[] data = texte.split(",");

                        Material mat = new Material(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]));

                        mesh.addMaterial(mat);
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CL_MAT_ISO_LIN.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
