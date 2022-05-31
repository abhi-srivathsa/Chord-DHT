import org.junit.Before;
import org.junit.Test;

public class FingerTableTest {
    FingerTable ft1;
    FingerTable ft2;
    FingerTable ft3;

    @Before
    public void setUp() throws Exception {
        String hash = "0000";
        ft1 = new FingerTable(hash);
        ft2 = new FingerTable(hash);
        ft3 = new FingerTable(hash);
        int i  = 0;
        while(i<16){
            Ninfo ninfo = new Ninfo(Helper.computefinger(hash,i));
            ft1.addFinger(ninfo);
            i++;
        }
        i = 0;
        while(i<16){
            Ninfo ninfo= new Ninfo("0000");
            ft2.addFinger(ninfo);
            i++;
        }
        i=0;
        while(i<16){
            Ninfo ninfo=new Ninfo("4000");
            ft3.addFinger(ninfo);
            i++;
        }


        ft1.printTable();
    }



    @Test
    public void addFingerTest(){
        Ninfo ninfo = new Ninfo("eeee");
        ft1.addFinger(ninfo);
        assert (!ft1.containsFinger(ninfo.getHash()));
        assert (ft1.getFingers().size()==16);
        assert (ft1.getNode().equals("0000"));
        int i = 0;
        while(i<16){
            Ninfo ninfo1 = new Ninfo(Helper.computefinger("0000",i));
            assert (ft1.getFingers().get(i).equals(ninfo1));
            i++;
        }
        i = 0;
        while(i<16){
            Ninfo ninfo1 = new Ninfo("0000");
            assert (ft2.getFingers().get(i).equals(ninfo1));
            i++;
        }
        i = 0;
        while(i<16){
            Ninfo ninfo1 = new Ninfo("4000");
            assert (ft3.getFingers().get(i).equals(ninfo1));
            i++;
        }

    }

