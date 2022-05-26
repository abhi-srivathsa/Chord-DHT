package chord.model;

import chord.Exceptions.PredecessorException;
import chord.Exceptions.TimerExpiredException;
import chord.Messages.*;
import chord.network.Router;

import java.util.*;

/**
 * Class that manages the communication of the node to which it is associated with the other nodes
 * Creates and forwards at the lower layers the different types of messages
 */
public class NodeDispatcher {

    /**
     * The port associated to this node
     */
    private int port;

    /**
     * List of tickets associated with messages that are waiting for an answer
     */
    private List<Integer> waitingTickets;

    /**
     * List of answers received
     */
    private HashMap<Integer, Message> answers;

    public NodeDispatcher(int port){
        this.answers = new HashMap<>();
        this.port = port;
        this.waitingTickets = new LinkedList<>();
    }

    public void setPort(int port){
        this.port = port;
    }

    /**
     *Set of methods used to send a message and handle the answer
     */

    /**
     * Create and forward a message of notify
     * @param destination information of the message destination
     * @param sender information of the message sender
     * @return
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized Map<String, String> sendNotify(final NodeInfo destination, final NodeInfo sender)throws TimerExpiredException {
        MessagesChanges ms = null;
        ms.NotifyRequestMessage(destination, sender);
        final int ticket= Router.sendMessage(this.port,ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run(){
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.NotifyAnswerMessage(sender, destination,new HashMap<>(), ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }
            }
        });
        while (!this.answers.containsKey(ticket)) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms3 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms3.check();
        return ms3.getFiles();
    }

    /**
     * Create and send a message to request the predecessor of the destination of the message
     * @param destination information of the message destination
     * @param sender information of the message sender
     * @return the predecessor of the node, if it exists
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     * @throws PredecessorException Exception thrown if the predecessor of the receiver of the message does not exist
     */
    public  synchronized NodeInfo sendPredecessorRequest(final NodeInfo destination, final NodeInfo sender) throws TimerExpiredException, PredecessorException {
        MessagesChanges ms = null;
        ms.PredecessorRequestMessage(destination, sender);
        final int ticket = Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.PredecessorAnswerMessage(sender,null, destination,ticket, false);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket,ms1);
                    }
                }

            }
        });
        while (!this.answers.containsKey(ticket)){
            try{
                wait();
            }catch (InterruptedException  e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms3;
        ms3 = (MessagesChanges) this.answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms3.check();
        ms3.checkPredecessorException();
        return ms3.getPredecessor();
    }

    /**
     * Create and send a request to know the successor of a given key
     * @param destination information of the message destination
     * @param node key of the node whose successor the sender is looking for
     * @param sender information of the message sender
     * @return the successor of the node if it exists
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized NodeInfo sendSuccessorRequest(final NodeInfo destination, String node, final NodeInfo sender)throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.SuccessorRequestMessage(destination, node, sender);
        final int ticket= Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.SuccessorAnswerMessage(sender,null, destination,ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket,ms1);
                    }
                }

            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) this.answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
        return ms2.getSuccessor();
    }

    /**
     * Create and send a request to know the first successor of the receiver
     * @param destination information of the message destination
     * @param sender information of the message sender
     * @return the first successor of the node if it exists
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized NodeInfo sendFirstSuccessorRequest(final NodeInfo destination,final NodeInfo sender)throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.FirstSuccessorRequestMessage(destination,sender);
        final int ticket= Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.FirstSuccessorAnswerMessage(sender,null,destination,ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket,ms1);
                    }
                }

            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2;
        ms2 = (MessagesChanges)this.answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
        return ms2.getSuccessor();
    }

    /**
     * Create and send a ping message
     * @param destination information of the message destination
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     *
     */
    public synchronized void sendPing(final NodeInfo destination, final NodeInfo sender) throws TimerExpiredException {
        MessagesChanges ms = null;
        ms.PingRequestMessage(destination,sender);
        final int ticket=Router.sendMessage(this.port,ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.PingAnswerMessage(sender,destination, ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }

            }
        });

        while(!this.answers.containsKey(ticket)){
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Create and send a start request message
     * @param destination information of the message destination
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized void sendStartRequest(final NodeInfo destination, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.StartRequestMessage(destination,sender);
        final int ticket=Router.sendMessage(this.port,ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.StartAnswerMessage(sender,destination, ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }

            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Create and send a message to notify that the predecessor of the receiver is leaving Chord
     * @param destination information of the message destination
     * @param newPredecessor information about the new predecessor of the receiver (predecessor of the sender)
     * @param files files of the node that must be transfer to the successor that is now responsible for them
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized void sendLeavingPredecessorRequest(final NodeInfo destination, final NodeInfo newPredecessor, final Map<String, String> files, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.LeavingPredecessorRequestMessage(destination,newPredecessor,files, sender);
        final int ticket=Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.LeavingPredecessorAnswerMessage(sender,destination, ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }

            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Create and send a message to notify that is the successor of the receiver is leaving Chord
     * @param destination information of the message destination
     * @param newSuccessor information about the new successor of the receiver (successor of the sender)
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized void sendLeavingSuccessorRequest(final NodeInfo destination, final NodeInfo newSuccessor, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.LeavingSuccessorRequestMessage(destination,newSuccessor,sender);
        final int ticket=Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.LeavingSuccessorAnswerMessage(sender,destination, ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }

            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Create and send a request of publish message
     * @param destination information of the message destination (node responsible of the key of the file)
     * @param data file to publish
     * @param key of the file
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized void sendPublishRequest(final NodeInfo destination, final String data, final String key, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.PublishRequestMessage(destination,data,key,sender);
        final int ticket=Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.PublishAnswerMessage(sender,destination,ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }
            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Create and send a request for a file with an associated key
     * @param destination information of the message destination (node responsible of the key of the file)
     * @param key of the file the sender is looking for
     * @param sender information of the message sender
     * @return the file associated to the key
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized String sendFileRequest(final NodeInfo destination,final String key, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.FileRequestMessage(destination,key,sender);
        final int ticket=Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.FileAnswerMessage(sender,null,destination,ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }
            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
        return ms2.getFile();
    }

    /**
     * Create and send a request for delete the file associated to the passed key
     * @param destination information of the message destination (node responsible of the key of the file)
     * @param key of the file to delete
     * @param sender information of the message sender
     * @throws TimerExpiredException Exception thrown if the answer does not arrive within the expiration of the timer
     */
    public synchronized void sendDeleteFileRequest(final NodeInfo destination, final String key, final NodeInfo sender) throws TimerExpiredException{
        MessagesChanges ms = null;
        ms.DeleteFileRequestMessage(destination,key,sender);
        final int ticket=Router.sendMessage(this.port, ms);
        this.waitingTickets.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitingTickets.contains(ticket)){
                        MessagesChanges ms1 = null;
                        ms1.DeleteFileResponseMessage(sender,destination,ticket);
                        ms1.setException(new TimerExpiredException());
                        addAnswer(ticket, ms1);
                    }
                }
            }
        });
        while(!this.answers.containsKey(ticket)){
            try{
                wait();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
        MessagesChanges ms2 = null;
        ms2 = (MessagesChanges) answers.get(ticket);
        waitingTickets.remove((Integer) ticket);
        answers.remove(ticket);
        ms2.check();
    }

    /**
     * Receive and add an answer to the list
     * @param ticket of the message that waits a reply (same ticket for request and answer)
     * @param message of reply
     */
    public synchronized void addAnswer(int ticket, Message message){
        if( waitingTickets.contains(ticket) && !answers.containsKey(ticket)){
            answers.put(ticket,message);
            notifyAll();
        }

    }
}
