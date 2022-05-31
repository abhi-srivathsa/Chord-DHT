import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

public class Node {
     Ninfo info;
     String nodeId;
     FingerTable fTable;
     FileSystem fs;
     NodeComparator comparator;
     boolean started;
     SList successors;
     Ninfo predecessor;
     NDis dis;
     int fixFCounter;
     ScheduledFuture helperThread;

    public Node(Ninfo cur) {
        this.info = cur;
        this.nodeId = cur.getHash();
        this.fTable = new FingerTable(cur.getHash());
        this.successors = new SList(cur.getHash());
        this.predecessor = null;
        this.dis = new NDis(this.getPort());
        this.fixFCounter = 0;
        this.fs = new FileSystem(cur.getHash());
        this.comparator=new NodeComparator(cur.getHash());
        this.started = false;
    }

    public FingerTable getfTable() {
        return fTable;
    }

    public SList getSuccessors() {
        return successors;
    }

    public Ninfo getInfo() {
        return info;
    }

    public void setInfo(Ninfo info) {
        this.info = info;
    }

    public void setfTable(FingerTable fTable) {
        this.fTable = fTable;
    }

    public void setSuccessors(SList successors) {
        this.successors = successors;
    }

    public void setDis(NDis NDis){
        this.dis = NDis;
    }
    public NDis getDis() {

        return dis;
    }
    public FileSystem getFs(){
        return fs;
    }
    public int getPort() {
        return this.info.getPort();
    }
    public Ninfo getPredecessor()throws PredException {
        if (this.predecessor == null){
            throw new PredException();
        }
        return predecessor;
    }
    // When provided port is not available, an available random port is assigned
    public void modifyPort(int port) {
        this.info.setPort(port);
        this.dis.setPort(port);
        Ninfo me = this.info;
        String key = me.getIp().concat(Integer.toString(me.getPort()));
        this.nodeId = Helper.calculateHash(key);
    }
    //get first successor
    public Ninfo getFSuccessor(){

        return successors.getFirst();
    }

    public boolean isSingle() {

        return this.successors.isSingle();
    }

    public void setSingle(boolean single) {

        this.successors.setSingle(single);
    }

    public boolean isStarted() {

        return started;
    }
    public void setStarted(boolean started) {

        this.started = started;
    }



    // Initialize method when there is no chord ring
    public void initialize() {
        int i = 0;
        while(i< Helper.numberOfBit()){
            fTable.addFinger(this.info);
            i++;
        }
        i = 0;
        while(i<4) {
            successors.addEntry(this.info);
            i++;
        }
        this.predecessor = this.info;
        this.helperThread = Threads.executePeriodically(new Helper(this));
    }
    // Initialize method when there is a chord ring
    public  void initialize(final Ninfo newNode) throws NotInitException {
        try {
            Ninfo successor = this.dis.sendSuccessorRequest(newNode, this.nodeId, this.info);
            this.successors.addEntry(successor);
            this.fTable.addFinger( successor);
            this.predecessor=null;
        } catch (TimeEndedException e) {
            throw new NotInitException("Unable to initialize.");
        }


        int i=1;
        try{
            for (i = 1; i < 4; i++) {
                Ninfo lastElement = successors.getLast();
                Ninfo successor = null;
                successor = dis.sendFirstSuccessorRequest(lastElement, info);
                if (successor.getHash().equals(nodeId)) {
                    while (i < 4) {
                        successors.addEntry(info);
                        i++;
                    }
                } else {
                    successors.addEntry(successor);
                }
            }
        }catch (TimeEndedException e){
            for(;i<4;i++) {
                successors.addEntry(info);
            }
        }

        int j = 1;
        while(j< Helper.numberOfBit()) {
            String hashedkey = Helper.computefinger(nodeId, j);
            try {
                Ninfo finger = dis.sendSuccessorRequest(successors.getFirst(), hashedkey, info);
                fTable.addFinger(finger);
                j++;
            } catch (TimeEndedException e) {
                repopulateSuccessorList(0);
            }
        }
        try{
            dis.sendStart(newNode, this.info);
        }catch (TimeEndedException e){
            throw new NotInitException("unable to initialize");
        }
        this.helperThread = Threads.executePeriodically(new Helper(this));
        if (!successors.getFirst().getHash().equals(this.nodeId)){
            setSingle(false);
        }
        setStarted(true);
    }


    // Checking if there are any new nodes