    @Test
    public void closestPredecessorTest(){

        Boolean failed=false;
        Ninfo ninfo= new Ninfo("0000");
        Ninfo ninfo1= new Ninfo("7000");
        Ninfo ninfo2 = new Ninfo("5000");
        Ninfo ninfo3= new Ninfo("0001");
        Ninfo ninfo4= new Ninfo("8000");
        Ninfo ninfo5= new Ninfo("0100");
        Ninfo ninfo6= new Ninfo("9000");
        Ninfo response = ft1.closestPredecessor(ninfo.getHash());
        Ninfo response1= ft1.closestPredecessor(ninfo1.getHash());
        Ninfo response2= ft1.closestPredecessor(ninfo2.getHash());
        try {
            Ninfo response3 = ft1.closestPredecessor(ninfo3.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        Ninfo response4= ft1.closestPredecessor(ninfo4.getHash());
        Ninfo response5= ft1.closestPredecessor(ninfo5.getHash());
        Ninfo response6= ft1.closestPredecessor(ninfo6.getHash());
        assert  (response.getHash().equals("8000"));
        assert  (response1.getHash().equals("4000"));
        assert  (response2.getHash().equals("4000"));
        assert  (response4.getHash().equals("4000"));
        assert  (response5.getHash().equals("0080"));
        assert  (response6.getHash().equals("8000"));
        failed=false;
        try {
            Ninfo response7 = ft2.closestPredecessor(ninfo.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        failed=false;
        try {
            Ninfo response8 = ft2.closestPredecessor(ninfo1.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        failed= false;
        try {
            Ninfo response9 = ft2.closestPredecessor(ninfo2.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        failed=false;
        try {
            Ninfo response10 = ft2.closestPredecessor(ninfo3.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);

        Ninfo response11= ft3.closestPredecessor(ninfo.getHash());
        Ninfo response12= ft3.closestPredecessor(ninfo1.getHash());
        Ninfo response13= ft3.closestPredecessor(ninfo2.getHash());
        failed=false;
        try {
            Ninfo response14 = ft3.closestPredecessor(ninfo3.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        Ninfo response15= ft3.closestPredecessor(ninfo4.getHash());
        failed=false;
        try {
            Ninfo response16 = ft3.closestPredecessor(ninfo5.getHash());
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        Ninfo response17= ft3.closestPredecessor(ninfo6.getHash());
        assert  (response11.getHash().equals("4000"));
        assert  (response12.getHash().equals("4000"));
        assert  (response13.getHash().equals("4000"));
        assert  (response15.getHash().equals("4000"));
        assert  (response17.getHash().equals("4000"));

        ft1.printTable();
        ft2.printTable();
        ft3.printTable();
    }

    @Test
    public void modifyFingerTest(){
        Ninfo ninfo = new Ninfo("7000");
        ft1.modifyFinger(0,ninfo);
        ft2.modifyFinger(5,ninfo);
        ft3.modifyFinger(15,ninfo);
        assert (!(ft1.containsFinger(Helper.computefinger("0000",0))));
        assert (ft1.getFingers().get(0).getHash().equals("7000"));
        assert (ft2.getFingers().get(5).getHash().equals("7000"));
        int i = 0;
        while(i<5){
           assert (ft2.getFingers().get(i).getHash().equals("0000"));
           i++;
        }
        i=6;
        while(i<16){
            assert (ft2.getFingers().get(i).getHash().equals("0000"));
            i++;
        }
        i=0;
        assert (ft3.getFingers().get(15).getHash().equals("7000"));
       while(i<15){
            assert (ft3.getFingers().get(i).getHash().equals("4000"));
            i++;
        }

        ft1.printTable();
        ft2.printTable();
        ft3.printTable();
    }

    @Test
    public void getFingerTest(){
        ft1.removeFinger(15);
        Ninfo ninfo = new Ninfo("eeee");
        ft1.addFinger(ninfo);
        Ninfo ninfo2 = ft1.getFinger(15);
        assert(ninfo.equals(ninfo2) );
    }

    @Test
    public void completeTest(){
        Boolean failed=false;
        Ninfo ninfo= new Ninfo("3000");
        ft1.modifyFinger(6,ninfo);
        Ninfo response= ft1.closestPredecessor("3100");
        Ninfo response1= ft1.closestPredecessor("2000");
        Ninfo response2= ft1.closestPredecessor("8345");
        Ninfo response3= ft1.closestPredecessor("4100");
        assert (response.getHash().equals("3000"));
        assert (response1.getHash().equals("1000"));
        assert (response2.getHash().equals("8000"));
        assert (response3.getHash().equals("4000"));
        ft1.printTable();
        ft2.printTable();
        ft3.printTable();
        ft2.modifyFinger(0,ninfo);
        Ninfo response4= ft2.closestPredecessor("7000");
        try{
            Ninfo response5= ft2.closestPredecessor("3000");
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert (failed);
        failed=false;
        try{
            Ninfo response6= ft2.closestPredecessor("0010");
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        Ninfo ninfo1= new Ninfo("3100");
        int i  = 4;
        while(i<12){
            ft2.modifyFinger(i,ninfo1);
            i++;
        }

        Ninfo response7= ft2.closestPredecessor("3500");
        Ninfo response8= ft2.closestPredecessor("0000");
        Ninfo response9= ft2.closestPredecessor("3010");
        failed=false;
        try{
            Ninfo response10= ft2.closestPredecessor("3000");
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        failed=false;
        try{
            Ninfo response11= ft2.closestPredecessor("1000");
        }
        catch (NullPointerException e){
            failed=true;
        }
        assert  (failed);
        assert (response4.getHash().equals("3000"));
        assert (response7.getHash().equals("3100"));
        assert (response8.getHash().equals("3100"));
        assert (response9.getHash().equals("3000"));
        ft1.printTable();
        ft2.printTable();
        ft3.printTable();

    }

}
