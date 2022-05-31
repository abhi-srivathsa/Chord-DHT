import java.util.*;
public class NDis {

     int port;
     List<Integer> waitTicks;
     HashMap<Integer, Message> answers;

    public NDis(int port){
        this.answers = new HashMap<>();
        this.port = port;
        this.waitTicks = new LinkedList<>();
    }
// request the predecessor of the dest of the message

    public  synchronized Ninfo sendPredecessorRequest(final Ninfo dest, final Ninfo source) throws TimeEndedException, PredException {
        PredReqMsg predecessorRequestMessage = new PredReqMsg(dest, source);
        final int ticket = Router.sendMessage(this.port, predecessorRequestMessage);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        PredAnsMsg predecessorAnswerMessage = new PredAnsMsg(source,null, dest,ticket, false);
                        predecessorAnswerMessage.setException(new TimeEndedException());
                        addAnswer(ticket,predecessorAnswerMessage);
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
        PredAnsMsg answerMessage = (PredAnsMsg) this.answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        answerMessage.check();
        answerMessage.checkPredecessorException();
        return answerMessage.getPredecessor();
    }

    // Requesting the successor of a given key

    public synchronized Ninfo sendSuccessorRequest(final Ninfo dest, String node, final Ninfo source)throws TimeEndedException {
        SucReqMsg sucReqMsg = new SucReqMsg(dest, node, source);
        final int ticket= Router.sendMessage(this.port, sucReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        SucAnsMsg sucAnsMsg = new SucAnsMsg(source,null, dest,ticket);
                        sucAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, sucAnsMsg);
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
        SucAnsMsg sucAnsMsg = (SucAnsMsg)this.answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        sucAnsMsg.check();
        return sucAnsMsg.getSuccessor();
    }

