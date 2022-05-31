

public class DelFileAnsMsg extends Message {

    public DelFileAnsMsg(Ninfo dest, Ninfo source, int ticket) {
        super(6, false, dest, source);
        this.id=ticket;
    }

}
