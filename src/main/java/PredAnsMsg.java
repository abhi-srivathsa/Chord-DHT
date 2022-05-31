public class PredAnsMsg extends Message {
    private final Ninfo predecessor;
    private PredException predecessorException;


    public PredAnsMsg(Ninfo dest, Ninfo predecessor, Ninfo source, int ticket, boolean exception ) {
        super(6, false, dest, source);
        this.id = ticket;
        this.predecessor = predecessor;
        if (exception){
            this.predecessorException = new PredException();
        }
        else {
            this.predecessorException = null;
        }
    }

    public Ninfo getPredecessor() {
        return predecessor;
    }

    public void checkPredecessorException() throws PredException {
        if (this.predecessorException != null){
            throw this.predecessorException;
        }
    }




}
