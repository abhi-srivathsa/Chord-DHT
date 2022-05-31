import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class SList {

    private final TreeMap<String, Ninfo> map;
    private LinkedList<Ninfo> successors;
    private String node;
    private boolean single;


    public SList(String nodeIdentifier){
        this.map = new TreeMap<>(new FingerTableComparator(nodeIdentifier));
        this.successors = new LinkedList<>();
        this.node = nodeIdentifier;
        this.single = true;
    }

    public void printTable(){
        int i=0;
        System.out.println("SUCCESSOR LIST ");
        System.out.println("size: " + this.successors.size());
        for (Ninfo ninfo : this.successors){
            System.out.println("finger " + i + " : " + ninfo.getHash() );
            i++;
        }
    }

    //get Smallest node
    public synchronized Ninfo closest(String node) throws SListException {
        this.map.clear();
        for (Ninfo ninfo : this.successors){
            this.map.put(ninfo.getHash(), ninfo);
        }
        Map.Entry<String, Ninfo> successor = this.map.ceilingEntry(node);
        if(successor == null){
            throw new SListException();
        }
        return successor.getValue();
    }


    public void addEntry( Ninfo node) {
        if (successors.size() < 4) {
            successors.addLast(node);
        }

    }

    // For tests
    public TreeMap<String, Ninfo> getMap() {
        return map;
    }

    public LinkedList<Ninfo> getSuccessors() {
        return successors;
    }

    public String getNode() {
        return node;
    }

    public boolean containsSuccessor(String key){
        for (Ninfo ninfo : this.successors) {
            if (ninfo.getHash().equals(key)) {
                return true;
            }
        }
        return false;
    }
     //Add new node at indicated position
    public synchronized void modifyEntry(int position, Ninfo newNinfo){
        if (position == 0 && newNinfo.getHash().equals(this.node)){
            setSingle(true);
        }
        if (position == 0 && !(newNinfo.getHash().equals(this.node))){
            setSingle(false);
        }
        this.successors.set(position, newNinfo);
    }


    public boolean isSingle() { return single; }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public Ninfo getElement(int position){
        return this.successors.get(position);
    }

    public Ninfo getLast(){
        return this.successors.getLast();
    }

    public Ninfo getFirst(){
        return successors.getFirst();
    }





}

