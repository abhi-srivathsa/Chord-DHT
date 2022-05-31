

public class SucReqMsg extends Message {


    //I'm asking for the successor of node identifier
    private final String   nodeidentifier;
    public SucReqMsg(Ninfo dest, String nodeidentifier, Ninfo source) {
        super(3, true, dest, source);
        this.nodeidentifier = nodeidentifier;
    }

    public String getNodeidentifier() {
        return nodeidentifier;
    }
}
