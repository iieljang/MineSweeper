import java.io.Serializable;

//Message class for socket transaction;
//'S' for normal string message
//'C' for win/lose information

public class Message implements Serializable {
    char type;
    String msg;
    public Message(char type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
