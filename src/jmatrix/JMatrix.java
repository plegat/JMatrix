/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class JMatrix {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        int n = 2000;
        int lBand = 200;

        //SquareMatrix mat = new SquareMatrix(n);
        SquareSymBandMatrix mat1 = new SquareSymBandMatrix(n, lBand);

        mat1.setRandom(10000, -100, 100);

        System.out.println("initialisation mat1 ok");

        
        SkylineSquareSymMatrix mat2 = new SkylineSquareSymMatrix(mat1);

        /*
         for (int i = 0; i < n; i++) {
         mat.setVal(i+1, i+1, 1);
         mat.setVal(i+1, i+3, 1);
         mat.setVal(i+1, i+7, 1);
            
         }
         */
        System.out.println("initialisation mat2 ok");

        BufferedImage img = mat2.getScaledImage(600);
        
        System.out.println("création image ok");
        
        System.out.println("sizeOf mat1: "+getObjectSize(mat1));
        System.out.println("sizeOf mat2: "+getObjectSize(mat2));

        JFrameMatrix jfm = new JFrameMatrix();
        jfm.setImage(img);
        jfm.revalidate();
        //jfm.setSize(2 * n + 100, 2 * n + 100);
        jfm.setSize(700, 700);
        jfm.setVisible(true);

    }

    private static int getObjectSize(Object obj) {
        if (obj == null) {
            return 0;
        }
        byte abyte0[];
        try {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            ObjectOutputStream objectoutputstream = new ObjectOutputStream(bytearrayoutputstream);
            objectoutputstream.writeObject(obj);
            abyte0 = bytearrayoutputstream.toByteArray();
            return abyte0.length;
        } catch (Exception exception) {
            return 0;
        }
    }

}
