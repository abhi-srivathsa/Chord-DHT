//If the port is in use this exception is used and a random port is assigned
public class PortNumException extends Exception {
    private final int port;
    public PortNumException(int port){
        this.port = port;
    }
    public int getPort(){
        return this.port;
    }

}
