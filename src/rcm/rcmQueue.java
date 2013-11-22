/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rcm;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Jean-Michel BORLOT
 */
public class rcmQueue {

    private final ArrayList<rcmNode> queue;

    public rcmQueue() {

        this.queue = new ArrayList<>();

    }

    public void init() {
        this.queue.clear();
    }

    public int getNbElements() {
        return this.queue.size();
    }

    public void add(rcmNode node) {
        if ((node != null) && (this.queue.indexOf(node) < 0)) {
            this.queue.add(node);
        }
    }

    public void add(rcmNode[] nodes) {
        for (rcmNode node : nodes) {
            if (node != null) {
                this.add(node);
            }
        }
    }

    public void remove(rcmNode node) {
        if ((node != null) && (this.queue.indexOf(node) > -1)) {
            this.queue.remove(node);
        }
    }

    public void sort() {
        Collections.sort(queue);
    }

    public rcmNode getFirst() {

        if (this.getNbElements() > 0) {
            rcmNode node = this.queue.get(0);
            this.remove(node);
            return node;
        } else {
            return null;
        }

    }

    public rcmNode[] getAll() {
        if (this.getNbElements() > 0) {
            return this.queue.toArray(new rcmNode[this.queue.size()]);
        } else {
            return null;
        }
    }

    public boolean isInQueue(rcmNode node) {
        return (this.queue.indexOf(node) > -1);
    }

}
