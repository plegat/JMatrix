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

    public double[][] solve(Vector displacements, Vector loads) {

        displacements.sort();
        loads.sort();

        if (this.state == RAW) {
            this.LUDecomposition();
        }

        double[][] result = new double[2][this.n];

        double[] interm = new double[n];

        // resolution matLow.interm=loads
        for (int i = 1; i <= this.n; i++) {

            double calc = loads.getVal(i);

            int startColumn = matLow.getStartColumn(i);

            for (int j = startColumn; j < i; j++) {

                double factor = this.matLow.getVal(i, j);

                calc = calc - factor * interm[j - 1];

            }

            interm[i - 1] = calc;

        }

        // resolution matUp.disp=interm
        // passage déplacements imposes à droite 
        int nbDispImposed = displacements.getValNumber();

        for (int row = 1; row <= this.n; row++) {

            for (int i = 0; i < nbDispImposed; i++) {

                int rowDisp = displacements.getRankValue(i);

                if (rowDisp >= row) {

                    double dispImposed = displacements.getVal(i);

                    interm[row - 1] = interm[row - 1] - dispImposed * matUp.getVal(row, rowDisp);
                }
            }
        }

        // resolution
        for (int row = this.n; row > 0; row--) {

            int dispImposedIndex = displacements.getIndexOf(row);

            if (dispImposedIndex > -1) {
                result[0][row - 1] = 0.;
            } else {

                double calc = interm[row - 1];

                for (int i = row + 1; i <= this.n; i++) {
                    calc = calc - this.matUp.getVal(row, i) * result[0][i - 1];
                }

                result[0][row - 1] = calc / this.matUp.getVal(row, row);
            }
        }
        
        
        // deplacements imposes    
        for (int i = 0; i < nbDispImposed; i++) {

            int rowDisp = displacements.getRankValue(i);
            double dispImposed = displacements.getVal(i);

            result[0][rowDisp - 1] = dispImposed;

        }

        
        // calcul des forces
        
        
        //calcul Ux
        for (int row = 1; row <= this.n; row++) {

            double calc=0;
            
            for (int i = row; i <= Math.min(this.n,this.matUp.getEndColumn(row)); i++) {
                calc=calc+this.matUp.getVal(row, i)*result[0][i-1];
            }
            
            interm[row-1]=calc;
        }

        //calcul LUx
        
        for (int row = 1; row <= this.n; row++) {

            double calc=0;
            
            for (int i = Math.max(1,this.matLow.getStartColumn(row)); i <= row; i++) {
                calc=calc+this.matLow.getVal(row, i)*result[0][i-1];
            }
            
            result[1][row-1]=calc;
        }
        
        return result;
    }

    public static void main(String[] args) {

        int n=10;
        int nbVal=100;
        
        SkylineSquareHalfMatrix matUp = new SkylineSquareHalfMatrix(n, SkylineSquareHalfMatrix.UPPER);
        matUp.setRandom(nbVal, 0.1, 10);
        
        System.out.println("matUp");
        System.out.println(matUp.toString());

        SquareMatrix matSq=matUp.convertToSymSquare();
        
        System.out.println("matSq:");
        System.out.println(matSq.toString());
        
        ProblemMatrix pbMat=new ProblemMatrix(n);
        
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                
                pbMat.setVal(i, j, matSq.getVal(i, j));
                
            }
        }
        
        System.out.println("pbMat:");
        System.out.println(pbMat.toString());
        
        pbMat.LUDecomposition();
        
        System.out.println("pbMat:");
        System.out.println(pbMat.toString());
        
        
    }

}
