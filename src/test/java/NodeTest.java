import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class NodeTest {
    Node node;
    NDis mockDispatcher;
    Node node1;
    NDis mockDispatcher1;
    @BeforeEach
    public void setUp() throws Exception{
        Ninfo ninfo= new Ninfo("0000");
        Ninfo ninfo1 = new Ninfo("abcd");
        node= new Node(ninfo);
        node1 = new Node(ninfo1);
        node.setDis(mockDispatcher);
        node1.setDis(mockDispatcher1);
        FingerTable fingerTable= new FingerTable("0000");
        SList sList = new SList("0000");
        int i = 0;
        while(i<16){
            Ninfo ninfo2 = new Ninfo(Helper.computefinger("0000",i));
            fingerTable.addFinger(ninfo2);
            i++;
        }
        i=0;
        while(i<4){
            Ninfo ninfo2= new Ninfo(Helper.computefinger("0000", i));
            sList.addEntry(ninfo2);
            i++;
        }

        node.setfTable(fingerTable);
        node.setSuccessors(sList);

        fingerTable.printTable();
        sList.printTable();
    }
    @org.junit.jupiter.api.Test
    void getfTable() {
    }

    @org.junit.jupiter.api.Test
    void getSuccessors() {
    }

    @org.junit.jupiter.api.Test
    void getInfo() {
    }

    @org.junit.jupiter.api.Test
    void setInfo() {
    }

    @org.junit.jupiter.api.Test
    void setfTable() {
    }

    @org.junit.jupiter.api.Test
    void setSuccessors() {
    }

    @org.junit.jupiter.api.Test
    void setDis() {
    }

    @org.junit.jupiter.api.Test
    void getDis() {
    }

    @org.junit.jupiter.api.Test
    void getFs() {
    }

    @org.junit.jupiter.api.Test
    void getPort() {
    }

    @org.junit.jupiter.api.Test
    void getPredecessor() {
    }

    @org.junit.jupiter.api.Test
    void modifyPort() {
    }

    @org.junit.jupiter.api.Test
    void getFSuccessor() {
    }

    @org.junit.jupiter.api.Test
    void setSingle() {
    }

    @org.junit.jupiter.api.Test
    void setStarted() {
    }

    @org.junit.jupiter.api.Test
    void start() {
    }

    @org.junit.jupiter.api.Test
    void testNotify() {
    }

    @org.junit.jupiter.api.Test
    void stabilization() {
    }

    @org.junit.jupiter.api.Test
    void fixFTable() {
    }

    @org.junit.jupiter.api.Test
    void fixSList() {
    }

    @org.junit.jupiter.api.Test
    void checkPredecessor() {
    }

    @org.junit.jupiter.api.Test
    void findSuccessor() {
        Ninfo res= node.findSuccessor("0000");
        Ninfo res1= node.findSuccessor("0002");
        Ninfo res2= node.findSuccessor("0003");
        Ninfo res3= node.findSuccessor("0007");

        assert (res.getHash().equals("0000"));
        assert (res1.getHash().equals("0002"));
        assert (res2.getHash().equals("0004"));
        assert (res3.getHash().equals("0008"));
    }
//    @Test
//    public void initialize() {
//        node.initialize();
//
//        for(Ninfo ninfo: node.getSuccessors().getSuccessors()){
//            assert (ninfo.equals(node.getInfo()));
//        }
//        for(Ninfo ninfo: node.getfTable().getFingers()){
//            assert (ninfo.equals(node.getInfo()));
//        }
//        try {
//            Ninfo ninfo = node.getPredecessor();
//            assert(ninfo.equals(node.getInfo()));
//        } catch (PredException e) {
//            e.printStackTrace();
//        }
//
//    }

    @org.junit.jupiter.api.Test
    void terminate() {
    }

    @org.junit.jupiter.api.Test
    void notifyLeavingPredecessor() {
    }

    @org.junit.jupiter.api.Test
    void notifyLeavingSuccessor() {
    }

    @org.junit.jupiter.api.Test
    void repopulateSuccessorList() {
    }

    @org.junit.jupiter.api.Test
    void findRandomFinger() {
    }

    @org.junit.jupiter.api.Test
    void publish() {
    }

    @org.junit.jupiter.api.Test
    void publishFile() {
    }

    @org.junit.jupiter.api.Test
    void getFile() {
    }

    @org.junit.jupiter.api.Test
    void getMyFile() {
    }

    @org.junit.jupiter.api.Test
    void deleteFile() {
    }

    @org.junit.jupiter.api.Test
    void deleteMyFile() {
    }

    @org.junit.jupiter.api.Test
    void printStatus() {
    }
}