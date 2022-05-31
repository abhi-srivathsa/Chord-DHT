

import java.util.Map;

public class NotifAnsMsg extends Message {
    private final Map<String, String> files;

    public NotifAnsMsg(Ninfo dest, Ninfo source, Map<String, String> files, int ticket) {
        super(6, false, dest, source);
        this.files = files;
        this.id=ticket;
    }

    public Map<String, String> getFiles() {
        return files;
    }





}
