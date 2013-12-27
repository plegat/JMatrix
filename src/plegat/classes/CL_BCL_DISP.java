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
import plegat.solver.NodalBCL;
import plegat.solver.Node;
import plegat.solver.NodeGroup;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class CL_BCL_DISP {
    
    public String read(BufferedReader br, Mesh mesh) {
        
        System.out.println("test BCL_DISP ok");

        String texte;

        try {

            while ((texte = br.readLine()) != null) {

                texte=texte.trim();
                
                if (texte.startsWith("*")) {
                    
                    System.out.println("code retour envoi fonction BCL_DISP: "+texte);
                    
                    return texte;
                } else {
                    
                    System.out.println("ligne: "+texte);
                    
                    // code specifique BCL_DISP
                    
                    String[] data=texte.split(",");
                    
                    NodeGroup grp=mesh.getNodeGroupByName(data[1]);
                    
                    double[] bclData=new double[3];
                    for (int i = 0; i < 3; i++) {
                        bclData[i]=Double.parseDouble(data[i+2]);
                    }
                            
                    NodalBCL bcl=new NodalBCL(data[0].trim(),NodalBCL.NODAL_DISP, grp.getNodes() ,bclData);
                    
                    mesh.addNodalBCL(bcl);
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(CL_BCL_DISP.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;

    }

}
