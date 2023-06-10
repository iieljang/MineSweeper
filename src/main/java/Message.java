import java.io.Serializable;

public class Message implements Serializable {
    char type;
    String msg;
    public Message(char type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
