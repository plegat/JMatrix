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

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Element {

    private String id;
    private Node[] nodes;
    private int type;
    private int model=0;    //0: contraintes planes, 1: déformations planes
    public static int ROD2 = 1;
    public static int BEAM2 = 2;
    public static int TRIA3 = 3;
    public static int QUAD4 = 4;

    private Property prop;

    public Element(String id, Node[] nodes, int type) {
        this.id = id;
        this.nodes = nodes;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Property getProperty() {
        return prop;
    }

    public void setProperty(Property prop) {
        this.prop = prop;
    }

    public void initAdjacents() {

        if ((this.type == ROD2) || (this.type == BEAM2)) {
            this.nodes[0].addAdjacent(this.nodes[1]);
            this.nodes[1].addAdjacent(this.nodes[0]);
        } else if (this.type == TRIA3) {
            this.nodes[0].addAdjacent(this.nodes[1]);
            this.nodes[0].addAdjacent(this.nodes[2]);

            this.nodes[1].addAdjacent(this.nodes[0]);
            this.nodes[1].addAdjacent(this.nodes[2]);

            this.nodes[2].addAdjacent(this.nodes[0]);
            this.nodes[2].addAdjacent(this.nodes[1]);
        } else if (this.type == QUAD4) {
            this.nodes[0].addAdjacent(this.nodes[1]);
            this.nodes[0].addAdjacent(this.nodes[3]);

            this.nodes[1].addAdjacent(this.nodes[0]);
            this.nodes[1].addAdjacent(this.nodes[2]);

            this.nodes[2].addAdjacent(this.nodes[1]);
            this.nodes[2].addAdjacent(this.nodes[3]);

            this.nodes[3].addAdjacent(this.nodes[0]);
            this.nodes[3].addAdjacent(this.nodes[2]);

        }
    }

    public double[][] getElementMatrix() {

        double[][] matrix = null;

        if ((this.type == ROD2) || (this.type == BEAM2)) {

            double l = this.nodes[0].getDistanceTo(this.nodes[1]); // longueur de la barre
            double s = this.getProperty().getData()[0];           // section de la barre
            Material mat = this.getProperty().getMat();             // materiau de la barre
            double e = mat.getModulus();                          // module de young de la barre
            double nu = mat.getNu();                                // coeff de Poisson de la barre

            
            double angle = Math.atan2(this.nodes[1].getY() - this.nodes[0].getY(), this.nodes[1].getX() - this.nodes[0].getX());
            double cost = Math.cos(angle);
            double cos2t = cost * cost;
            double sint = Math.sin(angle);
            double sin2t = sint * sint;
            double cosint = cost * sint;

            double es_l = e * s / l;

            if (this.type == ROD2) {

                matrix = new double[4][4];

                matrix[0][0] = cos2t * es_l;
                matrix[0][1] = cosint * es_l;
                matrix[0][2] = -cos2t * es_l;
                matrix[0][3] = -cosint * es_l;

                matrix[1][0] = cosint * es_l;
                matrix[1][1] = sin2t * es_l;
                matrix[1][2] = -cosint * es_l;
                matrix[1][3] = -sin2t * es_l;

                matrix[2][0] = -cos2t * es_l;
                matrix[2][1] = -cosint * es_l;
                matrix[2][2] = cos2t * es_l;
                matrix[2][3] = cosint * es_l;

                matrix[3][0] = -cosint * es_l;
                matrix[3][1] = -sin2t * es_l;
                matrix[3][2] = cosint * es_l;
                matrix[3][3] = sin2t * es_l;

            } else if (this.type == BEAM2) {

                double i = this.getProperty().getData()[1];
                double ei_l3 = 12 * e * i / Math.pow(l, 3);

                matrix[0][0] = es_l * cos2t + ei_l3 * sin2t;
                matrix[0][1] = (es_l - ei_l3) * cosint;
                matrix[0][2] = -6 * e * i / Math.pow(l, 2) * sint;
                matrix[0][3] = -matrix[0][0];
                matrix[0][4] = -matrix[0][1];
                matrix[0][5] = matrix[0][2];

                matrix[1][0] = matrix[0][1];
                matrix[1][1] = es_l * sin2t + ei_l3 * cos2t;
                matrix[1][2] = 6 * e * i / Math.pow(l, 2) * cost;
                matrix[1][3] = -matrix[1][0];
                matrix[1][4] = -matrix[1][1];
                matrix[1][5] = matrix[1][2];

                matrix[2][0] = matrix[0][2];
                matrix[2][1] = matrix[1][2];
                matrix[2][2] = 4 * e * i / l;
                matrix[2][3] = -matrix[2][0];
                matrix[2][4] = -matrix[2][1];
                matrix[2][5] = 0.5 * matrix[2][2];

                matrix[3][0] = matrix[0][3];
                matrix[3][1] = matrix[1][3];
                matrix[3][2] = matrix[2][3];
                matrix[3][3] = -matrix[3][0];
                matrix[3][4] = -matrix[3][1];
                matrix[3][5] = matrix[3][2];

                matrix[4][0] = matrix[0][4];
                matrix[4][1] = matrix[1][4];
                matrix[4][2] = matrix[2][4];
                matrix[4][3] = matrix[3][4];
                matrix[4][4] = -matrix[4][1];
                matrix[4][5] = matrix[4][2];

                matrix[5][0] = matrix[0][5];
                matrix[5][1] = matrix[1][5];
                matrix[5][2] = matrix[2][5];
                matrix[5][3] = matrix[3][5];
                matrix[5][4] = matrix[4][5];
                matrix[5][5] = 4 * e * i / l;

            }

        } else if (this.type == TRIA3) {

            matrix = new double[6][6];

            Matrix stiff = new Matrix(6,6);

            Material mat = this.getProperty().getMat();             // materiau de la barre
            double e = mat.getModulus();                          // module de young de la barre
            double nu = mat.getNu();                                // coeff de Poisson de la barre

            double g = e / (2 * (1 + nu));
            double h1 = 2 * g * (1 - this.model*nu) / (1 - (1 + this.model) * nu);
            double h2 = nu * h1 / (1 - this.model * nu);

            double ep = this.getProperty().getData()[0];
            double a=0.5 * ((this.nodes[1].getX() - this.nodes[0].getX()) * (this.nodes[2].getY() - this.nodes[0].getY()) 
                    - (this.nodes[2].getX() - this.nodes[0].getX()) * (this.nodes[1].getY() - this.nodes[0].getY()));
            
            
            double coef = ep / (4 * a);

            double x13 = this.nodes[0].getX() - this.nodes[2].getX();
            double x21 = this.nodes[1].getX() - this.nodes[0].getX();
            double x32 = this.nodes[2].getX() - this.nodes[1].getX();

            double y12 = this.nodes[0].getY() - this.nodes[1].getY();
            double y23 = this.nodes[1].getY() - this.nodes[2].getY();
            double y31 = this.nodes[2].getY() - this.nodes[0].getY();

            stiff.setVal(0, 0, h1 * y23 * y23 + g * x32 * x32);
            stiff.setVal(0, 1, h2 * x32 * y23 + g * x32 * y23);
            stiff.setVal(0, 2, h1 * y31 * y23 + g * x32 * x13);
            stiff.setVal(0, 3, h2 * x13 * y23 + g * x32 * y31);
            stiff.setVal(0, 4, h1 * y12 * y23 + g * x21 * x32);
            stiff.setVal(0, 5, h2 * x21 * y23 + g * x32 * y12);

            stiff.setVal(1, 1, h1 * x32 * x32 + g * y23 * y23);
            stiff.setVal(1, 2, h2 * x32 * y31 + g * x13 * y23);
            stiff.setVal(1, 3, h1 * x32 * x13 + g * y31 * y23);
            stiff.setVal(1, 4, h2 * x32 * y12 + g * x21 * y23);
            stiff.setVal(1, 5, h1 * x32 * x21 + g * y12 * y23);

            stiff.setVal(2, 2, h1 * y31 * y31 + g * x13 * x13);
            stiff.setVal(2, 3, h2 * x13 * y31 + g * x13 * y31);
            stiff.setVal(2, 4, h1 * y12 * y31 + g * x13 * x21);
            stiff.setVal(2, 5, h2 * x21 * y31 + g * x13 * y12);

            stiff.setVal(3, 3, h1 * x13 * x13 + g * y31 * y31);
            stiff.setVal(3, 4, h2 * x13 * y12 + g * x21 * y31);
            stiff.setVal(3, 5, h1 * x13 * x21 + g * y12 * y31);

            stiff.setVal(4, 4, h1 * y12 * y12 + g * x21 * x21);
            stiff.setVal(4, 5, h2 * x21 * y12 + g * x21 * y12);

            stiff.setVal(5, 5, h1 * x21 * x21 + g * y12 * y12);

            stiff.symetrize();
            stiff.mult(coef);
            
            return stiff.getVals();
            

        } else if (this.type == QUAD4) {

            matrix = new double[8][8];

        }

        return matrix;

    }

}
