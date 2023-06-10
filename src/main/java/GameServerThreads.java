import java.awt.*;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class GameServerThreads extends Thread{
    GameWindow game;
    GameConsole console;
    InputStream in;
    OutputStream out;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectInputStream ois;
    ObjectOutputStream oos;
    protected void checkGameState() throws IOException {
        String stat = game.GetGameState();
        if (stat == null)
            return;
        if (stat.equals("W")) {
            oos.writeObject(new Message('C', "W"));
        } else if (stat.equals("L")) {
            oos.writeObject(new Message('C', "L"));
        }
    }

    protected synchronized void HandleMessages(String opponent) throws IOException {
        ObjectReaderRunner tr = new ObjectReaderRunner();
        new Thread(tr).start();
        while (true) {
            checkGameState();
            Message msg = tr.ins.poll();
            if (msg != null) {
                if (msg.type == 'S') {
                    console.Print(opponent + ": " + msg.msg, Color.ORANGE);
                } else {
                    if (msg.msg.equals("W")) {
                        game.minePanel.mineboard.GameOver();
                    } else {
                        game.minePanel.mineboard.GameWin();
                    }
                    break;
                }
            }
            SendMessage();
        }
    }

    protected synchronized void SendMessage() throws IOException {
        Message msg = new Message('S', console.msgQueue.poll());
        if (msg.msg != null) {
            oos.writeObject(msg);
            System.out.println("Sending: " + msg.msg);
        }
    }
    protected class ObjectReaderRunner implements Runnable {
        Message message = null;
        ConcurrentLinkedQueue<Message> ins;
        public ObjectReaderRunner() {
            ins = new ConcurrentLinkedQueue<>();
        }

        @Override
        public void run() {
            while(true) {
                try {
                    message = (Message) ois.readObject();
                } catch (Exception e) {
                    console.Print(e.getMessage(), Color.RED);
                    e.printStackTrace();
                    return;
                }
                if (message != null){
                    ins.add(message);
                }
            }
        }
    }
}
