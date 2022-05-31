import java.util.HashMap;
import java.util.Map;

// The information of the files in their correponding nodes is in this class

public class FileSystem {
    private String nid;
    // The generated keys for the files are stores as keys of the hashmaps.
    // The json strings are the value part of the hashmaps

    HashMap<String, String> fls;

    public FileSystem(String nid) {
        this.nid = nid;
        this.fls = new HashMap<>();
    }

    //publish inserts a file




    // If the node is terminated, then all the keys stored in the node need to transmitted to the neighbour
    public Map<String, String> freeFileSystem() {
        Map<String, String> fls = new HashMap();
        for (String k : this.fls.keySet()) {
            String val = this.fls.remove(k);
            fls.put(k, val);
        }
        this.fls.clear();
        return fls;
    }

    // all the keys that have values less than or equal to the current node are returned in this function.
    public Map<String, String> retrieveFiles(String newNid) {
        FingerTableComparator fTC = new FingerTableComparator(this.nid);
        Map<String, String> fls = new HashMap();
        for (String k : this.fls.keySet()) {
            if (fTC.compare(k, newNid) < 0) {
                String val = this.fls.remove(k);
                fls.put(k, val);
            }
        }
        return fls;
    }
    public void publish(String k, String f) {
        fls.put(k, f);
    }

    //getfile is used to retrieve a file
    public String getFile(String k) {
        return fls.get(k);
    }


    // deleteFile Deletes a file
    public void deleteFile(String k) {
        if (fls.size() == 0) {
            return;
        } else {
            fls.remove(k);
        }

    }

    // print
    public void print() {
        System.out.println("FILES: ");
        if (fls.size() == 0) {
            System.out.println("Empty");
        }
        for (Map.Entry<String, String> fi : this.fls.entrySet()) {
            System.out.println("Key : " + fi.getKey() + " File : " + fi.getValue());
        }
    }

}
