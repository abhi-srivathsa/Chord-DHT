

public class PubReqMsg extends Message {

    private final String data;
    private final String key;

    public PubReqMsg(Ninfo dest, String data, String key, Ninfo source){
        super(85,true,dest,source);
        this.data=data;
        this.key=key;
    }

    public String getData(){
        return this.data;
    }

    public String getKey() { return key;}
}
