import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class SListTest {
    SList sList;
    SList sList1;
    SList sList2;
    SList sList3;

    @BeforeEach
    void setUp() throws Exception {
        this.sList = new SList("0000");
        this.sList1 = new SList("aaaa");
        this.sList2 = new SList("bbbb");
        this.sList3 = new SList("1000");

        int i = 0;
        while(i<4){
            Ninfo nodeInfo = new Ninfo(Helper.computefinger("0000", i));
            sList.addEntry(nodeInfo);
            i++;
        }
        i = 0;
        while(i<4){
            sList1.addEntry(new Ninfo("aaaa"));
            i++;
        }

        // 3rd variable is not initialized
        i = 0;
        while(i<4){
            Ninfo nodeInfo = new Ninfo(Helper.computefinger("1000", i));
            sList.addEntry(nodeInfo);
            i++;
        }
    }

    @Test
    void printTable() {
    }

    @Test
    void closest() {
        int i = 0;
        while(i<4){
            sList3.addEntry(new Ninfo("eeee"));
            i++;
        }
        Ninfo me = new Ninfo("0000");
        Ninfo nodeInfo = new Ninfo("0001");
        Ninfo nodeInfo1 = new Ninfo("0002");
        Ninfo nodeInfo2 = new Ninfo("0004");
        Ninfo nodeInfo3 = new Ninfo("0008");
        Ninfo nodeInfo4 = new Ninfo("0001");
        Ninfo nodeInfo5 = new Ninfo("aaaa");

        Ninfo response = null;
        try{
            response = sList.closest("0001");
        } catch (SListException e) {
            e.printStackTrace();
        }
        assert (response.equals(nodeInfo));

        try{
            response = sList.closest("0005");
        }catch (SListException e){
        }
        assert (response.equals(nodeInfo3));
        boolean failed = false;
        try{
            response = sList.closest("0010");
        }catch (SListException e){
            failed = true;
        }
        assertTrue(failed);
        try{
            response = sList.closest("0002");
        }catch (SListException e){
            failed = true;
        }
        assert (response.equals(nodeInfo1));
        try{
            response = sList.closest("0004");
        }catch (SListException e){
            failed = true;
        }
        assert (response.equals(nodeInfo2));
//        try{
//            response = sList.closest("9999");
//        }catch (SListException e){
//            failed = true;
//        }
//        assert (response.equals(nodeInfo4));
        try{
            response = sList1.closest("aaaa");
        }catch (SListException e){
            failed = true;
        }
        assert (response.equals(nodeInfo5));

        try{
            response = sList3.closest("eeea");
        }catch (SListException e){
            failed = true;
        }
        assert(response.equals(new Ninfo("eeee")));

    }

    @Test
    void addEntry() {
        Ninfo nodeInfo = new Ninfo("abcd");
        sList.addEntry(nodeInfo);

        assert (!sList.containsSuccessor("abcd"));
        assert (sList.getSuccessors().size()== 4);
        assert (sList.getNode().equals("0000"));
        assert (sList1.getMap().size() == 0);
        for (int i=0; i<4; i++){
            Ninfo nodeInfo1 = new Ninfo(Helper.computefinger("0000", i));
            assert (sList.getSuccessors().get(i).equals(nodeInfo1));
        }

        assert (sList1.getSuccessors().size() == 4);
        assert (sList1.getNode().equals("aaaa"));
        assert (sList1.getMap().size() == 0);
        for ( int i=0; i<4; i++){
            assert (sList1.getSuccessors().get(i).equals(new Ninfo("aaaa")));
        }

        assert (sList2.getSuccessors().size()==0);
        sList2.addEntry(new Ninfo("vxyz"));
        assert (sList2.containsSuccessor("vxyz"));
        assert (sList2.getSuccessors().size()==1);
    }

    @Test
    void getMap() {
    }

    @Test
    void getSuccessors() {
    }

    @Test
    void getNode() {
    }

    @Test
    void containsSuccessor() {
    }

    @Test
    void modifyEntry() {
        Ninfo nodeInfo = new Ninfo("5000");
        sList.modifyEntry(0,nodeInfo);
        int i = 1;
        while(i<4){
            Ninfo nodeInfo1 = new Ninfo(Helper.computefinger("0000", i));
            assert (sList.getSuccessors().get(i).equals(nodeInfo1));
            i++;
        }
        assert (sList.getSuccessors().getFirst().equals(nodeInfo));
    }

    @Test
    void isSingle() {
    }

    @Test
    void setSingle() {
    }

    @Test
    void getElement() {
    }

    @Test
    void getLast() {
        sList2.getSuccessors().add(new Ninfo("eeee"));
        assert (!sList.getLast().equals(new Ninfo("8000")));
        sList.getSuccessors().pollLast();
        Ninfo nodeInfo = sList.getSuccessors().getLast();
        assert (sList.getLast().equals(nodeInfo));
        assert (sList1.getLast().equals(new Ninfo("aaaa")));
        assert (sList2.getLast().equals(new Ninfo("eeee")));
    }
    

    @Test
    void getFirst() {
        Ninfo nodeInfo = new Ninfo("0001");
        Ninfo nodeInfo1 = new Ninfo("aaaa");
        boolean nullpointer = false;
        try{
            sList2.getFirst();
        }catch (NoSuchElementException e){
            nullpointer = true;
        }
        assert (sList.getFirst().equals(nodeInfo));
        assert (sList1.getFirst().equals(nodeInfo1));
        assertTrue(nullpointer);
    }
}