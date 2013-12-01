/*
 * Cette oeuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.solver;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import plegat.io.BinaryFileWriter;
import plegat.io.InputFileReader;
import plegat.io.TextFileWriter;
import plegat.jmatrix.ProblemMatrix;
import plegat.jmatrix.Vector;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Problem {
    
    private final String inputFile;
    private Mesh mesh;

    public Problem(String inputFile) {
        this.inputFile = inputFile;
        this.mesh=new Mesh();
    }
    
    
    
    public void solve() {
        
        InputFileReader ifr=new InputFileReader(this.inputFile, this.mesh);
        ifr.read();
        
        String basename;
        if (this.inputFile.endsWith(".dat")) {
            basename=this.inputFile.substring(0, this.inputFile.length()-4);
        } else {
            basename=this.inputFile;
        }
        
        ProblemMatrix pbmat=mesh.getMatrix();
        Vector disp=mesh.getDisp();
        Vector load=mesh.getLoad();
        
        double[][] result=pbmat.solve(disp, load);
        
        try {
            BinaryFileWriter binRes=new BinaryFileWriter(basename+".bin");
            TextFileWriter textLog=new TextFileWriter(basename+".log");
            binRes.setLogfile(textLog);
            
            TextFileWriter textRes=new TextFileWriter(basename+".res");
            binRes.setTextResFile(textRes);
            
            
            binRes.writeProblemResults(result, this.mesh.getRCMOptimization(), mesh);
            
            binRes.close();
            textLog.close();
            textRes.close();
            
            
        } catch (IOException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    
    
    
}
