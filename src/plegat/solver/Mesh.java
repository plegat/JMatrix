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

import java.util.ArrayList;
import java.util.Hashtable;
import plegat.jmatrix.ProblemMatrix;
import plegat.jmatrix.Vector;
import plegat.rcm.rcmNode;
import plegat.rcm.rcmQueue;
import plegat.rcm.rcmSolver;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Mesh {

    private final ArrayList<Node> nodes;
    private final ArrayList<Element> elements;
    private final ArrayList<Property> properties;
    private final ArrayList<Material> materials;
    private final ArrayList<NodeGroup> nodeGroups;
    private final ArrayList<ElementGroup> elementGroups;
    private final ArrayList<NodalBCL> nodalBcl;
    private final Hashtable<Property, ElementGroup> tableElementProperties;
    private final ArrayList<Loadcase> loadcases;
    private int[] rcmOptim;
    private int[] rcmOptimInverse;

    private static final double __RZ_STIFF__ = 1e-6;

    public Mesh() {

        this.nodes = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.nodeGroups = new ArrayList<>();
        this.elementGroups = new ArrayList<>();
        this.nodalBcl = new ArrayList<>();
        this.tableElementProperties = new Hashtable();
        this.loadcases = new ArrayList<>();
        this.rcmOptim = null;
        this.rcmOptimInverse = null;
    }

    public void init() {

        this.nodes.clear();
        this.elements.clear();
        this.properties.clear();
        this.materials.clear();
        this.nodeGroups.clear();
        this.elementGroups.clear();
        this.nodalBcl.clear();
        this.tableElementProperties.clear();
        this.loadcases.clear();
        this.rcmOptim = null;
        this.rcmOptimInverse = null;
    }

    public void addNode(Node node) {
        if (this.nodes.indexOf(node) < 0) {
            this.nodes.add(node);
        }
    }

    public void addElement(Element elmt) {
        if (this.elements.indexOf(elmt) < 0) {
            this.elements.add(elmt);
        }
    }

    public void addProperty(Property prop) {
        if (this.properties.indexOf(prop) < 0) {
            this.properties.add(prop);
        }
    }

    public void addMaterial(Material mat) {
        if (this.materials.indexOf(mat) < 0) {
            this.materials.add(mat);
        }
    }

    public void addNodeGroup(NodeGroup ng) {
        if (this.nodeGroups.indexOf(ng) < 0) {
            this.nodeGroups.add(ng);
        }
    }

    public void addElementGroup(ElementGroup eg) {
        if (this.elementGroups.indexOf(eg) < 0) {
            this.elementGroups.add(eg);
        }
    }

    public void addNodalBCL(NodalBCL nbcl) {
        if (this.nodalBcl.indexOf(nbcl) < 0) {
            this.nodalBcl.add(nbcl);
        }
    }

    public void addElementGroupProperty(ElementGroup group, Property prop) {
        this.tableElementProperties.put(prop, group);
    }

    public void addLoadcase(Loadcase lc) {
        if (!this.loadcases.contains(lc)) {
            this.loadcases.add(lc);
        }
    }

    public ProblemMatrix getMatrix() {

        int matrixSize = this.nodes.size() * 3;

        ProblemMatrix pbmat = new ProblemMatrix(matrixSize);

        if (this.rcmOptim == null) {
            this.makeRCMOptimization();
        }

        int nbElements = this.elements.size();

        System.out.println("Nombre d'éléments à traiter: " + nbElements);

        for (int i = 0; i < nbElements; i++) {

            Element elm = this.elements.get(i);
            System.out.println("element " + elm.getId());

            double[][] matrix = elm.getElementMatrix();

            if (matrix != null) {
                int nbligne = matrix.length;
                int nbcol = matrix[0].length;

                for (int j = 0; j < nbligne; j++) {
                    for (int k = 0; k < nbcol; k++) {
                        System.out.print(matrix[j][k] + "  ");
                    }
                    System.out.println("");
                }

                if (elm.getType() == Element.ROD2) {

                    int rank1 = this.nodes.indexOf(elm.getNodes()[0]);
                    int rank2 = this.nodes.indexOf(elm.getNodes()[1]);

                    int matrixLine1 = this.rcmOptimInverse[rank1] * 3 + 1;
                    int matrixLine2 = this.rcmOptimInverse[rank2] * 3 + 1;

                    System.out.println("ecriture sur ligne " + matrixLine1 + " pour noeud " + elm.getNodes()[0].getId());
                    System.out.println("ecriture sur ligne " + matrixLine2 + " pour noeud " + elm.getNodes()[1].getId());

                    // ATTENTION: la symétrie est gérée automatiquement par pbmat!!!
                    pbmat.addVal(matrixLine1, matrixLine1, matrix[0][0]);
                    pbmat.addVal(matrixLine1, matrixLine1 + 1, matrix[0][1]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine1 + 1, matrix[1][1]);

                    pbmat.addVal(matrixLine2, matrixLine2, matrix[2][2]);
                    pbmat.addVal(matrixLine2, matrixLine2 + 1, matrix[2][3]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine2 + 1, matrix[3][3]);

                    pbmat.addVal(matrixLine1, matrixLine2, matrix[0][2]);
                    pbmat.addVal(matrixLine1, matrixLine2 + 1, matrix[0][3]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2, matrix[1][2]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2 + 1, matrix[1][3]);

                    //ajout des raideurs RZ
                    pbmat.addVal(matrixLine1 + 2, matrixLine1 + 2, __RZ_STIFF__);
                    pbmat.addVal(matrixLine2 + 2, matrixLine2 + 2, __RZ_STIFF__);

                } else if (elm.getType() == Element.BEAM2) {

                    int rank1 = this.nodes.indexOf(elm.getNodes()[0]);
                    int rank2 = this.nodes.indexOf(elm.getNodes()[1]);

                    int matrixLine1 = this.rcmOptimInverse[rank1] * 3 + 1;
                    int matrixLine2 = this.rcmOptimInverse[rank2] * 3 + 1;

                    System.out.println("ecriture sur ligne " + matrixLine1 + " pour noeud " + elm.getNodes()[0].getId());
                    System.out.println("ecriture sur ligne " + matrixLine2 + " pour noeud " + elm.getNodes()[1].getId());

                    // ATTENTION: la symétrie est gérée automatiquement par pbmat!!!
                    pbmat.addVal(matrixLine1, matrixLine1, matrix[0][0]);
                    pbmat.addVal(matrixLine1, matrixLine1 + 1, matrix[0][1]);
                    pbmat.addVal(matrixLine1, matrixLine1 + 2, matrix[0][2]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine1 + 1, matrix[1][1]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine1 + 2, matrix[1][2]);
                    pbmat.addVal(matrixLine1 + 2, matrixLine1 + 2, matrix[2][2]);

                    pbmat.addVal(matrixLine1, matrixLine2, matrix[0][3]);
                    pbmat.addVal(matrixLine1, matrixLine2 + 1, matrix[0][4]);
                    pbmat.addVal(matrixLine1, matrixLine2 + 2, matrix[0][5]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2, matrix[1][3]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2 + 1, matrix[1][4]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2 + 2, matrix[1][5]);
                    pbmat.addVal(matrixLine1 + 2, matrixLine2 , matrix[2][3]);
                    pbmat.addVal(matrixLine1 + 2, matrixLine2 + 1, matrix[2][4]);
                    pbmat.addVal(matrixLine1 + 2, matrixLine2 + 2, matrix[2][5]);

                    pbmat.addVal(matrixLine2, matrixLine2, matrix[3][3]);
                    pbmat.addVal(matrixLine2, matrixLine2 + 1, matrix[3][4]);
                    pbmat.addVal(matrixLine2, matrixLine2 + 2, matrix[3][5]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine2 + 1, matrix[4][4]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine2 + 2, matrix[4][5]);
                    pbmat.addVal(matrixLine2 + 2, matrixLine2 + 2, matrix[5][5]);

                } else if (elm.getType() == Element.TRIA3) {

                    int rank1 = this.nodes.indexOf(elm.getNodes()[0]);
                    int rank2 = this.nodes.indexOf(elm.getNodes()[1]);
                    int rank3 = this.nodes.indexOf(elm.getNodes()[2]);

                    int matrixLine1 = this.rcmOptimInverse[rank1] * 3 + 1;
                    int matrixLine2 = this.rcmOptimInverse[rank2] * 3 + 1;
                    int matrixLine3 = this.rcmOptimInverse[rank3] * 3 + 1;

                    System.out.println("ecriture sur ligne " + matrixLine1 + " pour noeud " + elm.getNodes()[0].getId());
                    System.out.println("ecriture sur ligne " + matrixLine2 + " pour noeud " + elm.getNodes()[1].getId());
                    System.out.println("ecriture sur ligne " + matrixLine3 + " pour noeud " + elm.getNodes()[2].getId());

                    // ATTENTION: la symétrie est gérée automatiquement par pbmat!!!
                    pbmat.addVal(matrixLine1, matrixLine1, matrix[0][0]);
                    pbmat.addVal(matrixLine1, matrixLine1 + 1, matrix[0][1]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine1 + 1, matrix[1][1]);

                    pbmat.addVal(matrixLine1, matrixLine2, matrix[0][2]);
                    pbmat.addVal(matrixLine1, matrixLine2 + 1, matrix[0][3]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2, matrix[1][2]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine2 + 1, matrix[1][3]);

                    pbmat.addVal(matrixLine1, matrixLine3, matrix[0][4]);
                    pbmat.addVal(matrixLine1, matrixLine3 + 1, matrix[0][5]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine3, matrix[1][4]);
                    pbmat.addVal(matrixLine1 + 1, matrixLine3 + 1, matrix[1][5]);

                    pbmat.addVal(matrixLine2, matrixLine2, matrix[2][2]);
                    pbmat.addVal(matrixLine2, matrixLine2 + 1, matrix[2][3]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine2 + 1, matrix[3][3]);

                    pbmat.addVal(matrixLine2, matrixLine3, matrix[2][4]);
                    pbmat.addVal(matrixLine2, matrixLine3 + 1, matrix[2][5]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine3, matrix[3][4]);
                    pbmat.addVal(matrixLine2 + 1, matrixLine3 + 1, matrix[3][5]);

                    pbmat.addVal(matrixLine3, matrixLine3, matrix[4][4]);
                    pbmat.addVal(matrixLine3, matrixLine3 + 1, matrix[4][5]);
                    pbmat.addVal(matrixLine3 + 1, matrixLine3 + 1, matrix[5][5]);

                    //ajout des raideurs RZ
                    pbmat.addVal(matrixLine1 + 2, matrixLine1 + 2, __RZ_STIFF__);
                    pbmat.addVal(matrixLine2 + 2, matrixLine2 + 2, __RZ_STIFF__);
                    pbmat.addVal(matrixLine3 + 2, matrixLine3 + 2, __RZ_STIFF__);

                } else if (elm.getType() == Element.QUAD4) {

                }
            }

        }

        return pbmat;

    }

    public Vector getDisp(Loadcase lc) {

        Vector disp = new Vector();

        if (this.rcmOptim == null) {
            this.makeRCMOptimization();
        }

        int nbBcl = this.nodalBcl.size();

        System.out.println("Nombre de BCL à traiter: " + nbBcl);

        for (int i = 0; i < nbBcl; i++) {

            NodalBCL bcl = this.nodalBcl.get(i);

            if (bcl.getType() == NodalBCL.NODAL_DISP) {

                ArrayList<Node> zone = bcl.getZone();

                double[] data = bcl.getData();

                for (Node node : zone) {

                    int rank = this.nodes.indexOf(node);
                    int vectorLine = this.rcmOptimInverse[rank] * 3 + 1;

                    for (int j = 0; j < 3; j++) {
                        if (!Double.isNaN(data[j])) {
                            disp.setVal(vectorLine + j, data[j]);
                        }
                    }
                }
            }
        }

        return disp;

    }

    public Vector getLoad(Loadcase lc) {

        Vector load = new Vector();

        if (this.rcmOptim == null) {
            this.makeRCMOptimization();
        }

        int nbBcl = this.nodalBcl.size();

        System.out.println("Nombre de BCL à traiter: " + nbBcl);

        for (int i = 0; i < nbBcl; i++) {

            NodalBCL bcl = this.nodalBcl.get(i);

            if (bcl.getType() == NodalBCL.NODAL_FORCE) {

                ArrayList<Node> zone = bcl.getZone();

                double[] data = bcl.getData();

                for (Node node : zone) {

                    int rank = this.nodes.indexOf(node);
                    int vectorLine = this.rcmOptimInverse[rank] * 3 + 1;

                    for (int j = 0; j < 3; j++) {
                        if (!Double.isNaN(data[j])) {
                            load.setVal(vectorLine + j, data[j]);
                        }
                    }
                }
            }
        }

        return load;

    }

    public Node getNode(int rank) {

        if (rank < this.nodes.size()) {
            return this.nodes.get(rank);
        } else {
            return null;
        }
    }

    public Node getNodeByID(String id) {

        int rank = 0;

        while (rank < this.nodes.size()) {

            Node node = this.nodes.get(rank);
            if (node.getId().equals(id)) {
                return node;
            } else {
                rank++;
            }
        }

        return null;
    }

    public Element getElement(int rank) {

        if (rank < this.elements.size()) {
            return this.elements.get(rank);
        } else {
            return null;
        }
    }

    public Element getElementByID(String id) {

        int rank = 0;

        while (rank < this.elements.size()) {

            Element elm = this.elements.get(rank);
            if (elm.getId().equals(id)) {
                return elm;
            } else {
                rank++;
            }
        }

        return null;
    }

    public Material getMaterialByName(String name) {

        int rank = 0;

        while (rank < this.materials.size()) {

            Material mat = this.materials.get(rank);
            if (mat.getName().equals(name)) {
                return mat;
            } else {
                rank++;
            }
        }

        return null;

    }

    public Property getPropertyByName(String name) {

        int rank = 0;

        while (rank < this.properties.size()) {

            Property prop = this.properties.get(rank);
            if (prop.getName().equals(name)) {
                return prop;
            } else {
                rank++;
            }
        }

        return null;

    }

    public NodeGroup getNodeGroupByName(String name) {

        int rank = 0;

        while (rank < this.nodeGroups.size()) {

            NodeGroup grp = this.nodeGroups.get(rank);
            if (grp.getId().equals(name)) {
                return grp;
            } else {
                rank++;
            }
        }

        return null;

    }

    public void listElementGroup() {

        for (int i = 0; i < this.elementGroups.size(); i++) {
            System.out.println("element groupe #" + (i + 1) + ": " + this.elementGroups.get(i).getId());
        }

    }

    public ElementGroup getElementGroupByName(String name) {

        int rank = 0;

        while (rank < this.elementGroups.size()) {

            ElementGroup grp = this.elementGroups.get(rank);
            if (grp.getId().equals(name)) {
                return grp;
            } else {
                rank++;
            }
        }

        return null;

    }

    public ElementGroup getElementGroupByProperty(Property prop) {

        return this.tableElementProperties.get(prop);

    }

    public NodalBCL getBCLByName(String name) {

        int rank = 0;

        while (rank < this.nodalBcl.size()) {

            NodalBCL bcl = this.nodalBcl.get(rank);
            if (bcl.getId().equals(name)) {
                return bcl;
            } else {
                rank++;
            }
        }

        return null;
    }

    public void makeRCMOptimization() {

        int nbNodes = this.nodes.size();

        rcmQueue nodeQueue = new rcmQueue();
        rcmNode[] tabrcmNode = new rcmNode[nbNodes];

        for (int i = 0; i < nbNodes; i++) {
            tabrcmNode[i] = new rcmNode(i);
            this.nodes.get(i).setRcmRank(i);
        }

        for (int i = 0; i < nbNodes; i++) {

            Node[] adjacents = this.nodes.get(i).getAdjacents();

            for (int j = 0; j < adjacents.length; j++) {

                tabrcmNode[i].addAdjacent(tabrcmNode[adjacents[j].getRcmRank()]);

            }

        }

        for (int i = 0; i < nbNodes; i++) {
            nodeQueue.add(tabrcmNode);
        }

        rcmSolver solver = new rcmSolver(nodeQueue);

        this.rcmOptim = solver.solve();

        this.rcmOptimInverse = new int[nbNodes]; // table permettant de savoir à quelle ligne de la matrice se trouve le noeud i

        for (int i = 0; i < nbNodes; i++) {
            this.rcmOptimInverse[this.rcmOptim[i]] = i;
        }

    }

    public int[] getRcmOptim() {
        return rcmOptim;
    }

    public int[] getRcmOptimInverse() {
        return rcmOptimInverse;
    }

    public void initNodeAdjacents() {

        for (Element elm : this.elements) {
            elm.initAdjacents();
        }

    }

    public void initElementProperty() {

        for (Property prop : this.properties) {

            ElementGroup elmGrp = this.getElementGroupByProperty(prop);

            if (elmGrp == null) {
                System.out.println("elmgroup null for property " + prop.getName() + " !!!");
            } else {

                System.out.println("element group: " + elmGrp.getId());

                ArrayList<Element> elmList = elmGrp.getElements();

                for (Element element : elmList) {

                    if (element == null) {
                        System.out.println("element null for property " + prop.getName() + " !!!");
                    } else {

                        element.setProperty(prop);
                    }
                }
            }
        }
    }

    public Loadcase getLoadcase(int rank) {

        if (rank < this.loadcases.size()) {
            return this.loadcases.get(rank);
        } else {
            return null;
        }

    }

    public ArrayList<Loadcase> getLoadcases() {
        return loadcases;
    }

}
