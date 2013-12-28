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
import plegat.solver.Loadcase;
import plegat.solver.Mesh;
import plegat.solver.NodalBCL;
import plegat.solver.Node;
import plegat.solver.NodeGroup;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_LOADCASE {

    public String read(BufferedReader br, Mesh mesh) {

        System.out.println("test LOADCASE ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte = texte.trim();

                if (texte.startsWith("*")) {

                    System.out.println("code retour envoi fonction BCL_LOADCASE: " + texte);

                    return texte;
                } else {

                    if (!texte.startsWith("$")) {
                        System.out.println("ligne: " + texte);

                    // code specifique LOADCASE
                        String[] data = texte.split(",");

                        Loadcase lc = new Loadcase(data[0].trim());

                        for (int i = 1; i < data.length; i++) {
                            NodalBCL bcl = mesh.getBCLByName(data[i].trim());

                            if (bcl != null) {
                                lc.addBCL(bcl);
                            }
                        }

                        mesh.addLoadcase(lc);
                    }
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CL_LOADCASE.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
