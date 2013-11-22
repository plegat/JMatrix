/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcm;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class rcmSolver {

    private final rcmQueue nodes;
    private final rcmQueue result;
    private final rcmQueue temp, temp2;

    public rcmSolver(rcmQueue nodes) {
        this.nodes = nodes;
        this.result = new rcmQueue();
        this.temp = new rcmQueue();
        this.temp2 = new rcmQueue();
    }

    public int[] solve() {

        this.nodes.sort();
        this.result.init();

        while (this.nodes.getNbElements() > 0) {

            this.temp.init();

            rcmNode node = this.nodes.getFirst();

            if (!this.result.isInQueue(node)) {

                this.result.add(node);

                this.temp.addNotIn(node.getAdjacents(), this.result);
                this.temp.sort();

                while (this.temp.getNbElements() > 0) {

                    this.temp2.init();
                    rcmNode node2 = this.temp.getFirst();

                    if (!this.result.isInQueue(node2)) {
                        this.result.add(node2);

                        this.temp2.addNotIn(node2.getAdjacents(), this.result);
                        this.temp2.sort();

                        this.temp.add(this.temp2.getAll());
                    }
                }

            }
        }

        int nbElements = this.result.getNbElements();
        int[] ranks = new int[nbElements];

        for (int i = 0; i < nbElements; i++) {
            ranks[nbElements - i - 1] = this.result.getFirst().getId();
        }

        return ranks;
    }

    public static void main(String[] args) {

        rcmNode[] node = new rcmNode[11];

        for (int i = 1; i < 11; i++) {
            node[i] = new rcmNode(i);
        }

        node[1].addAdjacent(node[2]);
        node[1].addAdjacent(node[4]);

        node[2].addAdjacent(node[1]);
        node[2].addAdjacent(node[3]);
        node[2].addAdjacent(node[5]);
        node[2].addAdjacent(node[6]);

        node[3].addAdjacent(node[2]);
        node[3].addAdjacent(node[6]);

        node[4].addAdjacent(node[1]);
        node[4].addAdjacent(node[5]);
        node[4].addAdjacent(node[8]);

        node[5].addAdjacent(node[2]);
        node[5].addAdjacent(node[4]);
        node[5].addAdjacent(node[6]);
        node[5].addAdjacent(node[7]);

        node[6].addAdjacent(node[2]);
        node[6].addAdjacent(node[3]);
        node[6].addAdjacent(node[5]);
        node[6].addAdjacent(node[9]);

        node[7].addAdjacent(node[5]);
        node[7].addAdjacent(node[9]);
        node[7].addAdjacent(node[10]);

        node[8].addAdjacent(node[4]);

        node[9].addAdjacent(node[6]);
        node[9].addAdjacent(node[7]);
        node[9].addAdjacent(node[10]);

        node[10].addAdjacent(node[7]);
        node[10].addAdjacent(node[9]);

        System.out.println("degrés:");
        for (int i = 1; i < 11; i++) {
            System.out.println("noeud " + i + ": " + node[i].getDegree());
        }

        rcmQueue nodes = new rcmQueue();
        nodes.add(node);

        rcmSolver solver = new rcmSolver(nodes);
        int[] result = solver.solve();

        for (int i = 0; i < result.length; i++) {

            System.out.println("rang " + (i + 1) + ": " + result[i]);
        }

    }

}
