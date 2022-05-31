import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//This class is primarily responsible to forward and transport messages across the network.
public class Router {
   //Global linkedList for this class that represents nodes with sockets in the chord network
    static List<SockNode> sockNodes = new LinkedList<SockNode>();
    //Token number counter
    private static int tokenNum = 0;

    /*Below function is used to generate a unique token
    number for messages*/
    public static synchronized int getTocketNum(){
        int maxInt = Integer.MAX_VALUE;
        if(tokenNum>=maxInt) tokenNum=0;
        tokenNum++;
        return tokenNum;
    }



    /*The below function is used to print the current status
      of the nodes (router) in the chord network.*/
    public static void printRouter(){
        for(SockNode n: sockNodes){
            n.printSocketNode();
        }
    }



    /* The below function is used to remove or terminate a node.
       The function will receive the port number of the node that needs
       to be deleted from the network as a parameter.*/
    public static void terminate(int portNum){
        SockNode terminatedNode = null; //temporary holder
        for (SockNode n: sockNodes){
            if (n.getPort() == portNum){ //if the portNumber of a node is equal to received portNumber, proceed to delete it
                n.terminate();
                terminatedNode= n;
            }
        }
        sockNodes.remove(terminatedNode);
    }



    /*Below function is called when a new n1 joins the network or is created and a socket Node with end points
      needs to be generated for it */
    public static void addnode(int portNum) throws PortNumException {
        synchronized (sockNodes){
            SockNode n1; //a new node n1
            try{
                n1 = new SockNode(portNum);
                sockNodes.add(n1); //adding new node to list of nodes
                Threads.executeImmediately(n1); //executing it as runnable thread
                n1.initialize(); //checks active connections of the node and initializes
            }catch (IOException error){
                try{
                    n1 = new SockNode(0); //port zero is a wilcard port, asks system to find own port number
                    sockNodes.add(n1);
                    Threads.executeImmediately(n1);
                    n1.initialize();
                    throw new PortNumException(n1.getPort());
                }catch (IOException e){

                }
            }
        }

    }



    /*This below function will be used to send a reply whenever a msg
      might be received.
      The function will receive the port number of the sender and the
      msg that needs to be delivered as parameters */
    public static void sendAnswer(int portNum, Message msg){
        boolean doneDelivery = false;
        if (msg.getDest().getHash().equals(msg.getSource().getHash())){
            doneDelivery = true;
            Chord.deliverMessage(msg.getDest().getPort(),msg);
        }
        if (doneDelivery==false){
            for (SockNode n: sockNodes){
                if (n.getPort() == portNum){
                    n.sendMessage(msg);
                }
            }
        }
    }



    /*This below function will send the message it receives into the network
    and will return a token number which uniquely identifies the message and
    associated reply.
    The function will receive the port number of the sender and the msg 
    to be delivered as parameters.
     */
    public static int sendMessage(int portNum, Message msg) {
        boolean doneDelivery = false;
        int token = getTocketNum();
        msg.setId(token);
        //if destination hash is equal to sender hash, delivery is done
        if (msg.getDest().getHash().equals(msg.getSource().getHash())){
            doneDelivery = true;
            Chord.deliverMessage(msg.getDest().getPort(),msg);
        }
        if (doneDelivery==false){
            for (SockNode n1: sockNodes){
                if (n1.getPort() == portNum){
                    n1.sendMessage(msg);
                }
            }
        }
        return token;
    }

}
