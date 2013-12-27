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

/**
 *
 * @author Jean-Michel BORLOT
 */
public class Mesh {

    private ArrayList<Node> nodes;
    private ArrayList<Element> elements;
    private ArrayList<Property> properties;
    private ArrayList<Material> materials;
    private ArrayList<NodeGroup> nodeGroups;
    private ArrayList<ElementGroup> elementGroups;
    private ArrayList<NodalBCL> nodalBcl;
    private Hashtable<Property, ElementGroup> tableElementProperties;
    private ArrayList<Loadcase> loadcases;

    public Mesh() {

        this.nodes = new ArrayList<>();
        this.elements = new ArrayList<>();
        this.properties = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.nodeGroups = new ArrayList<>();
        this.elementGroups = new ArrayList<>();
        this.nodalBcl = new ArrayList<>();
        this.tableElementProperties = new Hashtable();
        this.loadcases=new ArrayList<>();
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

        return pbmat;

    }

    public Vector getDisp() {

        Vector disp = new Vector();

        return disp;

    }

    public Vector getLoad() {

        Vector load = new Vector();

        return load;

    }

    public Node getNode(int rank) {

        if (rank < this.nodes.size()) {
            return this.nodes.get(rank);
        } else {
            return null;
        }
    }

    public Node getNodeByID(int id) {

        int rank = 0;

        while (rank < this.nodes.size()) {

            Node node = this.nodes.get(rank);
            if (node.getId() == id) {
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

    public Element getElementByID(int id) {

        int rank = 0;

        while (rank < this.elements.size()) {

            Element elm = this.elements.get(rank);
            if (elm.getId() == id) {
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
            System.out.println("element groupe #"+(i+1)+": "+this.elementGroups.get(i).getId());
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
    

    public int[] getRCMOptimization() {

        int[] rcmResult = new int[this.nodes.size()];

        return rcmResult;

    }

}
