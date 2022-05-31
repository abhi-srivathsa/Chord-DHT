

public class StartReqMsg extends Message {

    public StartReqMsg(Ninfo dest, Ninfo source) {

        super(33, true, dest,source);
    }
}
