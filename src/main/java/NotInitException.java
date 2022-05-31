public class NotInitException extends Exception {
    // this exception is used when you are not able to join a Chord
    public NotInitException(String msg){
        super(msg);
    }
}
