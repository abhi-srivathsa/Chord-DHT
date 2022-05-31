import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

// The fingertable for a particular node is represented by this class
public class FingerTable {

    private final TreeMap<String, Ninfo> tmap;

    private LinkedList<Ninfo> fin;

    private String node;

    public FingerTable(String nId) {
        this.tmap = new TreeMap<>(new FingerTableComparator(nId));
        this.fin = new LinkedList<>();
        this.node = nId;
    }

    // add nodes to the finger table
    public void addFinger(Ninfo node) {
        if (fin.size() < Helper.numberOfBit()) {
            fin.addLast(node);
        }
    }

    // modify the values of the finger in the finger table ninfo represents the new
    // information to be added or modified
    public synchronized void modifyFinger(int pos, Ninfo nInfo) {
        this.fin.set(pos, nInfo);
    }

    // get the information of the finger table
    public Ninfo getFinger(int pos) {
        return this.fin.get(pos);
    }

    // Get the value of the node that is closest to the given node and is smaller
    // than the given node

    public synchronized Ninfo closestPredecessor(String node) {
        this.tmap.clear();
        for (Ninfo Info : this.fin) {
            this.tmap.put(Info.getHash(), Info);
        }
        Map.Entry<String, Ninfo> pred = this.tmap.lowerEntry(node);
        return pred.getValue();
    }

    // print the table
    public void printTable() {
        int j = 0;
        System.out.println("finger table values");
        for (Ninfo Info : this.fin) {
            System.out.println("finger " + j + ": " + Info.getHash());
            j++;
        }
    }

    // remove a finger
    public synchronized Ninfo removeFinger(int pos) {
        return this.fin.remove(pos);
    }

    public boolean containsFinger(String k) {
        for (Ninfo Info : this.fin) {
            if (Info.getHash().equals(k)) {
                return true;
            }
        }
        return false;
    }

    public LinkedList<Ninfo> getFingers() {
        return fin;
    }

    public String getNode() {
        return node;
    }

}
