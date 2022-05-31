public class PingAnsMsg extends Message {

    public PingAnsMsg(Ninfo dest, Ninfo source, int ticket) {
        super(6, false, dest,source) ;
        this.id=ticket;
    }





}
