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

        for (int i = 1; i <= this.matUp.getSize(); i++) {

            double pivot = this.matUp.getVal(i, i);

            if (Math.abs(pivot) < EPSILON) {
                System.out.println("Problème de pivot nul: " + pivot);
                System.out.println("matrice non décomposable LU");
                return false;
            }

            // modif matrice courante
            for (int j = i + 1; j <= this.matUp.getSize(); j++) {

                double factor = this.matLow.getVal(j, i);

                if (Math.abs(factor) > EPSILON) { // on ne modifie pas le terme colonne i, sera traité juste après
                    for (int k = i + 1; k < j; k++) { // partie sous la diagonale => matLow
                        this.matLow.setVal(j, k, this.matLow.getVal(j, k) - this.matUp.getVal(i, k) / pivot * factor);
                    }
                    for (int k = j; k <= this.matUp.getSize(); k++) { // partie sur la diagonale => matUp
                        this.matUp.setVal(j, k, this.matUp.getVal(j, k) - this.matUp.getVal(i, k) / pivot * factor);
                    }
                }

            }

            // remplissage matrice low
            matLow.setVal(i, i, 1);
            for (int j = i + 1; j <= this.matUp.getSize(); j++) {
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

        System.out.println("disp");
        System.out.println(displacements.toString());
        System.out.println("loads");
        System.out.println(loads.toString());

        // initialisation des resultats efforts
        double[][] result = new double[2][this.n];

        for (int i = 0; i < this.n; i++) {
            result[1][i] = 0;
        }

        int nbLoads = loads.getValNumber();

        for (int i = 0; i < nbLoads; i++) {
            int row = loads.getRankValue(i);
            double value = loads.getValueAtRank(i);

            result[1][row - 1] = value;
        }

        // clonage matrice sur lignes deplacements imposes
        SkylineSquareHalfMatrix matDisp = new SkylineSquareHalfMatrix(this.n, SkylineSquareHalfMatrix.UPPER);

        int nbDisp = displacements.getValNumber();

        for (int i = 0; i < nbDisp; i++) {

            int rank = displacements.getRankValue(i);
            int colMax = this.matUp.getEndColumn(rank);

            for (int j = rank; j <= colMax; j++) {
                matDisp.setVal(rank, j, this.matUp.getVal(rank, j));
            }

        }

        // modification du vecteur effort
        for (int row = 1; row <= this.n; row++) {

            for (int i = 0; i < nbDisp; i++) {

                int rank = displacements.getRankValue(i);
                double value = displacements.getValueAtRank(i);

                int colMax = this.matUp.getEndColumn(rank);
                double factor;

                if (rank > colMax) {
                    factor = 0;
                } else if (rank > row) {
                    factor = this.matUp.getVal(row, rank);
                } else {
                    factor = this.matUp.getVal(rank, row);
                }

                loads.addVal(row, -factor * value);
            }
        }

        // suppression des lignes
        for (int i = nbDisp; i > 0; i--) {
            int rank = displacements.getRankValue(i - 1);
            loads.deleteRow(rank);

            this.matUp.removeRowAndColumn(rank);

        }

        System.out.println("matrice à resoudre:");
        System.out.println(this.matUp.toString());

        System.out.println("vecteur effort");
        loads.sort();
        System.out.println(loads.toString());

        // decomposition LU
        if (this.state == RAW) {
            this.LUDecomposition();
        }

        double[] interm = new double[n];

        System.out.println("resolution Lx=F");

        // resolution matLow.interm=loads
        for (int i = 1; i <= this.matUp.getSize(); i++) {
            double calc = loads.getVal(i);

            int startColumn = matLow.getStartColumn(i);

            for (int j = startColumn; j < i; j++) {

                double factor = this.matLow.getVal(i, j);

                calc = calc - factor * interm[j - 1];

            }

            interm[i - 1] = calc;
        }

        System.out.println("solution intermediaire:");
        for (int i = 0; i < this.matUp.getSize(); i++) {
            System.out.println(interm[i]);
        }
        System.out.println("");

        // resolution matUp.disp=interm
        // resolution
        for (int row = this.matUp.getSize(); row > 0; row--) {

            {

                double calc = interm[row - 1];

                for (int i = row + 1; i <= this.matUp.getSize(); i++) {
                    calc = calc - this.matUp.getVal(row, i) * result[0][i - 1];
                }

                result[0][row - 1] = calc / this.matUp.getVal(row, row);
            }
        }

        System.out.println("deplacements solution");
        for (int i = 0; i < this.matUp.getSize(); i++) {
            System.out.println(result[0][i]);
        }

        // insertion des deplacements imposes    
        for (int i = 0; i < nbDisp; i++) {

            int rowDisp = displacements.getRankValue(i);
            double dispImposed = displacements.getValueAtRank(i);

            System.out.println("init disp imposed row " + rowDisp + " at " + dispImposed);
            for (int j = n - 1; j >= rowDisp; j--) {
                result[0][j] = result[0][j - 1];
            }
            result[0][rowDisp - 1] = dispImposed;
        }

        // calcul des forces
        // mise a jour uniquement avec deplacements imposes
        System.out.println("effort avant calcul");
        for (int i = 0; i < this.n; i++) {
            System.out.println(result[1][i]);
        }

        for (int i = 0; i < nbDisp; i++) {

            int rank = displacements.getRankValue(i);
            double value = result[0][rank-1];

            int colMax = matDisp.getEndColumn(rank);
            double factor=0;

            for (int j = 1; j <= rank; j++) {
                factor=factor+value*matDisp.getVal(j, rank);
            }
            for (int j = rank+1; j <= Math.min(rank,colMax); j++) {
                factor=factor+value*matDisp.getVal(rank,j);
            }
            

            result[1][rank - 1] = factor;
        }

        return result;
    }

    public static void main(String[] args) {

        int n = 4;
        int nbVal = 50;

        ProblemMatrix pbMat = new ProblemMatrix(n);
        pbMat.setVal(1, 1, 4);
        pbMat.setVal(1, 2, 1);
        pbMat.setVal(1, 3, 8);
        pbMat.setVal(1, 4, 2);

        pbMat.setVal(2, 2, 9);
        pbMat.setVal(2, 3, 2);
        pbMat.setVal(2, 4, 0);

        pbMat.setVal(3, 3, 6);
        pbMat.setVal(3, 4, 1);

        pbMat.setVal(4, 4, 6);

        System.out.println("pbMat:");
        System.out.println(pbMat.toString());

        Vector force = new Vector();
        force.setVal(2, 100);
        force.setVal(4, 100);
        //force.setVal(6, 100);

        Vector disp = new Vector();
        disp.setVal(1, 1);
        //disp.setVal(7, 1);

        System.out.println("force:");
        System.out.println(force.toString());

        System.out.println("disp:");
        System.out.println(disp.toString());

        double[][] result = pbMat.solve(disp, force);

        System.out.println("resultat:");
        for (int i = 0; i < n; i++) {
            System.out.println(result[0][i] + ", " + result[1][i]);
        }

    }

}
