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

                this.temp.add(node.getAdjacents());
                this.temp.sort();

                while (this.temp.getNbElements() > 0) {

                    this.temp2.init();

                    rcmNode node2 = this.temp.getFirst();
                    
                    if (!this.result.isInQueue(node)) {
                        this.result.add(node2);

                        this.temp2.add(node2.getAdjacents());
                        this.temp2.sort();

                        this.temp.add(this.temp2.getAll());
                    }

                }
            }
        }

        int nbElements=this.result.getNbElements();
        int[] ranks = new int[nbElements];
        
        for (int i = 0; i < nbElements; i++) {
            
            ranks[i]=this.result.getFirst().getId();
        }
        
        return ranks;

    }

}
