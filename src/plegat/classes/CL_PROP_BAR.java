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
import plegat.solver.Property;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_PROP_BAR {
    
    public String read(BufferedReader br, Mesh mesh) {
        
        System.out.println("test PROP_BAR ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte=texte.trim();
                
                if (texte.startsWith("*")) {
                    
                    System.out.println("code retour envoi fonction PROP_BAR: "+texte);
                    
                    return texte;
                } else {
                    
                    System.out.println("ligne: "+texte);
                    
                    // code specifique PROP_BAR
                    
                    String[] data=texte.split(",");
                    
                    double[] dataProp=new double[1];
                    dataProp[0]=Double.parseDouble(data[2]);
                    
                    Property prop=new Property(data[0], mesh.getMaterialByName(data[1]),dataProp,Property.BAR);
                    
                    mesh.addProperty(prop);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CL_2DNODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;

    }

}
