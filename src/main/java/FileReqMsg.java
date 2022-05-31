
public class FileReqMsg extends Message {

    private final String key;

    public FileReqMsg(Ninfo dest, String key, Ninfo source){
        super(25, true, dest,source);
        this.key=key;
    }

    public String getKey() {
        return key;
    }
}
