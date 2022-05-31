public class LeavePredAnsMsg extends Message {
    public LeavePredAnsMsg(Ninfo dest, Ninfo source, int ticket) {
        super(6, false, dest, source);
        this.id = ticket;
    }
}
