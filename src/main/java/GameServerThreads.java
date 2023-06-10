import java.awt.*;
import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

//abstract class for server/client thread.
//Extends Thread
public abstract class GameServerThreads extends Thread{
    GameWindow game;
    GameConsole console;
    InputStream in;
    OutputStream out;
    ObjectInputStream ois;
    ObjectOutputStream oos;

    //Check game state and send 'C' message to opponent if win/lose condition is meet
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

    //Handle user incoming/outgoing messages
    protected synchronized void HandleMessages(String opponent) throws IOException {
        //read in new Thread(if not, readObject() stalls)
        ObjectReaderRunner tr = new ObjectReaderRunner();
        new Thread(tr).start();

        while (true) {
            checkGameState();
            //deque incoming message from reading Thread
            Message msg = tr.ins.poll();
            if (msg != null) {
                if (msg.type == 'S') {
                    //print out normal message
                    console.Print(opponent + ": " + msg.msg, Color.ORANGE);
                } else {
                    //game State related action -> Trigger game function
                    if (msg.msg.equals("W")) {
                        game.minePanel.mineboard.GameOver();
                    } else {
                        game.minePanel.mineboard.GameWin();
                    }
                    break;
                }
            }
            //Send Message
            SendMessage();
        }
    }

    protected synchronized void SendMessage() throws IOException {
        //deque from console outgoing message queue and send 'S' Message.
        Message msg = new Message('S', console.msgQueue.poll());
        if (msg.msg != null) {
            oos.writeObject(msg);
            System.out.println("Sending: " + msg.msg);
        }
    }

    //Message reader class
    protected class ObjectReaderRunner implements Runnable {
        Message message = null;

        //incoming message queue
        ConcurrentLinkedQueue<Message> ins;
        public ObjectReaderRunner() {
            ins = new ConcurrentLinkedQueue<>();
        }

        @Override
        public void run() {
            while(true) {
                try {
                    //wait message to arrive
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
