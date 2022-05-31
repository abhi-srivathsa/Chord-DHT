

public class LeavingSucReqMsg extends Message {
    private final Ninfo newSuccessor;
    public LeavingSucReqMsg(Ninfo dest, Ninfo newSuccessor, Ninfo source) {
        super(45, true, dest, source);
        this.newSuccessor = newSuccessor;
    }
    public Ninfo getNewSuccessor() {
        return newSuccessor;
    }

}
