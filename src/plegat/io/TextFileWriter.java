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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class TextFileWriter {

    private final String basePath;
    BufferedWriter fichier;

    public TextFileWriter(String basePath) throws IOException {
        this.basePath = basePath;
        
        fichier = new BufferedWriter(new FileWriter(basePath));
    }

    public void close() throws IOException {

        this.fichier.flush();
        this.fichier.close();

    }

    public boolean writeln(String texte) {

        try {
            this.fichier.write(texte);
            this.fichier.write(System.lineSeparator());
            return true;
        } catch (IOException ex) {
            Logger.getLogger(BinaryFileWriter.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
