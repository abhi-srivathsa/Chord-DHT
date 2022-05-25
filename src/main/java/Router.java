import Exceptions.PortException;
import org.w3c.dom.Node;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//This class is primarily responsible to forward and transport messages across the network.
public class Router {
   //Global linkedList for this class that represents nodes with sockets in the chord network
    static List<SocketNode> sockNodes = new LinkedList<SocketNode>();

    //Token number counter
    private static int tokenNum = 0;

    /*Below function is used to generate a unique token number for messages
     */
    public static synchronized int getTocketNum(){
        int maxInt = Integer.MAX_VALUE;
        if(tokenNum>=maxInt) tokenNum=0;
        tokenNum++;
        return tokenNum;
    }



    /*Below function is called when a new n1 joins the network or is created and a socket Node with end points
      needs to be generated for it */
    public static void addnode(int portNum) throws PortException {
        synchronized (sockNodes){
            SocketNode n1; //a new node n1
            try{
                n1 = new SocketNode(portNum);
                sockNodes.add(n1); //adding new node to list of nodes
                Threads.executeImmediately(n1); //executing it as runnable thread
                n1.initialize(); //checks active connections of the node and initializes
            }catch (IOException error){
                try{
                    n1 = new SocketNode(0); //port zero is a wilcard port, asks system to find own port number
                    sockNodes.add(n1);
                    Threads.executeImmediately(n1);
                    n1.initialize();
                    throw new PortException(n1.getPort());
                }catch (IOException e){

                }
            }
        }

    }


    /*This below function will send the message it receives into the network
    and will return a token number which uniquely identifies the message and
    associated reply
     */
    public static int sendMessage(int portNum, Message message) {
        int token = getTocketNum();
        boolean doneDelivery = false;
        message.setId(token);
        //if destination hash is equal to sender hash, delivery is done
        if (message.getDestination().getHash().equals(message.getSender().getHash())){
            doneDelivery = true;
            Chord.deliverMessage(message.getDestination().getPort(),message);
        }
        if (doneDelivery==false){
            for (SocketNode n1: sockNodes){
                if (n1.getPort() == portNum){
                    n1.sendMessage(message);
                }
            }
        }
        return token;
    }


    




}
