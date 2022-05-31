import java.util.Comparator;


  // Setting the caller node as the smallest

public class NodeComparator implements Comparator<String> {

    //the hash which we consider as the "0" on the ring
    private String nodeidentifier;

    public NodeComparator(String nodeidentifier) {
        this.nodeidentifier = nodeidentifier;
    }

    @Override
    public int compare(String hash1, String hash2){
        if (hash1.compareTo(nodeidentifier)>=0 && hash2.compareTo(nodeidentifier) >=0){
            return hash1.compareTo(hash2);
        }
        if (hash1.compareTo(nodeidentifier)<0 && hash2.compareTo(nodeidentifier) >=0){
            return 1;
        }
        if (hash1.compareTo(nodeidentifier) >=0 && hash2.compareTo(nodeidentifier) <0){
            return -1;
        }
        if (hash1.compareTo(nodeidentifier)<0 && hash2.compareTo(nodeidentifier) <0){
            return hash1.compareTo(hash2);
        }
        return 0;
    }
}
