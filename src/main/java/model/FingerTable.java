package chord.model;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

// The fingertable for a particular node is represented by this class
public class FingerTable {

    private final TreeMap<String, NodeInfo> tmap;

    private LinkedList<NodeInfo> fin;

    private String node;

    public FingerTable(String nId) {
        this.tmap = new TreeMap<>(new FingerTableComparator(nId));
        this.fin = new LinkedList<>();
        this.node = nId;
    }

    // addfinger is used to add nodes to the finger table
    public void addFinger(NodeInfo node) {
        if (fin.size() < Utilities.numberOfBit()) {
            fin.addLast(node);
        }
    }

    // modify th values of the finger in the finger table nifo represents the new
    // information to be aded or modified
    public synchronized void modifyFinger(int pos, NodeInfo nInfo) {
        this.fin.set(pos, nInfo);
    }

    // get the information of the fnger table
    public NodeInfo getFinger(int pos) {
        return this.fin.get(pos);
    }

    // Get the value of the node that is closest to the given node and is smaller
    // than the given node

    public synchronized NodeInfo closestPredecessor(String node) {
        this.tmap.clear();
        for (NodeInfo Info : this.fin) {
            this.tmap.put(Info.getHash(), Info);
        }
        Map.Entry<String, NodeInfo> pred = this.tmap.lowerEntry(node);
        return pred.getValue();
    }

    // print the table
    public void printTable() {
        int j = 0;
        System.out.println("finger table values");
        for (NodeInfo Info : this.fin) {
            System.out.println("finger " + j + ": " + Info.getHash());
            j++;
        }
    }

    // remove a finger
    public synchronized NodeInfo removeFinger(int pos) {
        return this.fin.remove(pos);
    }

    public boolean containsFinger(String k) {
        for (NodeInfo Info : this.fin) {
            if (Info.getHash().equals(k)) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<NodeInfo> getFingers() {
        return fin;
    }

    public String getNode() {
        return node;
    }

}
