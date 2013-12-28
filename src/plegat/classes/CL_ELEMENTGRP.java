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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import plegat.solver.Element;
import plegat.solver.ElementGroup;
import plegat.solver.Mesh;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_ELEMENTGRP {

    public String read(BufferedReader br, Mesh mesh) {

        System.out.println("test ELEMENTGRP ok");

        String texte;

        ElementGroup eg = new ElementGroup();
        int loop = 0;
        ArrayList<Element> elementList = new ArrayList<>();

        try {

            while ((texte = br.readLine()) != null) {

                texte = texte.trim();

                if (texte.startsWith("*")) {

                    eg.setElements(elementList);
                    mesh.addElementGroup(eg);

                    System.out.println("code retour envoi fonction ELEMENTGRP: " + texte);

                    return texte;
                } else {

                    if (!texte.startsWith("$")) {
                        System.out.println("ligne: " + texte);

                        // code specifique ELEMENTGRP
                        if (loop == 0) {
                            eg.setId(texte.trim());
                            loop++;
                        } else {

                            String[] data = texte.split(",");

                            for (int i = 0; i < data.length; i++) {
                                Element elm = mesh.getElementByID(data[i].trim());
                                elementList.add(elm);
                            }
                        }

                    }

                }

            }
        } catch (IOException ex) {
            Logger.getLogger(CL_ELEMENTGRP.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;

    }

}
