import java.awt.*;
import java.io.*;
import java.net.Socket;

public class TCPClientThread extends GameServerThreads {
    String serverIP;
    public TCPClientThread(String serverIP, GameWindow game) {

        this.game = game;
        this.console = game.console;

        this.serverIP = serverIP;
    }

    @Override
    public void run() {
        //Socket soc;
        console.Print("Connecting to " + serverIP + "...");
        try(Socket soc = new Socket(serverIP, 8760)) {
            console.Print("Connected to the host.", Color.ORANGE);

            in = soc.getInputStream();
            out = soc.getOutputStream();

            dis = new DataInputStream(in);
            dos = new DataOutputStream(out);
            ois = new ObjectInputStream(in);
            oos = new ObjectOutputStream(out);

            //map
            console.Print("waiting for board information...", Color.YELLOW);
            GameInfo info = null;
            try {
                info = (GameInfo) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            game.NewClientBoard(info);
            console.Print("Done.", Color.ORANGE);
            game.SetMPGame();
            //
            HandleMessages("Server");

        } catch (IOException e) {
            console.Print(e.getMessage(), Color.RED);
            console.Print("Something went wrong with client.\nTerminating...", Color.RED);
            e.printStackTrace();
        }
    }
}
