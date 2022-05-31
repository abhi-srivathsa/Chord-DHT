

public class FirstSucAnsMsg extends Message {
    private final Ninfo successor;

    public FirstSucAnsMsg(Ninfo dest, Ninfo successor, Ninfo source, int ticket ) {
        super(6, false, dest,source);
        this.successor = successor;
        this.id=ticket;
    }

    public Ninfo getSuccessor() {
        return successor;
    }
}
