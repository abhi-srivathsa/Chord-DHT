

import java.util.Map;

public class LeavingPredReqMsg extends Message {
    private final Ninfo newPredecessor;
    private final Map<String, String> files;

    public LeavingPredReqMsg(Ninfo dest, Ninfo newPredecessor, Map<String, String> files, Ninfo source) {
        super(44, true, dest, source);
        this.newPredecessor = newPredecessor;
        this.files = files;
    }

    public Ninfo getNewPredecessor() {
        return newPredecessor;
    }

    public Map<String, String> getFiles() {
        return files;
    }


}
