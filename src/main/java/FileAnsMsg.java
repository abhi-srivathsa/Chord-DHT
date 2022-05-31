
public class FileAnsMsg extends Message {
    private final String file;

    public FileAnsMsg(Ninfo dest, String file, Ninfo source, int ticket){
        super(6, false, dest, source);
        this.file=file;
        this.id=ticket;
    }

    public String getFile() {
        return file;
    }
}
