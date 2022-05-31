

public class StartAnsMsg extends Message {
    public StartAnsMsg(Ninfo dest, Ninfo source, int ticket) {
        super(6, false, dest,source) ;
        this.id=ticket;
    }
}
