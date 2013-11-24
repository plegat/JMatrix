/*
 * Cette œuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.jmatrix;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class SquareMatrix implements Serializable {

    private final int n;
    private final double[][] val;
    private static final double EPSILON = 1e-6;

    public SquareMatrix(int n) {
        this.n = n;
        val = new double[n][n];

        this.init();
    }

    public void init(double diagonalValue) {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    val[i][j] = diagonalValue;
                } else {
                    val[i][j] = 0;
                }

            }

        }
    }

    public final void init() {
        this.init(0.);
    }

    public int getSize() {
        return this.n;
    }

    public void setVal(int row, int col, double value) {
        this.val[row - 1][col - 1] = value;
    }

    public double getVal(int row, int col) {
        return this.val[row - 1][col - 1];
    }

    public BufferedImage getImage() {

        BufferedImage img = new BufferedImage(2 * n, 2 * n, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(val[i][j]) > SquareMatrix.EPSILON) {
                    img.setRGB(2 * i, 2 * j, 0);
                    img.setRGB(2 * i + 1, 2 * j, 0);
                    img.setRGB(2 * i, 2 * j + 1, 0);
                    img.setRGB(2 * i + 1, 2 * j + 1, 0);
                } else {
                    img.setRGB(2 * i, 2 * j, 0xffffff);
                    img.setRGB(2 * i + 1, 2 * j, 0xffffff);
                    img.setRGB(2 * i, 2 * j + 1, 0xffffff);
                    img.setRGB(2 * i + 1, 2 * j + 1, 0xffffff);
                }
            }
        }

        return img;
    }

    public void setRandom(int nbValues, double min, double max) {

        double factor = max - min;

        for (int i = 0; i < nbValues; i++) {

            int row = Math.min((int) Math.round(Math.random() * n) + 1, n);
            int col = Math.min((int) Math.round(Math.random() * n) + 1, n);

            double value = Math.random() * factor + min;

            this.setVal(row, col, value);
        }

    }

    public SquareMatrix LUDecomposition() {

        SquareMatrix low = new SquareMatrix(n);
        low.init();

        for (int i = 1; i <= n; i++) {

            int rank = i;
            while ((rank <= n) && (Math.abs(this.getVal(rank, i)) < EPSILON)) {
                rank++;
            }

            if (rank > n) {
                System.out.println("matrice non décomposable LU");
            } else if (rank > i) {

                for (int j = i; j <= n; j++) {
                    this.setVal(i, j, this.getVal(i, j) + this.getVal(rank, j) / this.getVal(rank, i));
                }
            }

            double pivot = this.getVal(i, i);

            // remplissage matrice low
            low.setVal(i, i, 1);
            for (int j = i + 1; j <= n; j++) {
                if (Math.abs(this.getVal(j, i)) < EPSILON) {
                    low.setVal(j, i, 0.);
                } else {
                    low.setVal(j, i, this.getVal(j, i) / pivot);
                }
            }

            // modif matrice courante
            for (int j = i + 1; j <= n; j++) {

                double factor = this.getVal(j, i);

                if (Math.abs(factor) > EPSILON) {
                    for (int k = i; k <= n; k++) {
                        this.setVal(j, k, this.getVal(j, k) -this.getVal(i, k)/pivot*factor);
                    }
                } else {
                    this.setVal(j, i, 0.);
                }

            }

        }

        return low;

    }

    public void initAsIdentity() {

        this.init(1.);

    }

    @Override
    public String toString() {

        DecimalFormat df=new DecimalFormat(" 0.000000;-0.000000");
        
        
        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {

                double value = this.getVal(i + 1, j + 1);

                
                buf.append(df.format(value));
                buf.append(" ");

            }
            buf.append("\n");
        }

        return buf.toString();
    }

    public SquareMatrix matMult(SquareMatrix mat2) {
        
        SquareMatrix mult=new SquareMatrix(n);
        mult.init();
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                
                double prod=0;
                
                for (int k = 1; k <= n; k++) {
                    
                    prod=prod+this.getVal(i, k)*mat2.getVal(k, j);
                    
                }
                
                mult.setVal(i, j, prod);
                
            }
        }
        
        return mult;
        
        
    }
    
    public SquareMatrix matMinus(SquareMatrix mat2) {
        
        SquareMatrix minus=new SquareMatrix(n);
        minus.init();
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                minus.setVal(i, j, this.getVal(i, j)-mat2.getVal(i, j));
                        
            }
        }
        
        return minus;
        
    }
    
    
    public SquareMatrix copy() {
        
        SquareMatrix copy=new SquareMatrix(n);
        copy.init();
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                copy.setVal(i, j, this.getVal(i, j));
                        
            }
        }
        
        return copy;
        
    }
    
    public static void main(String[] args) {

        SquareMatrix mat = new SquareMatrix(5);

        mat.setRandom(80, -5, 5);

        System.out.println("mat:");
        System.out.println(mat.toString());
        
        SquareMatrix copie=mat.copy();

        System.out.println("LU decomposition");
        
        SquareMatrix l=mat.LUDecomposition();
        
        System.out.println("matrice upper:");
        System.out.println(mat.toString());
        
        System.out.println("matrice lower:");
        System.out.println(l.toString());
        
        System.out.println("matrice L*U:");
        SquareMatrix mult=l.matMult(mat);
        System.out.println(mult.toString());
        
        System.out.println("delta:");
        mult=mult.matMinus(copie);
        System.out.println(mult.toString());
        
        
    }

}
