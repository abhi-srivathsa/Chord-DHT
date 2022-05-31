

public class LeavingSucAnsMsg extends Message {
    public LeavingSucAnsMsg(Ninfo dest, Ninfo source, int ticket) {
        super(6, false, dest, source);
        this.id=ticket;
    }
}
