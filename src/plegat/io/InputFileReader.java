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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import plegat.solver.Mesh;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class InputFileReader {

    private String path;
    private Mesh mesh;

    public InputFileReader(String path, Mesh mesh) {
        this.path = path;
        this.mesh = mesh;
    }

    public InputFileReader() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Mesh getMesh() {
        return mesh;
    }

    public void setMesh(Mesh mesh) {
        this.mesh = mesh;
    }

    public boolean read() {

        try {

            BufferedReader br = new BufferedReader(new FileReader(new File(path)));

            // premier boucle: lecture des noeuds
            String texte=null;
            boolean flag = true;
            

            while (flag) {
                
                if (texte==null) {
                    texte = br.readLine();
                }
                
                if (texte != null) {

                    texte = texte.trim();
                    //System.out.println(texte);

                    if (texte.startsWith("*")) {

                        String classe = texte.substring(1).toUpperCase();
                        System.out.println("classe: " + classe);

                        if (classe.equals("END")) {
                            flag = false;
                        } else {

                            Class clas = Class.forName("plegat.classes.CL_" + classe);
                            Method met = clas.getMethod("read", BufferedReader.class, Mesh.class);

                            Object ret=met.invoke(clas.newInstance(), br,this.mesh);

                            if (ret!=null) {
                                texte=(String)ret;
                                System.out.println("retour fonction: "+texte);
                            } else {
                                texte=null;
                            }
                            
                        }
                    } else {
                        texte=null;
                    }
                } else {
                    flag=false;
                }
            }

            // TODO la suite
            br.close();

            return true;

        } catch (FileNotFoundException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(InputFileReader.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;

    }

    public static void main(String[] args) {

        InputFileReader ifr = new InputFileReader("/home/jmb2/Bureau/test_pfem.inp", new Mesh());

        boolean flag = ifr.read();

        System.out.println("flag: " + flag);

    }

}
