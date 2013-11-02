/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmatrix;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class ProblemMatrix {

    private final int n;
    private static final double EPSILON = 1e-6;
    private final SkylineSquareHalfMatrix matUp;
    private SkylineSquareHalfMatrix matLow;
    private int state;
    public static final int RAW = 0;
    public static final int LU_DEC = 1;

    public ProblemMatrix(int n) {
        this.n = n;

        this.matUp = new SkylineSquareHalfMatrix(n, SkylineSquareHalfMatrix.UPPER);
        this.matLow = new SkylineSquareHalfMatrix(n, SkylineSquareHalfMatrix.LOWER);

        this.state = RAW;
    }

    public final void setVal(int row, int col, double value) {

        if (row <= col) {
            this.matUp.setVal(row, col, value);
        } else {
            this.matUp.setVal(col, row, value);
        }
    }

    public final double getVal(int row, int col) {

        // check out of bounds
        if ((row < 1) || (col < 1) || (row > n) || (col > n)) {
            return 0.;
        }

        if (row <= col) {
            return this.matUp.getVal(row, col);
        } else {
            return this.matUp.getVal(col, row);
        }
    }

    public boolean LUDecomposition() {

        this.matLow = this.matUp.copyTo(SkylineSquareHalfMatrix.LOWER);

        for (int i = 1; i <= n; i++) {

            double pivot = this.matUp.getVal(i, i);

            if (pivot < Math.abs(EPSILON)) {
                System.out.println("Problème de pivot nul");
                System.out.println("matrice non décomposable LU");
                return false;
            }

            // modif matrice courante
            for (int j = i + 1; j <= n; j++) {

                double factor = this.matLow.getVal(j, i);

                if (Math.abs(factor) > EPSILON) { // on ne modifie pas le terme colonne i, sera traité juste après
                    for (int k = i + 1; k < j; k++) { // partie sous la diagonale => matLow
                        this.matLow.setVal(j, k, this.matLow.getVal(j, k) - this.matUp.getVal(i, k) / pivot * factor);
                    }
                    for (int k = j; k <= n; k++) { // partie sur la diagonale => matUp
                        this.matUp.setVal(j, k, this.matUp.getVal(j, k) - this.matUp.getVal(i, k) / pivot * factor);
                    }
                }

            }

            // remplissage matrice low
            matLow.setVal(i, i, 1);
            for (int j = i + 1; j <= n; j++) {
                if (Math.abs(this.matLow.getVal(j, i)) < EPSILON) {
                    matLow.setVal(j, i, 0.);
                } else {
                    //matLow.setVal(j, i, this.getVal(j, i) / pivot);
                    this.matLow.setVal(j, i, this.matLow.getVal(j, i) / pivot);
                }
            }

        }

        this.state = LU_DEC;
        return true;

    }

    @Override
    public String toString() {

        StringBuilder buf = new StringBuilder();

        if (this.state == RAW) {
            buf.append("Raw matrix:\n");

            SquareMatrix matSq = matUp.convertToSymSquare();

            buf.append(matSq.toString());
        } else {
            buf.append("U matrix:\n");
            buf.append(this.matUp.toString());
            buf.append("L matrix:\n");
            buf.append(this.matLow.toString());

        }

        return buf.toString();

    }

    public boolean solve(Vector displacements, Vector loads) {

        
        
        
        
        
        return true;
        
    }

    public static void main(String[] args) {

        SkylineSquareHalfMatrix matUp = new SkylineSquareHalfMatrix(5, SkylineSquareHalfMatrix.UPPER);

        for (int i = 1; i <= 5; i++) {
            for (int j = i; j <= 5; j++) {
                matUp.setVal(i, j, i * 10 + j);
            }
        }

        matUp.trimToSize();
        System.out.println(matUp.valToString());

        System.out.println("matrix upper:");
        System.out.println(matUp.toString());

        SquareMatrix matSymSq = matUp.convertToSymSquare();
        System.out.println("matrix upper convertTo squareSym:");
        System.out.println(matSymSq.toString());

        SquareMatrix matLUlow = matSymSq.LUDecomposition();
        System.out.println("LU decomposition");
        System.out.println("U matrix:");
        System.out.println(matSymSq.toString());
        System.out.println("L matrix:");
        System.out.println(matLUlow.toString());

        SquareMatrix product = matLUlow.matMult(matSymSq);
        System.out.println("check product:");
        System.out.println(product.toString());

        ProblemMatrix pm = new ProblemMatrix(5);
        for (int i = 1; i <= 5; i++) {
            for (int j = i; j <= 5; j++) {
                pm.setVal(i, j, i * 10 + j);
            }
        }
        System.out.println("problem matrix");
        System.out.println(pm.toString());

        pm.LUDecomposition();

        System.out.println("problem matrix, after LU decomposition");
        System.out.println(pm.toString());

        System.out.println("check product");
        product = pm.matLow.convertToSquare().matMult(pm.matUp.convertToSquare());
        System.out.println(product.toString());

    }

}
