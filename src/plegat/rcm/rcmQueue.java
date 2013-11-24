/*
 * Cette œuvre est mise à disposition sous licence Attribution - Pas d’Utilisation Commerciale - Partage dans les Mêmes Conditions 3.0 non transposé. 
 * Pour voir une copie de cette licence, visitez http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * ou écrivez à Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA. 
 * 
 * This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * or send a letter to Creative Commons, 444 Castro Street, Suite 900, Mountain View, California, 94041, USA.   
 */

package plegat.rcm;

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

    public void addNotIn(rcmNode node, rcmQueue queue) {
        if ((node != null) && (!queue.isInQueue(node)) && (this.queue.indexOf(node) < 0)) {
            this.queue.add(node);
        }
    }

    public void add(rcmNode[] nodes) {
        if (nodes != null) {
            for (rcmNode node : nodes) {
                if (node != null) {
                    this.add(node);
                }
            }
        }
    }

    public void addNotIn(rcmNode[] nodes, rcmQueue queue) {
        for (rcmNode node : nodes) {
            if ((node != null) && (!queue.isInQueue(node))) {
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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        if (this.getNbElements() > 0) {
            for (int i = 0; i < this.queue.size(); i++) {
                sb.append(this.queue.get(i).getId());
                if (i < this.queue.size() - 1) {
                    sb.append("-");
                }
            }
        } else {
            sb.append("empty");
        }

        return sb.toString();

    }

}