    public  void start(Ninfo ninfo){
        setStarted(true);
        if (isSingle() && ! (ninfo.equals(this.info))){
            this.successors.modifyEntry(0, ninfo);
            this.fTable.modifyFinger(0, ninfo);
            setSingle(false);
        }
    }

    // Check previous node and notify
    public  void notify(Ninfo potPred) {
        if(potPred.equals(this.info)){
            return;
        }
        if (this.predecessor == null) {
            this.predecessor = potPred;
        } else {
            String predecessorKey = this.predecessor.getHash();
            String potentialKey = potPred.getHash();
            if(comparator.compare(predecessorKey,potentialKey)<0){
                this.predecessor=potPred;
            }
        }
    }

   // Check the next node and notify it
    public void stabilization() {
        Ninfo successor = null;
        try {
            successor = this.successors.getFirst();
            Ninfo potentialSuccessor = this.dis.sendPredecessorRequest(successor, this.info);
            if(potentialSuccessor.getHash().equals(this.nodeId)) {
                return;
            }
            String successorKey = successor.getHash();
            String potentialSuccessorKey = potentialSuccessor.getHash();
            if(comparator.compare(potentialSuccessorKey,successorKey)<0){
                this.successors.modifyEntry(0,potentialSuccessor);
            }
        } catch (TimeEndedException e) {
            repopulateSuccessorList(0);
        }catch (PredException e){}
        try {
            Ninfo newSuccessor = this.successors.getFirst();
            Map<String,String> newFiles = this.dis.sendNotify(newSuccessor, this.info);
            if (!newFiles.isEmpty()){
                for (Map.Entry<String, String> newFile: newFiles.entrySet() ){
                    this.fs.publish(newFile.getKey(), newFile.getValue());
                }
            }
        } catch (TimeEndedException e) {
            repopulateSuccessorList(0);
        }
    }


    // check finger table
    public void fixFTable() {
        String hashedkey = Helper.computefinger(this.nodeId, fixFCounter);
        Ninfo successor = findSuccessor(hashedkey);
        this.fTable.modifyFinger(fixFCounter, successor);
        fixFCounter++;
        if (fixFCounter == Helper.numberOfBit()){
            fixFCounter = 0;
        }
    }

    // check successors
    public void fixSList(){
        int i = 0;
        Ninfo recent = null;
        try {
            while(i<3) {
                recent = successors.getElement(i);
                Ninfo successor = dis.sendFirstSuccessorRequest(recent, info);
                if (successor.getHash().equals(nodeId)) {
                    while (i < 3) {
                        successors.modifyEntry(i + 1, this.info);
                        i++;
                    }
                } else {
                    successors.modifyEntry(i + 1, successor);
                }
                i++;
            }
        } catch (TimeEndedException e) {}
    }

   // Heartbeat to predecessor
    public void checkPredecessor() {
        if (predecessor != null) {
            try {
                dis.sendPing(this.predecessor, this.info);
            } catch (TimeEndedException e) {
                predecessor = null;
            }
        }
    }

   // check which node has key
    public Ninfo findSuccessor(String key){
        Ninfo successor=null;
        if(key.equals(this.nodeId)){
            return this.info;
        }
        FingerTableComparator comparator= new FingerTableComparator(this.nodeId);
        if(predecessor!=null){
            String predecessorKey= predecessor.getHash();
            if( comparator.compare(predecessorKey,key)<0){
                return this.info;
            }
        }
        try {
            successor = successors.closest(key);
            return successor;
        } catch (SListException e) {
        }
        try {
            Ninfo closestPredecessor = fTable.closestPredecessor(key);
            successor = this.dis.sendSuccessorRequest(closestPredecessor,key,this.info);
        } catch (TimeEndedException ex) {
            try{
                this.dis.sendSuccessorRequest(this.successors.getFirst(), key, this.info);
            }catch (TimeEndedException e){
                repopulateSuccessorList(0);
            }
        }
        return successor;
    }

  //Termination
    public  void terminate() {
        helperThread.cancel(true);
        try{
            // I send a message to my successor
            this.dis.sendLeavingPredecessorRequest(this.successors.getFirst(), this.predecessor, this.fs.freeFileSystem(),  this.info);
            // I send a message to my predecessor
            if (this.predecessor != null){
                this.dis.sendLeavingSuccessorRequest(this.predecessor, this.successors.getFirst(), this.info);
            }
        }catch (TimeEndedException e){
            // do nothing
        }
    }

    public  void notifyLeavingPredecessor(Ninfo newPredecessor){
        if (newPredecessor != null){
            this.predecessor = newPredecessor;
        }
    }

