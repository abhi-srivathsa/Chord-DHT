import java.io.Serializable;


public class  Message implements Serializable{
    protected int id;
    protected TimeEndedException exception;
    private final Ninfo source;
    private final Ninfo dest;
    private final boolean ack;
    private final int type;

    public Message(int type, boolean ack, Ninfo dest, Ninfo source){
        this.type=type;
        this.ack=ack;
        this.dest = dest;
        this.source = source;

    }

    public int getType() {
        return type;
    }

    public boolean isAck() {
        return ack;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ninfo getDest() {
        return dest;
    }

    public Ninfo getSource(){return source;}

    //to check if the current message has an exception
    public void check() throws TimeEndedException {
        if (this.exception != null){
            throw  this.exception;
        }
    }

    public void setException(TimeEndedException exception) {
        this.exception = exception;
    }
    public TimeEndedException getException() {
        return exception;
    }



}
