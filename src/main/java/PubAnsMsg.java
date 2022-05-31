

public class PubAnsMsg extends Message {

    public PubAnsMsg(Ninfo dest, Ninfo source, int ticket){
        super(6,false,dest,source);
        this.id=ticket;
    }
}
