import java.io.Serializable;

/**
 * Class which contains the information about a node : IP address, port number and hashed key
 */
public class Ninfo implements Serializable{
    private String Ip;
    private int port;
    private String hash;

    // For testing
    public Ninfo(String hash){
        this.hash = hash;
        this.Ip = "-.-.-.-";
        this.port = -1;
    }
    public Ninfo(String Ip, int port){
        this.Ip = Ip;
        this.port = port;
        String key = Ip.concat(Integer.toString(port));
        this.hash = Helper.calculateHash(key);
    }

    public String getIp() {
        return Ip;
    }

    public int getPort() {
        return port;
    }
    public String getHash() {
        return hash;
    }

    public void setPort(int port) {
        this.port = port;
        String key = Ip.concat(Integer.toString(port));
        this.hash = Helper.calculateHash(key);
    }

    @Override
    public boolean equals(Object o){
        Ninfo ninfo = (Ninfo) o;
        return this.hash.equals(ninfo.hash);
    }



}
