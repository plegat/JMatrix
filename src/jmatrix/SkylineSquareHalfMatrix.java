/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class SkylineSquareHalfMatrix implements Serializable {

    private final int n;
    private ArrayList<Double> val;
    private ArrayList<Integer> ptr;
    private static final double EPSILON = 1e-6;
    public static final int UPPER = 0;
    public static final int LOWER = 1;
    private int type;

    public SkylineSquareHalfMatrix(int n, int type) {
        this.n = n;
        this.type = type;

        this.init();

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSize() {
        return this.n;
    }

    public void init(double value) {

        this.val = new ArrayList<>(6 * this.n);
        this.ptr = new ArrayList<>(this.n + 1);

        for (int i = 0; i < n; i++) {
            this.val.add(i, value);
            if (i == 0) {
                this.ptr.add(0, 0);
            } else {
                this.ptr.add(i, i);
            }
        }
        this.ptr.add(n, n);
    }

    public void initAsIdentity() {
        this.init(1.);
    }

    public final void init() {
        this.init(0.);
    }

    public final void setVal(int row, int col, double value) {

        // check value==0
        if (Math.abs(value) < SkylineSquareHalfMatrix.EPSILON) {
            value=0.;
        }

        // check out of bounds
        if ((row < 1) || (col < 1) || (row > n) || (col > n)) {
            return;
        } else if (this.type == UPPER) {
            if (row > col) {
                return;
            }
        } else if (this.type == LOWER) {
            if (col > row) {
                return;
            }
        }

        int ptr_col = this.ptr.get(row - 1);
        int ptr_col_next = this.ptr.get(row);

        int nb_elem = ptr_col_next - ptr_col;

        if (Math.abs(row - col) + 1 > nb_elem) {

            int decal = Math.abs(row - col) - nb_elem + 1;

            this.val.add(ptr_col, value);
            for (int i = 1; i < Math.abs(row - col) - nb_elem + 1; i++) {
                this.val.add(ptr_col + 1, 0.);
            }

            for (int i = row; i <= this.n; i++) {
                this.ptr.set(i, this.ptr.get(i) + decal);
            }

        } else {

            //insertion sans décalage
            int decal = nb_elem - Math.abs(row - col) - 1;

            this.val.set(ptr_col + decal, value);
        }

    }

    public double getVal(int row, int col) {

        // check out of bounds
        if ((row < 1) || (col < 1) || (row > n) || (col > n)) {
            return 0.;
        } else if (this.type == UPPER) {
            if (row > col) {
                return 0.;
            }
        } else if (this.type == LOWER) {
            if (col > row) {
                return 0.;
            }
        }

        int ptr_col = this.ptr.get(row - 1);
        int ptr_col_next = this.ptr.get(row);

        int nb_elem = ptr_col_next - ptr_col;

        if (Math.abs(row - col) + 1 > nb_elem) {
            return 0;
        } else {
            int decal = nb_elem - Math.abs(row - col) - 1;

            return this.val.get(ptr_col + decal);
        }

    }

    public BufferedImage getImage() {

        BufferedImage img = new BufferedImage(2 * n, 2 * n, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (Math.abs(this.getVal(i + 1, j + 1)) > SkylineSquareHalfMatrix.EPSILON) {
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

    public BufferedImage getScaledImage(int size) {

        double ratio = 2.0 * n / (size - 1);

        BufferedImage img = new BufferedImage(size + 1, size + 1, BufferedImage.TYPE_INT_RGB);

        int xmax = Integer.MIN_VALUE;
        int ymax = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i + 1; j++) {
                if (Math.abs(this.getVal(i + 1, j + 1)) > SkylineSquareHalfMatrix.EPSILON) {

                    if ((int) Math.round(2 * i / ratio) > xmax) {
                        xmax = (int) Math.round(2 * i / ratio);
                    }
                    if ((int) Math.round(2 * j / ratio) > ymax) {
                        ymax = (int) Math.round(2 * j / ratio);
                    }

                    img.setRGB((int) Math.round(2 * i / ratio), (int) Math.round(2 * j / ratio), 0);
                    img.setRGB((int) Math.round(2 * i / ratio) + 1, (int) Math.round(2 * j / ratio), 0);
                    img.setRGB((int) Math.round(2 * i / ratio), (int) Math.round(2 * j / ratio) + 1, 0);
                    img.setRGB((int) Math.round(2 * i / ratio) + 1, (int) Math.round(2 * j / ratio) + 1, 0);
                } else {
                    img.setRGB((int) Math.round(2 * i / ratio), (int) Math.round(2 * j / ratio), 0xffffff);
                    img.setRGB((int) Math.round(2 * i / ratio) + 1, (int) Math.round(2 * j / ratio), 0xffffff);
                    img.setRGB((int) Math.round(2 * i / ratio), (int) Math.round(2 * j / ratio) + 1, 0xffffff);
                    img.setRGB((int) Math.round(2 * i / ratio) + 1, (int) Math.round(2 * j / ratio) + 1, 0xffffff);
                }
            }
        }

        System.out.println("xmax/ymax: " + xmax + "/" + ymax);

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

    @Override
    public String toString() {

        DecimalFormat df=new DecimalFormat(" 0.00;-0.00");
        
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

    public String valToString() {

        StringBuilder buf = new StringBuilder();

        buf.append("val table:\n");

        for (int i = 0; i < this.val.size(); i++) {
            buf.append(this.val.get(i));
            buf.append(" ");
        }
        buf.append("\n");

        return buf.toString();
    }

    public String ptrToString() {

        StringBuilder buf = new StringBuilder();

        buf.append("ptr table:\n");

        for (int i = 0; i < this.ptr.size(); i++) {
            buf.append(this.ptr.get(i));
            buf.append(" ");
        }
        buf.append("\n");

        return buf.toString();
    }

    public void trimToSize() {
        this.val.trimToSize();
        this.ptr.trimToSize();
    }

    public SkylineSquareHalfMatrix copyTo(int typeCopy) {

        SkylineSquareHalfMatrix matCopy = new SkylineSquareHalfMatrix(n, typeCopy);

        double value;

        for (int i = 1; i <= n; i++) {

            int rank = this.ptr.get(i - 1);
            int rankNext = this.ptr.get(i);

            for (int j = 0; j < rankNext - rank; j++) {

                if (this.type == typeCopy) {
                    if (this.type == UPPER) {
                        value = this.getVal(i, i + j);
                        matCopy.setVal(i, i + j, value);
                    } else {
                        value = this.getVal(i, j+1);
                        matCopy.setVal(i, j+1, value);
                    }
                } else if (this.type == UPPER) {
                    value = this.getVal(i, i + j);
                    matCopy.setVal(i + j, i, value);
                } else {
                    value = this.getVal(i, j + 1);
                    matCopy.setVal(j + 1, i, value);
                }

            }

        }

        return matCopy;
    }

    public SquareMatrix convertToSymSquare() {
        
        SquareMatrix matConvert=new SquareMatrix(n);

        
        double value;

        for (int i = 1; i <= n; i++) {

            int rank = this.ptr.get(i - 1);
            int rankNext = this.ptr.get(i);

            for (int j = 0; j < rankNext - rank; j++) {

                if (this.type == UPPER) {
                    value = this.getVal(i, i + j);
                    matConvert.setVal(i, i + j, value);
                    matConvert.setVal(i + j,i,  value);
                } else {
                    value = this.getVal(i, j + 1);
                    matConvert.setVal(i, j + 1, value);
                    matConvert.setVal(j + 1,i, value);
                }

            }

        }

        return matConvert;
        
        
    }
    
    public SquareMatrix convertToSquare() {
        
        SquareMatrix matConvert=new SquareMatrix(n);

        
        double value;

        for (int i = 1; i <= n; i++) {

            int rank = this.ptr.get(i - 1);
            int rankNext = this.ptr.get(i);

            for (int j = 0; j < rankNext - rank; j++) {

                if (this.type == UPPER) {
                    value = this.getVal(i, i + j);
                    matConvert.setVal(i, i + j, value);
                } else {
                    value = this.getVal(i, j + 1);
                    matConvert.setVal(i, j + 1, value);
                }

            }

        }

        return matConvert;
        
        
    }
    
    public int getStartColumn(int row) {
        
        if (this.type==UPPER) {
            return row;
        } else {
            int rank=this.ptr.get(row-1);
            int rankNext=this.ptr.get(row);
            
            return row-(rankNext-rank)+1;
        }        
    }
    
    public int getEndColumn(int row) {
        
        if (this.type==LOWER) {
            return row;
        } else {
            int rank=this.ptr.get(row-1);
            int rankNext=this.ptr.get(row);
            
            return row+(rankNext-rank)-1;
        }        
    }
    
 
    public Vector product(Vector vect) {
        
        Vector prod=new Vector();
        int nbElm=vect.getValNumber();
        
        for (int i = 0; i < nbElm; i++) {
            
            int rank=vect.getRankValue(i);
            double value=vect.getValueAtRank(i);
            
            for (int j = 0; j < n; j++) {
                
                
                
                
            }
            
            
        }
        
        
        
        
        return prod;
    }
    
    
    
    
    public static void main(String[] args) {

        int taille=10;
        
        SkylineSquareHalfMatrix matLow = new SkylineSquareHalfMatrix(taille, UPPER);
        matLow.setRandom((int)(taille*taille*0.5), -1., 1.);
        
        System.out.println(matLow.toString());
        
        for (int i = 0; i < taille; i++) {
            System.out.println("ligne "+(i+1)+": "+matLow.getStartColumn(i+1)+" - "+matLow.getEndColumn(i+1));
        }
        
    }

}