    public void setPort(int port){
        this.port = port;
    }
    // notify message
    public synchronized Map<String, String> sendNotify(final Ninfo dest, final Ninfo source)throws TimeEndedException {
        NotifReqMsg notifReqMsg =new NotifReqMsg(dest, source);
        final int ticket= Router.sendMessage(this.port, notifReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run(){
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        NotifAnsMsg notifAnsMsg = new NotifAnsMsg(source, dest,new HashMap<>(), ticket);
                        notifAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, notifAnsMsg);
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
        NotifAnsMsg notifAnsMsg = (NotifAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        notifAnsMsg.check();
        return notifAnsMsg.getFiles();
    }

    // add an answer to the list

    public synchronized void addAnswer(int ticket, Message message){
        if( waitTicks.contains(ticket) && !answers.containsKey(ticket)){
            answers.put(ticket,message);
            notifyAll();
        }

    }

    // Requesting the first successor of the receiver

    public synchronized Ninfo sendFirstSuccessorRequest(final Ninfo dest, final Ninfo source)throws TimeEndedException {
        FirstSucReqMsg firstSucReqMsg = new FirstSucReqMsg(dest,source);
        final int ticket= Router.sendMessage(this.port, firstSucReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        FirstSucAnsMsg firstSucAnsMsg = new FirstSucAnsMsg(source,null,dest,ticket);
                        firstSucAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, firstSucAnsMsg);
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
        FirstSucAnsMsg firstSucAnsMsg = (FirstSucAnsMsg)this.answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        firstSucAnsMsg.check();
        return firstSucAnsMsg.getSuccessor();
    }

    // sending a ping

    public synchronized void sendPing(final Ninfo dest, final Ninfo source) throws TimeEndedException {
        PingReqMsg pingReqMsg = new PingReqMsg(dest,source);
        final int ticket=Router.sendMessage(this.port, pingReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        PingAnsMsg pingAnsMsg = new PingAnsMsg(source,dest, ticket);
                        pingAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, pingAnsMsg);
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
        PingAnsMsg pingAnsMsg = (PingAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        pingAnsMsg.check();
    }

    // sending a start request

    public synchronized void sendStart(final Ninfo dest, final Ninfo source) throws TimeEndedException {
        StartReqMsg startReqMsg = new StartReqMsg(dest,source);
        final int ticket=Router.sendMessage(this.port, startReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        StartAnsMsg startAnsMsg = new StartAnsMsg(source,dest, ticket);
                        startAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, startAnsMsg);
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
        StartAnsMsg startAnsMsg = (StartAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        startAnsMsg.check();
    }


     // notifying that the predecessor is leaving

    public synchronized void sendLeavingPredecessorRequest(final Ninfo dest, final Ninfo newPredecessor, final Map<String, String> files, final Ninfo source) throws TimeEndedException {
        LeavingPredReqMsg leavingPredReqMsg = new LeavingPredReqMsg(dest,newPredecessor,files, source);
        final int ticket=Router.sendMessage(this.port, leavingPredReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        LeavePredAnsMsg leavePredAnsMsg = new LeavePredAnsMsg(source,dest, ticket);
                        leavePredAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, leavePredAnsMsg);
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
        LeavePredAnsMsg leavePredAnsMsg = (LeavePredAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        leavePredAnsMsg.check();
    }

    // notifying that is the successor is leaving

    public synchronized void sendLeavingSuccessorRequest(final Ninfo dest, final Ninfo newSuccessor, final Ninfo source) throws TimeEndedException {
        LeavingSucReqMsg leavingSucReqMsg = new LeavingSucReqMsg(dest,newSuccessor,source);
        final int ticket=Router.sendMessage(this.port, leavingSucReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        LeavingSucAnsMsg leavingSucAnsMsg = new LeavingSucAnsMsg(source,dest, ticket);
                        leavingSucAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, leavingSucAnsMsg);
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
        LeavingSucAnsMsg leavingSucAnsMsg = (LeavingSucAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        leavingSucAnsMsg.check();
    }
    // requesting for a file with an associated key

    public synchronized String sendFileRequest(final Ninfo dest, final String key, final Ninfo source) throws TimeEndedException {
        FileReqMsg fileReqMsg = new FileReqMsg(dest,key,source);
        final int ticket=Router.sendMessage(this.port, fileReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        FileAnsMsg fileAnsMsg = new FileAnsMsg(source,null,dest,ticket);
                        fileAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, fileAnsMsg);
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
        FileAnsMsg fileAnsMsg = (FileAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        fileAnsMsg.check();
        return fileAnsMsg.getFile();
    }

    // deleting the file associated to the passed key

    public synchronized void sendDeleteFileRequest(final Ninfo dest, final String key, final Ninfo source) throws TimeEndedException {
        DelFileReqMsg delFileReqMsg = new DelFileReqMsg(dest,key,source);
        final int ticket=Router.sendMessage(this.port, delFileReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        DelFileAnsMsg delFileAnsMsg = new DelFileAnsMsg(source,dest,ticket);
                        delFileAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, delFileAnsMsg);
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
        DelFileAnsMsg delFileAnsMsg = (DelFileAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        delFileAnsMsg.check();
    }


    // Sending a request of publish message

    public synchronized void sendPublishRequest(final Ninfo dest, final String data, final String key, final Ninfo source) throws TimeEndedException {
        PubReqMsg pubReqMsg = new PubReqMsg(dest,data,key,source);
        final int ticket=Router.sendMessage(this.port, pubReqMsg);
        this.waitTicks.add(ticket);
        Threads.executeAfterDelay(new TimerTask() {
            @Override
            public void run() {
                synchronized (this){
                    if(waitTicks.contains(ticket)){
                        PubAnsMsg pubAnsMsg = new PubAnsMsg(source,dest,ticket);
                        pubAnsMsg.setException(new TimeEndedException());
                        addAnswer(ticket, pubAnsMsg);
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
        PubAnsMsg pubAnsMsg = (PubAnsMsg) answers.get(ticket);
        waitTicks.remove((Integer) ticket);
        answers.remove(ticket);
        pubAnsMsg.check();
    }



}