    public  void notifyLeavingSuccessor (Ninfo newSuccessor){
        this.successors.modifyEntry(0, newSuccessor);
        this.fTable.modifyFinger(0, newSuccessor);
        fixSList();
    }

    public  void repopulateSuccessorList(int positionOFUnvalidNode){
        int positionOFValidSuccessorNode = positionOFUnvalidNode +1;
        if (positionOFUnvalidNode == 0){
            boolean got = false;
            while (!got && positionOFValidSuccessorNode<4){
                try{
                    this.dis.sendLeavingPredecessorRequest(this.successors.getElement(positionOFValidSuccessorNode), this.info,new HashMap<>(), this.info);
                    got = true;
                }catch (TimeEndedException e){
                    positionOFValidSuccessorNode++;//check the next one
                }
            }
            if (positionOFValidSuccessorNode == 4){
                Ninfo aNode = findRandomFinger(0);
                this.successors.modifyEntry(0,aNode);
            }else{
                this.successors.modifyEntry(0,this.successors.getElement(positionOFValidSuccessorNode));
            }
        }
        else{
            boolean got = false;
            while (!got && positionOFValidSuccessorNode<4){
                try{
                    Ninfo validSuccessor = this.successors.getElement(positionOFValidSuccessorNode);
                    Ninfo validPredecessor = this.successors.getElement(positionOFUnvalidNode-1);
                    this.dis.sendLeavingPredecessorRequest(validSuccessor,validPredecessor , new HashMap<>(),  this.info);
                    got = true;
                }catch (TimeEndedException e){
                    positionOFValidSuccessorNode++;//check the next one
                }
            }
            int positionOFValidPredecessorNode = positionOFUnvalidNode -1;
            if (positionOFValidSuccessorNode <4){
                got = false;
                while (!got && positionOFValidPredecessorNode >= 0){
                    try{
                        this.dis.sendLeavingSuccessorRequest(this.successors.getElement(positionOFValidPredecessorNode), this.successors.getElement(positionOFValidSuccessorNode),this.info);
                        got = true;

                    }catch (TimeEndedException e){
                        positionOFValidPredecessorNode--; //check the next one
                    }
                }
                this.successors.modifyEntry(positionOFUnvalidNode,this.successors.getElement(positionOFValidSuccessorNode));
            }else{
                Ninfo aNode = findRandomFinger(positionOFUnvalidNode);
                this.successors.modifyEntry(positionOFUnvalidNode,aNode);
            }
        }
    }

    public Ninfo findRandomFinger(int position){
        for (int i = position; i< Helper.numberOfBit(); i++){
            Ninfo ninfo = fTable.getFinger(i);
            try{
                this.dis.sendPing(ninfo, this.info);
                return ninfo;
            }catch (TimeEndedException e){
                //try next
            }
        }
        return this.info;
    }

    public void publish(String key, String data){
        Ninfo successor=findSuccessor(key);
        try {
            this.dis.sendPublishRequest(successor, data, key, this.info);
        }
        catch (TimeEndedException e){
            //do nothing
        }
    }

    public void publishFile(String key, String data){
        this.fs.publish(key, data);
    }

    public String getFile(String key){
        String file=null;
        Ninfo successor= findSuccessor(key);
        if(successor.getHash().equals(this.nodeId)){
            file=getMyFile(key);
        }
        else {
            try {
                file = this.dis.sendFileRequest(successor, key, this.info);
            } catch (TimeEndedException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public String getMyFile(String key)  {
        String file=null;
        file= this.fs.getFile(key);
        return file;
    }

    public void deleteFile(String key){
        Ninfo successor= findSuccessor(key);
        if(successor.getHash().equals(this.nodeId)){
            deleteMyFile(key);
        }
        else{
            try {
                this.dis.sendDeleteFileRequest(successor, key, this.info);
            }
            catch (TimeEndedException e){
            }
        }
    }


    public void deleteMyFile(String key){
        fs.deleteFile(key);
    }

    public void printStatus() {
        System.out.println("***********************************");
        System.out.println("\n ");
        System.out.println("Current Node: " + this.nodeId);
        System.out.println("Current Port: "+ this.getPort());
        if (predecessor != null){
            System.out.println("Predecessor is: " + predecessor.getHash());
        }
        else {
            System.out.println("Predecessor is null");
        }
        successors.printTable();
        fTable.printTable();
        fs.print();
        System.out.println("***********************************");
        System.out.println("\n ");
    }


}



