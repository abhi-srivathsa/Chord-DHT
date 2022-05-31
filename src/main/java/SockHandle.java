import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



public class SockHandle implements Runnable{

    public int portNum;
    Ninfo end = null;
    private Socket sock;
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private boolean delete = false;
    private boolean freshlyUsed;

    public SockHandle(int portNum, Socket sock) throws IOException{
        this.portNum = portNum;
        this.sock = sock;
        this.out = new ObjectOutputStream(sock.getOutputStream());
        this.in = new ObjectInputStream(sock.getInputStream());
        this.freshlyUsed = true;
    }

    public void terminate(){
        this.delete = true;
    }
    public Ninfo getEnd() throws Exception {
        if (end == null){
            throw new Exception();
        }
        return end;
    }

    public int getPortNum() {
        return portNum;
    }

    public boolean isFreshlyUsed() {
        return freshlyUsed;
    }

    public void setFreshlyUsed(boolean freshlyUsed) {
        this.freshlyUsed = freshlyUsed;
    }


    //  write the message received on the output stream

    public void sendMessage(Message message)throws IOException{
        if (this.end == null) {
            this.end = message.getDest();
        }
        out.writeObject(message);
        out.flush();
        setFreshlyUsed(true);
    }

    // forward the message to Chord Class

    @Override
    public void run() {
        while(!delete){
            try {
                //read the message from the buffer
                Message message= (Message) in.readObject();
                setFreshlyUsed(true);
                //set the endpoint if not done yet
                if (this.end == null){
                    this.end = message.getSource();
                }
                //deliver the message to the above layer
                Chord.deliverMessage(this.portNum, message);
                //note: the socket layer does not care about the content of the message
            }catch (IOException | ClassNotFoundException e) {
                //do nothing
            }
        }
        try{
            in.close();
            out.close();
            sock.close();
        }catch (IOException e){
            //do nothing
        }
    }



}

