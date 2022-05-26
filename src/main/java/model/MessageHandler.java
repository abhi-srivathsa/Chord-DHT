package chord.model;

import chord.Exceptions.PredecessorException;
import chord.Messages.*;
import chord.network.Router;
//import com.sun.org.apache.xml.internal.serializer.utils.MsgKey;

import java.util.Map;

/**
 * Class to handle a message when it is delivered to a node and send the answer
 *
 * Message protocol:
 * #1: Ping message
 * #2: Request of predecessor message
 * #3: Find successor message
 * #4: Notify message
 * #5: First successor request message
 * #6: Reply message
 * #17: Delete file message
 * #25: Lookup message
 * #33: Start message
 * #44: Leaving predecessor message
 * #45: Leaving successor message
 * #85: Publish message
 *
 */
public class MessageHandler implements Runnable {
    private Message message;

    private MessagesChanges ms;
    private Node node;

    public MessageHandler (Node node, Message message){
        this.node = node;
        this.message=message;
    }

    @Override
    public void run() {
        if (node.isStarted()){
            node.start(message.getSender());
        }
        switch (message.getType()){
            case 1://ping
                ms.PingAnswerMessage(message.getSender(),message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(),ms);
                break;
            case 2: //predecessor
                boolean exception = false;
                NodeInfo predecessor = null;
                try{
                    predecessor = node.getPredecessor();
                }catch (PredecessorException e){
                    exception = true;
                }
                ms.PredecessorAnswerMessage(message.getSender(), predecessor,message.getDestination(),message.getId(), exception);
                Router.sendAnswer(node.getPort(), ms);
                break;

            case 3: //find successor
                NodeInfo successor=node.findSuccessor(((MessagesChanges) message).getNodeidentifier());
                ms.SuccessorAnswerMessage(message.getSender(),successor,message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(),ms);
                break;

            case 4:  //notify
                node.notify(message.getSender());
                Map<String, String> newFiles = node.getFileSystem().retrieveFiles(message.getSender().getHash());
                ms.NotifyAnswerMessage(message.getSender(),message.getDestination(), newFiles, message.getId());
                Router.sendAnswer(node.getPort(),ms);
                break;

            case 5:  // first successor
                NodeInfo firstSuccessor= node.getFirstSuccessor();
                ms.FirstSuccessorAnswerMessage(message.getSender(),firstSuccessor,message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(),ms);
                break;

            case 6: //answer
                NodeDispatcher dispatcher = node.getDispatcher();
                dispatcher.addAnswer(message.getId(),message);
                break;

            case 17: // delete
                node.deleteMyFile(((MessagesChanges) message).getKey());
                ms.DeleteFileResponseMessage(message.getSender(), message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;

            case 25: // file request
                String file = node.getMyFile(((MessagesChanges)message).getKey());
                ms.FileAnswerMessage(message.getSender(),file,message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;

            case 33: //start
                node.start(message.getSender());
                ms.StartAnswerMessage(message.getSender(), message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;

            case 44: // leavePredecessor
                node.notifyLeavingPredecessor( ((MessagesChanges) message).getNewPredecessor());
                if (!((MessagesChanges) message).getFiles().isEmpty()){
                    for (Map.Entry<String, String> files : ((MessagesChanges) message).getFiles().entrySet()){
                        node.publishFile(files.getKey(), files.getValue());
                    }
                }
                ms.LeavingPredecessorAnswerMessage(message.getSender(), message.getDestination(), message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;
            case 45: // leaveSuccessor
                node.notifyLeavingSuccessor(((MessagesChanges) message).getNewSuccessor());
                ms.LeavingSuccessorAnswerMessage(message.getSender(), message.getDestination(), message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;

            case 85: // publish
                node.publishFile(((MessagesChanges) message).getKey(), ((MessagesChanges) message).getData());
                ms.PublishAnswerMessage(message.getSender(), message.getDestination(),message.getId());
                Router.sendAnswer(node.getPort(), ms);
                break;


        }
    }
}


