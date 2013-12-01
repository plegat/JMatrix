/*
 * Cette oeuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */
package plegat.io;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import plegat.solver.Mesh;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class BinaryFileWriter {

    private final String basePath;
    DataOutputStream fichier;
    TextFileWriter logfile;
    TextFileWriter textResFile;

    private static final int __STRING__ = 1;
    private static final int __INTEGER__ = 2;
    private static final int __POINT__ = 3;
    private static final int __ELEMENT__ = 4;

    public BinaryFileWriter(String basePath) throws IOException {
        this.basePath = basePath;
        this.logfile = null;
        this.textResFile = null;

        fichier = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(basePath)));

    }

    public void close() throws IOException {

        this.fichier.flush();
        this.fichier.close();

    }

    public void setLogfile(TextFileWriter logfile) {
        this.logfile = logfile;
    }

    public void setTextResFile(TextFileWriter textResFile) {
        this.textResFile = textResFile;
    }

    public boolean writeString(String texte) {
        try {
            /*
             byte[] binTexte = texte.getBytes("UTF-8");

             this.fichier.write(__STRING__);
             this.fichier.write(binTexte.length);
             */
            this.fichier.writeUTF(texte);

            return true;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(BinaryFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(BinaryFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean writeInt(int entier) {
        try {
            this.fichier.write(__INTEGER__);
            this.fichier.write(entier);

            return true;
        } catch (IOException ex) {
            Logger.getLogger(BinaryFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean writeProblemResults(double[][] results, int[] rcmRanks,Mesh mesh) {

        int nbData = results[0].length / 3;

        try {
            
            this.fichier.writeInt(nbData);

            for (int j = 0; j <= 1; j++) {
                // j=0: ecriture des déplacements
                // j=1: ecriture des efforts/moments

                for (int i = 0; i < nbData; i++) {

                    int rank = rcmRanks[i];

                    this.fichier.writeDouble(results[j][3 * rank]);
                    this.fichier.writeDouble(results[j][3 * rank + 1]);
                    this.fichier.writeDouble(results[j][3 * rank + 2]);
                }
            }

            
            if (this.textResFile!=null) {
                this.writeProblemTextResults(results, rcmRanks, mesh);
            }
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(BinaryFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    public boolean writeProblemTextResults(double[][] results, int[] rcmRanks,Mesh mesh) {

        int nbData = results[0].length / 3;

        for (int j = 0; j <= 1; j++) {
                // j=0: ecriture des déplacements
            // j=1: ecriture des efforts/moments

            if (j == 0) {
                this.textResFile.writeln("Resultats des deplacements noeuds:");
                this.textResFile.writeln("");
                this.textResFile.writeln("ID NOEUD              DX                 DY               RZ");
                
            } else {
                this.textResFile.writeln("");
                this.textResFile.writeln("");
                this.textResFile.writeln("Resultats des efforts et moments aux noeuds:");
                this.textResFile.writeln("");
                this.textResFile.writeln("ID NOEUD              FX                 FY               MZ");
            }

            for (int i = 0; i < nbData; i++) {

                int rank = rcmRanks[i];

                StringBuilder text=new StringBuilder(mesh.getNode(i).getId());
                
                text.append("        ", 0, 16-text.length());
                
                
                for (int k = 0; k < 3; k++) {
                    String data=String.format("%- 8g", results[j][3 * rank+k]);
                    text.append(data);
                    if (k<2) {
                        text.append("        ");
                    }
                }
                
            }
        }

        return true;

    }

}
