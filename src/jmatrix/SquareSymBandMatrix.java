/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

import java.awt.image.BufferedImage;
import java.io.Serializable;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class SquareSymBandMatrix implements Serializable {

    private final int n; // square matrix dimension
    private final int l; // half bandwith
    private final double[][] val;
    private static final double EPSILON = 1e-6;

    public SquareSymBandMatrix(int n, int l) {
        this.n = n;
        this.l = l;
        val = new double[n][l];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                val[i][j] = 0;
            }

        }
    }

    public int getSize() {
        return this.n;
    }
    
    public int getHalfBandwidth() {
        return this.l;
    }
    
    public boolean setVal(int row, int col, double value) {

        if ((col - row <= l) && (col <= this.n)) {
            this.val[row - 1][col - row] = value;
            return true;
        } else {
            return false;
        }

    }

    public double getVal(int row, int col) {
        if ((col - row <= l) && (col <= this.n)) {
            return this.val[row - 1][col - row];
        } else {
            return 0;
        }
    }

    public BufferedImage getImage() {

        BufferedImage img = new BufferedImage(2 * n, 2 * n, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {

                int col = Math.min(i + j, n - 1);

                if (Math.abs(val[i][j]) > SquareSymBandMatrix.EPSILON) {
                    img.setRGB(2 * i, 2 * col, 0);
                    img.setRGB(2 * i + 1, 2 * col, 0);
                    img.setRGB(2 * i, 2 * col + 1, 0);
                    img.setRGB(2 * i + 1, 2 * col + 1, 0);

                    img.setRGB(2 * col, 2 * i, 0);
                    img.setRGB(2 * col, 2 * i + 1, 0);
                    img.setRGB(2 * col + 1, 2 * i, 0);
                    img.setRGB(2 * col + 1, 2 * i + 1, 0);

                } else {
                    img.setRGB(2 * i, 2 * col, 0xffffff);
                    img.setRGB(2 * i + 1, 2 * col, 0xffffff);
                    img.setRGB(2 * i, 2 * col + 1, 0xffffff);
                    img.setRGB(2 * i + 1, 2 * col + 1, 0xffffff);

                    img.setRGB(2 * col, 2 * i, 0xffffff);
                    img.setRGB(2 * col, 2 * i + 1, 0xffffff);
                    img.setRGB(2 * col + 1, 2 * i, 0xffffff);
                    img.setRGB(2 * col + 1, 2 * i + 1, 0xffffff);
                }



            }

            for (int j = i + l; j < n; j++) {
                img.setRGB(2 * i, 2 * j, 0xffffff);
                img.setRGB(2 * i + 1, 2 * j, 0xffffff);
                img.setRGB(2 * i, 2 * j + 1, 0xffffff);
                img.setRGB(2 * i + 1, 2 * j + 1, 0xffffff);

                img.setRGB(2 * j, 2 * i, 0xffffff);
                img.setRGB(2 * j, 2 * i + 1, 0xffffff);
                img.setRGB(2 * j + 1, 2 * i, 0xffffff);
                img.setRGB(2 * j + 1, 2 * i + 1, 0xffffff);

            }
        }

        return img;
    }

    public void setRandom(int nbValues, double min, double max) {

        
        double factor = max - min;

        int rowMin=Integer.MAX_VALUE;
        int colMin=Integer.MAX_VALUE;
        
        for (int i = 0; i < nbValues; i++) {

            int row = Math.min((int) Math.round(Math.random() * n) + 1, n - 1);
            int col = Math.min((int) Math.round(Math.random() * l) + 1, l - 1);

            if (row<rowMin) rowMin=row;
            if (col<colMin) colMin=col;
            
            double value = Math.random() * factor + min;

            this.val[row-1][col-1] = value;
        }

        System.out.println("rowmin/colMin: "+rowMin+"/"+colMin);

    }
}
