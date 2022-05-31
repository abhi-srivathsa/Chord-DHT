import java.util.Map;

// Handling a message when it is delivered to a node and send the answer

public class MsgHandle implements Runnable {
    private Message message;
    private Node node;

    public MsgHandle(Node node, Message message){
        this.node = node;
        this.message=message;
    }

    @Override
    public void run() {
        if (node.isStarted()){
            node.start(message.getSource());
        }
        switch (message.getType()){
            case 1://ping
                PingAnsMsg pingAnswerMessage= new PingAnsMsg(message.getSource(),message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(),pingAnswerMessage);
                break;
            case 2: //predecessor
                boolean exception = false;
                Ninfo predecessor = null;
                try{
                    predecessor = node.getPredecessor();
                }catch (PredException e){
                    exception = true;
                }
                PredAnsMsg predecessorAnswerMessage = new PredAnsMsg(message.getSource(), predecessor,message.getDest(),message.getId(), exception);
                Router.sendAnswer(node.getPort(), predecessorAnswerMessage);
                break;

            case 3: //find successor
                Ninfo successor=node.findSuccessor(((SucReqMsg) message).getNodeidentifier());
                SucAnsMsg successorAnswerMessage= new SucAnsMsg(message.getSource(),successor,message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(),successorAnswerMessage);
                break;

            case 4:  //notify
                node.notify(message.getSource());
                Map<String, String> newFiles = node.getFs().retrieveFiles(message.getSource().getHash());
                NotifAnsMsg notifyAnswerMessage= new NotifAnsMsg(message.getSource(),message.getDest(), newFiles, message.getId());
                Router.sendAnswer(node.getPort(),notifyAnswerMessage);
                break;

            case 5:  // first successor
                Ninfo firstSuccessor= node.getFSuccessor();
                FirstSucAnsMsg firstSuccessorAnswerMessage= new FirstSucAnsMsg(message.getSource(),firstSuccessor,message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(),firstSuccessorAnswerMessage);
                break;

            case 6: //answer
                NDis dispatcher = node.getDis();
                dispatcher.addAnswer(message.getId(),message);
                break;

            case 17: // delete
                node.deleteMyFile(((DelFileReqMsg) message).getKey());
                DelFileAnsMsg deleteFileAnswerMessage= new DelFileAnsMsg(message.getSource(), message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(), deleteFileAnswerMessage);
                break;

            case 25: // file request
                String file = node.getMyFile(((FileReqMsg) message).getKey());
                FileAnsMsg fileAnswerMessage= new FileAnsMsg(message.getSource(),file,message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(), fileAnswerMessage);
                break;

            case 33: //start
                node.start(message.getSource());
                StartAnsMsg startAnswerMessage = new StartAnsMsg(message.getSource(), message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(), startAnswerMessage);
                break;

            case 44: // leavePredecessor
                node.notifyLeavingPredecessor( ((LeavingPredReqMsg) message).getNewPredecessor());
                if (!((LeavingPredReqMsg) message).getFiles().isEmpty()){
                    for (Map.Entry<String, String> files : ((LeavingPredReqMsg) message).getFiles().entrySet()){
                        node.publishFile(files.getKey(), files.getValue());
                    }
                }
                LeavePredAnsMsg leavingPredecessorAnswerMessage = new LeavePredAnsMsg(message.getSource(), message.getDest(), message.getId());
                Router.sendAnswer(node.getPort(), leavingPredecessorAnswerMessage);
                break;
            case 45: // leaveSuccessor
                node.notifyLeavingSuccessor(((LeavingSucReqMsg) message).getNewSuccessor());
                LeavingSucAnsMsg leavingSuccessorAnswerMessage = new LeavingSucAnsMsg(message.getSource(), message.getDest(), message.getId());
                Router.sendAnswer(node.getPort(), leavingSuccessorAnswerMessage);
                break;

            case 85: // publish
                node.publishFile(((PubReqMsg) message).getKey(), ((PubReqMsg) message).getData());
                PubAnsMsg publishAnswerMessage= new PubAnsMsg(message.getSource(), message.getDest(),message.getId());
                Router.sendAnswer(node.getPort(), publishAnswerMessage);
                break;


        }
    }
}


