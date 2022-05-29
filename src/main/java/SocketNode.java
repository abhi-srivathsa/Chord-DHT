import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import SocketHandler;


public class SocketNode implements Runnable {
    ServerSocket serverSock; //socket to listen for new connections
    int portNum; //every node is associated to a port number
    boolean deleted;
    ScheduledFuture delete;
    final List<SocketHandler> handle;

    /* Below is the constructor to initialize class variables */
    public SocketNode(int portNum) throws IOException{
        this.serverSock = new ServerSocket(portNum);
        this.portNum = serverSock.getLocalPort();
        this.handle = new LinkedList<>();
        this.deleted = false;
    }

    @Override
    public void run() {
        while (deleted==false) {
            try {
                Socket client = serverSock.accept(); //connection request received
                synchronized (this.handle){
                    SocketHandler handler = new SocketHandler(this.portNum, client); //creating new handler for this client or connection
                    Threads.executeImmediately(handler); //handling the ne client connection
                    this.handle.add(handler); //store the new connection to the list
                }
            } catch (IOException err) {
                err.printStackTrace(); //if error, printing the msg.
            }
        }
        try{
            //close socket
            serverSock.close();
        }catch (IOException err){
            //don't handle this err
        }
    }



    /*Below function is used to terminate or delete 
    the node associated with the socket */
    public void terminate(){
        this.delete.cancel(true);
        for (SocketHandler h : this.handle){  //terminate all the handle
            h.terminate();
        }
        this.deleted = true; //mark the handler as deleted.
    }

    /* Keep track of the connections that are still active
     and delete the connections that are not recently used.
     */
    public void initialize(){
        this.delete = Threads.executeRarely(() -> {
            synchronized (handle){
                List<SocketHandler> unusedHandlers = new LinkedList<>();
                //Keep track of the socket handle that are recently used and those that are not
                for (SocketHandler h: handle){
                    if (h.isUsedRecently()==false){
                        unusedHandlers.add(h);
                        h.terminate();
                    }else{
                        h.setUsedRecently(false);
                    }
                }

                //remove the unused handle from the handle list
                for (SocketHandler h: unusedHandlers){
                    handle.remove(h);
                }
            }
        });
    }
    
    /* Below is a getter that returns the port number where the
    node is listening i.e the port of the server socket 
    */
     public int getPort(){
            return this.portNum;
     }


    public  void sendMessage(Message msg){
        NodeInfo nDetails = msg.getDestination();
        boolean toSend = false;
        /*If end point or destination of msg and handler end point are the same,
         send the msg.Otherwise, mark the handler up for deletion and terminate it*/
        synchronized (this.handle){
            List<SocketHandler> handlersToDelete = new LinkedList<>(); //list of handlers to delete
            for (SocketHandler h: this.handle){
                try{
                    if (nDetails.equals(h.getEndpoint())){
                        h.sendMessage(msg);
                        toSend = true;
                    }
                }catch (IOException err ){
                    handlersToDelete.add(h);
                    h.terminate();
                }
                catch(Exception err){
                }
            }

            
            if (handlersToDelete.isEmpty()==false){
                for (SocketHandler h: handlersToDelete){
                    handle.remove(h);
                }
            }


            if (toSend==false){
                Socket sock = null;
                try {//create new socket and new handler
                    sock = new Socket(nDetails.getIPAddress(),nDetails.getPort());
                    SocketHandler handler = new SocketHandler(this.port,sock);
                    this.handle.add(handler); //add new connection to list of handlers
                    Threads.executeImmediately(handler); //execute handler
                    handler.sendMessage(msg); //send msg
                }catch(IOException err){
                }
            }
        }
    }




}
