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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Matrix {

    int row, col;
    private final double[][] val;

    public Matrix(int row, int col) {
        this.row = row;
        this.col = col;

        this.val = new double[row][col];
        this.init();
    }

    public final void init() {

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                val[i][j] = 0.0;
            }
        }
    }

    public void setIdentity() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == j) {
                    val[i][j] = 1.0;
                } else {
                    val[i][j] = 0.0;
                }
            }
        }
    }

    public double[][] getVals() {
        return this.val;
    }
    
    public double getVal(int numRow, int numCol) {
        return this.val[numRow][numCol];
    }

    public void setVal(int numRow, int numCol, double value) {
        this.val[numRow][numCol] = value;
    }

    public Matrix product(Matrix mat2) throws Exception {

        if (this.col != mat2.row) {
            throw new Exception("Produit de matrices pas possible!");
        } else {

            Matrix temp = new Matrix(this.row, mat2.col);

            for (int i = 0; i < this.row; i++) {
                for (int j = 0; j < mat2.col; j++) {

                    double somme = 0;

                    for (int k = 0; k < this.col; k++) {
                        somme = somme + this.val[i][k] * mat2.val[k][j];
                    }

                    temp.val[i][j] = somme;
                }

            }

            return temp;

        }

    }

    
    public void mult(double coef) {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                val[i][j] = val[i][j] * coef;
            }
        }
    }

    public void multLine(int rank, double value) {
        for (int i = 0; i < this.col; i++) {
            this.val[rank][i] = this.val[rank][i] * value;
        }
    }

    public void rowSum(int firstRow, int secondRow, double coef) {

        for (int i = 0; i < col; i++) {

            this.val[secondRow][i] = this.val[secondRow][i] + coef * this.val[firstRow][i];

        }

    }

    public void symetrize() {

        if (this.row == this.col) {

            for (int i = 1; i < this.row; i++) {
                for (int j = 0; j < i; j++) {
                    this.val[i][j] = this.val[j][i];
                }
            }

        }

    }

    @Override
    public String toString() {
        return this.toString("0.000");
    }

    public String toString(String format) {

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat(format, dfs);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < row; i++) {

            for (int j = 0; j < col; j++) {
                sb.append(df.format(this.val[i][j]));
                sb.append(" ");
            }
            sb.append("\n");

        }

        return sb.toString();

    }

    @Override
    public Matrix clone() {

        int nb_lig = this.row;
        int nb_col = this.col;

        Matrix temp = new Matrix(nb_lig, nb_col);

        for (int i = 0; i < nb_lig; i++) {
            System.arraycopy(this.val[i], 0, temp.val[i], 0, nb_col);
        }

        return temp;

    }

    public Matrix removeRowCol(int[] rank) {

        int nbRank = rank.length;

        Matrix temp = new Matrix(this.row - nbRank, this.col - nbRank);

        int offset_row = 0;
        int offset_col;

        for (int i = 0; i < this.row; i++) {

            if (i == rank[offset_row]) {
                offset_row++;
            } else {

                offset_col = 0;

                for (int j = 0; j < this.col; j++) {

                    if (j == rank[offset_col]) {
                        offset_col++;
                    } else {
                        temp.val[i - offset_row][j - offset_col] = this.val[i][j];
                    }

                }
            }
        }

        return temp;

    }

    public void switchLines(int rank1, int rank2) {

        double buffer;

        for (int i = 0; i < this.col; i++) {
            buffer = this.val[rank1][i];
            this.val[rank1][i] = this.val[rank2][i];
            this.val[rank2][i] = buffer;
        }

    }

    public void initRandom(double min, double max) {

        double interval = max - min;

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                val[i][j] = (Math.random() * interval) - min;
            }
        }

    }

    public Matrix getTransposed() {

        Matrix temp = new Matrix(this.col, this.row);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                temp.val[j][i] = this.val[i][j];
            }
        }
        return temp;
    }

    public static void main(String[] args) {

        int nb = 100;

        Matrix mat = new Matrix(nb, nb);
        mat.initRandom(-10, 10);

        Matrix mat2 = mat.getTransposed();

    }
}
