public class FirstSucReqMsg extends Message {

    //I'm asking for the first successor of the dest
    public FirstSucReqMsg(Ninfo dest, Ninfo source) {
        super(5, true, dest, source);
    }

}
