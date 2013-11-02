/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class SkylineSquareSymMatrix implements Serializable {

    private final int n;
    private ArrayList<Double> val;
    private ArrayList<Integer> ptr;
    private static final double EPSILON = 1e-6;

    public SkylineSquareSymMatrix(int n) {
        this.n = n;

        this.init();

    }

    public SkylineSquareSymMatrix(SquareSymBandMatrix ssm) {

        this.n = ssm.getSize();

        int l = ssm.getHalfBandwidth();

        this.init();

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < l; j++) {

                this.setVal(i + 1, i + j + 1, ssm.getVal(i + 1, i + j + 1));

            }

        }

    }

    public int getSize() {
        return this.n;
    }

    public final void init() {

        this.val = new ArrayList<>(6 * this.n);
        this.ptr = new ArrayList<>(this.n + 1);

        for (int i = 0; i < n; i++) {
            this.val.add(i, 0.);
            if (i == 0) {
                this.ptr.add(0, 0);
            } else {
                this.ptr.add(i, i);
            }
        }
        this.ptr.add(n, n);
    }

    public final void setVal(int row, int col, double value) {

        if (Math.abs(value)<SkylineSquareSymMatrix.EPSILON) {
            //
        } else if ((row < 1) || (col < 1) || (row > n) || (col > n)) {
            //
        } else if (row < col) {
            this.setVal(col, row, value);
        } else {

            int ptr_col = this.ptr.get(row - 1);
            int ptr_col_next = this.ptr.get(row);

            int nb_elem = ptr_col_next - ptr_col;

            if (row - col + 1 > nb_elem) {

                int decal = (row - col) - nb_elem + 1;

                this.val.add(ptr_col, value);
                for (int i = 1; i < row - col - nb_elem + 1; i++) {
                    this.val.add(ptr_col + 1, 0.);
                }

                for (int i = row; i <= this.n; i++) {
                    this.ptr.set(i, this.ptr.get(i) + decal);
                }

            } else {

                //insertion sans décalage
                int decal = nb_elem - (row - col) - 1;

                this.val.set(ptr_col + decal, value);
            }

        }
    }

    public double getVal(int row, int col) {

        if ((row < 1) || (col < 1) || (row > n) || (col > n)) {
            return 0;
        } else if (row < col) {
            return this.getVal(col, row);
        } else {

            int ptr_col = this.ptr.get(row - 1);
            int ptr_col_next = this.ptr.get(row);

            int nb_elem = ptr_col_next - ptr_col;

            if (row - col + 1 > nb_elem) {
                return 0;
            } else {
                int decal = nb_elem - (row - col) - 1;

                return this.val.get(ptr_col + decal);
            }

        }

    }

    public BufferedImage getImage() {

        BufferedImage img = new BufferedImage(2 * n, 2 * n, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(this.getVal(i + 1, j + 1)) > SkylineSquareSymMatrix.EPSILON) {
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

        double ratio = 2.0 * n / (size-1);

        BufferedImage img = new BufferedImage(size+1, size+1, BufferedImage.TYPE_INT_RGB);

        int xmax=Integer.MIN_VALUE;
        int ymax=Integer.MIN_VALUE;
        
        
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (Math.abs(this.getVal(i + 1, j + 1)) > SkylineSquareSymMatrix.EPSILON) {
                    
                    if ((int) Math.round(2 * i / ratio)>xmax) xmax=(int) Math.round(2 * i / ratio);
                    if ((int) Math.round(2 * j / ratio)>ymax) ymax=(int) Math.round(2 * j / ratio);
                    
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

        System.out.println("xmax/ymax: "+xmax+"/"+ymax);
        
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

        StringBuilder buf = new StringBuilder();

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {

                double value = this.getVal(i + 1, j + 1);

                buf.append(value);
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

    public static void main(String[] args) {

        SkylineSquareSymMatrix mat = new SkylineSquareSymMatrix(5);

        mat.setVal(1, 1, 1);
        mat.setVal(2, 1, -1);
        mat.setVal(3, 1, -3);
        mat.setVal(2, 2, 5);
        mat.setVal(3, 3, 4);
        mat.setVal(4, 3, 6);
        mat.setVal(5, 3, 4);
        mat.setVal(4, 4, 7);
        mat.setVal(5, 5, -5);

        mat.trimToSize();

        System.out.println("matrix:");
        System.out.println(mat.toString());

    }

}
