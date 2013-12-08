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
import plegat.solver.Mesh;
import plegat.solver.NodalBCL;
import plegat.solver.NodeGroup;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_BCL_LOAD {
    
    public String read(BufferedReader br, Mesh mesh) {
        
        System.out.println("test BCL_LOAD ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte=texte.trim();
                
                if (texte.startsWith("*")) {
                    
                    System.out.println("code retour envoi fonction BCL_LOAD: "+texte);
                    
                    return texte;
                } else {
                    
                    System.out.println("ligne: "+texte);
                    
                    // code specifique BCL_LOAD
                    
                    String[] data=texte.split(",");
                    
                    NodeGroup grp=mesh.getNodeGroupByName(data[0]);
                    
                    double[] bclData=new double[3];
                    for (int i = 0; i < 3; i++) {
                        bclData[i]=Double.parseDouble(data[i+1]);
                    }
                            
                    NodalBCL bcl=new NodalBCL(NodalBCL.NODAL_FORCE, grp.getNodes() ,bclData);
                    
                    mesh.addNodalBCL(bcl);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CL_2DNODE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;

    }

}