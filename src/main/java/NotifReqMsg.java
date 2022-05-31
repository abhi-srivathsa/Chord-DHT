

public class NotifReqMsg extends Message {
    public NotifReqMsg(Ninfo dest, Ninfo source) {
        super(4 , true, dest, source);
    }
}
