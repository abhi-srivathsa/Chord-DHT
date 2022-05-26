package chord.Messages;

//package chord.Messages;
import chord.Exceptions.PredecessorException;
import chord.model.NodeInfo;

import java.util.Map;

public class MessagesChanges extends Message {
    public String key;
    public String data;
    public String file;
    public NodeInfo successor;
    public NodeInfo newPredecessor;
    public Map<String, String> files;
    public NodeInfo newSuccessor;
    public String nodeidentifier;
    public NodeInfo predecessor;
    public PredecessorException predecessorException;

    public MessagesChanges(int type, boolean ack, NodeInfo destination, NodeInfo sender) {
        super(type, ack, destination, sender);
        // TODO Auto-generated constructor stub
    }

    // private final String key;

    public void DeleteFileResponseMessage(NodeInfo destination, NodeInfo sender, int ticket) {
        Message obj = new Message(6, false, destination, sender);
        // super(6, false, destination, sender);
        this.id = ticket;
    }

    public void DeleteFileRequestMessage(NodeInfo destination, String key, NodeInfo sender) {
        // super(17, true, destination,sender);
        Message obj = new Message(17, true, destination, sender);
        this.key = key;
    }
    public void FileAnswerMessage(NodeInfo destination, String file, NodeInfo sender, int ticket){
        Message obj = new Message(6, false, destination, sender);
        this.file=file;
        this.id=ticket;
    }
    public void FileRequestMessage(NodeInfo destination, String key, NodeInfo sender){
        Message obj = new Message(25, true, destination,sender);
        this.key=key;
    }
    public void FirstSuccessorAnswerMessage(NodeInfo destination, NodeInfo successor, NodeInfo sender, int ticket ) {
        Message obj = new Message(6, false, destination,sender);
        this.successor = successor;
        this.id=ticket;
    }
    public void FirstSuccessorRequestMessage(NodeInfo destination, NodeInfo sender) {
        Message obj = new Message(5, true, destination, sender);
    }
    public void LeavingPredecessorAnswerMessage(NodeInfo destination, NodeInfo sender, int ticket) {
        Message obj = new Message(6, false, destination, sender);
        this.id = ticket;
    }
    public void LeavingPredecessorRequestMessage(NodeInfo destination, NodeInfo newPredecessor, Map<String, String> files, NodeInfo sender) {
        Message obj = new Message(44, true, destination, sender);
        this.newPredecessor = newPredecessor;
        this.files = files;
    }
    public void LeavingSuccessorAnswerMessage( NodeInfo destination, NodeInfo sender, int ticket) {
        Message obj = new Message(6, false, destination, sender);
        this.id=ticket;
    }
    public void LeavingSuccessorRequestMessage( NodeInfo destination, NodeInfo newSuccessor, NodeInfo sender) {
        Message obj = new Message(45, true, destination, sender);
        this.newSuccessor = newSuccessor;
    }
    public void NotifyAnswerMessage(NodeInfo destination, NodeInfo sender, Map<String, String> files, int ticket) {
        Message obj = new Message(6, false, destination, sender);
        this.files = files;
        this.id=ticket;
    }
    public void NotifyRequestMessage( NodeInfo destination, NodeInfo sender) {
        Message obj = new Message(4 , true, destination, sender);
    }
    public void PingAnswerMessage(NodeInfo destination, NodeInfo sender, int ticket) {
        Message obj = new Message(6, false, destination,sender) ;
        this.id=ticket;
    }
    public void PingRequestMessage(NodeInfo destination, NodeInfo sender) {

        Message obj = new Message(1, true, destination,sender);
    }
    public void PredecessorRequestMessage(NodeInfo destination,  NodeInfo sender) {

        Message obj = new Message(2, true, destination, sender);
    }
    public void PublishAnswerMessage(NodeInfo destination, NodeInfo sender, int ticket){
        Message obj = new Message(6,false,destination,sender);
        this.id=ticket;
    }
    public void PublishRequestMessage(NodeInfo destination, String data, String key, NodeInfo sender){
        Message obj = new Message(85,true,destination,sender);
        this.data=data;
        this.key=key;
    }
    public void StartAnswerMessage(NodeInfo destination, NodeInfo sender, int ticket) {
        Message obj = new Message(6, false, destination,sender) ;
        this.id=ticket;
    }
    public void StartRequestMessage(NodeInfo destination, NodeInfo sender) {

        Message obj = new Message(33, true, destination,sender);
    }
    public void SuccessorAnswerMessage(NodeInfo destination, NodeInfo successor, NodeInfo sender, int ticket ) {
        Message obj = new Message(6, false, destination,sender);
        this.successor = successor;
        this.id=ticket;
    }
    public void SuccessorRequestMessage( NodeInfo destination, String nodeidentifier, NodeInfo sender) {
        Message obj = new Message(3, true, destination, sender);
        this.nodeidentifier = nodeidentifier;
    }
    public void PredecessorAnswerMessage(NodeInfo destination, NodeInfo predecessor, NodeInfo sender, int ticket, boolean exception ) {
        Message obj = new Message(6, false, destination, sender);
        this.id = ticket;
        this.predecessor = predecessor;
        if (exception){
            this.predecessorException = new PredecessorException();
        }
        else {
            this.predecessorException = null;
        }
    }

    public NodeInfo getPredecessor() {
        return predecessor;
    }

    public void checkPredecessorException() throws PredecessorException{
        if (this.predecessorException != null){
            throw this.predecessorException;
        }
    }


    public String getNodeidentifier() {
        return nodeidentifier;
    }
    public String getData(){
        return this.data;
    }

    public NodeInfo getNewSuccessor() {
        return newSuccessor;
    }

    public NodeInfo getNewPredecessor() {
        return newPredecessor;
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public String getKey() {
        return key;
    }

    public String getFile() {
        return file;
    }
    public NodeInfo getSuccessor() {
        return successor;
    }

}
