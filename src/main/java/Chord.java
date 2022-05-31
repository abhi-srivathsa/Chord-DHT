import java.util.LinkedList;
import java.util.List;
import com.google.gson.Gson;

public class  Chord {
    // Class containing the methods to implement core Chord functionality

    // List of nodes that are active in the ring
    private static final List<Node> nodes = new LinkedList<>();

    private Chord(){};
    // Creating a new chord ring and a new node

    public static void create(String Ip, int port)throws PortNumException {
        synchronized (nodes){
            Ninfo info = new Ninfo(Ip,port);
            Node node = new Node(info);
            nodes.add(node);
            try{
                Router.addnode(port);
            }catch(PortNumException e){
                node.modifyPort(e.getPort());
                throw e;
            }finally{
                node.initialize();
            }
        }
    }

    // Method to create a new node and join an existing chord ring
    public static void join(String Ip, int port, String knownIp, int knownPort) throws PortNumException, NotInitException {
        synchronized (nodes){
            Ninfo info = new Ninfo(Ip,port);
            Ninfo knownNode = new Ninfo(knownIp,knownPort);
            Node node = new Node(info);
            nodes.add(node);
            try{
                Router.addnode(port);
            }catch(PortNumException e){
                node.modifyPort(e.getPort());
                throw e;
            }finally {
                try {
                    node.initialize(knownNode);
                } catch (NotInitException e) {
                    nodes.remove(node);
                    Router.terminate(node.getPort());
                    throw new NotInitException(e.getMessage());
                }
            }
        }
    }

    // Method to print the finger tables and status
    public static void printChord() {
        for (Node node : nodes) {
            node.printStatus();
        }
        return;
    }

    // Method to add data into a file and generate a key
    public static String insert(Object obj, int port) throws NotInitException {
        // Serialization/Desiaralization to convert java object to json
        Gson gson=new Gson();
        String json=gson.toJson(obj);

        // Hash function generates key using json
        String key= Helper.calculateHash(json);
        Node cur=null;
        for(Node node: nodes){
            if(node.getPort()==port){
                cur=node;
            }
        }
        if(cur!=null) {
            cur.publish(key, json);
        }
        else{
            throw new NotInitException("Invalid port no! ");
        }
        return key;
    }


    // Method to delete the given file
    public static void deleteFile(String key, int port) throws NotInitException {
        Node cur=null;
        for(Node node: nodes){
            if(node.getPort()==port){
                cur=node;
            }
        }
        if(cur!=null){
            cur.deleteFile(key);
        }
        else{
            throw new NotInitException("There is no node associated to this port, try with another port");
        }
    }

    // Method to search for the file
    public static String search(String key, int port) throws NotInitException {
        Node cur=null;
        for(Node node: nodes){
            if(node.getPort()==port){
                cur=node;
            }
        }
        // get the file based on the key
        if(cur!=null) {
            String file = cur.getFile(key);
            return file;
        }
        else{
            throw new NotInitException("There is no node associated to this port, try with another port");
        }
    }

    // Method to terminate the node associated with the port
    public static void deleteNode(int port) throws NotInitException {
        Node node = null;
        for (Node cur: nodes){
            if (cur.getPort() == port){
                node = cur;
            }
        }
        if(node!=null){
            node.terminate();
            Router.terminate(port);
            nodes.remove(node);
        }
        else {
            throw new NotInitException("There is no node associated to this port, try with another port");
        }
    }

    // Delete all nodes and hence delete the ring
    public static void deleteAll(){
        for(Node node: nodes){
            node.terminate();
            Router.terminate(node.getPort());
            nodes.remove(node);
        }
    }




    // when a SocketHandler receives a new message, this method is used to deliver it to the corresponding node.

    public static void deliverMessage(int port, Message message){
        for (Node cur: nodes){
            if (cur.getPort() == port){
                MsgHandle handler = new MsgHandle(cur,message);
                Threads.executeImmediately(handler);
            }
        }
    }


}


