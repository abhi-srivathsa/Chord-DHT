public class DelFileReqMsg extends Message {

    private final String key;

    public DelFileReqMsg(Ninfo dest, String key, Ninfo source){
        super(17, true, dest,source);
        this.key=key;
    }

    public String getKey() {
        return key;
    }
}
