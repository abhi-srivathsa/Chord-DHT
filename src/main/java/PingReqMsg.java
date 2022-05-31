

public class PingReqMsg extends Message {

    public PingReqMsg(Ninfo dest, Ninfo source) {

        super(1, true, dest,source);
    }


}
