import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends GameServerThreads {

    public ServerThread(GameWindow game) {
        this.game = game;
        this.console = game.console;
    }

    @Override
    public void run() {
        try (ServerSocket ss = new ServerSocket(8760) ){
            console.Print("Waiting for client...", Color.YELLOW);
            Socket soc = ss.accept();
            console.Print("Client connected at: " + soc.getInetAddress().getHostAddress(), Color.ORANGE);

            in = soc.getInputStream();
            out = soc.getOutputStream();

            oos = new ObjectOutputStream(out);
            ois = new ObjectInputStream(in);

            //map
            console.Print("Host creating new game...", Color.YELLOW);
            //send game information to client
            oos.writeObject(game.GetGameInfo());
            console.Print("Done.", Color.ORANGE);
            game.SetMPGame();
            //

            HandleMessages("Client");
        } catch (IOException e) {
            console.Print(e.getMessage(), Color.RED);
            console.Print("Something went wrong with server.\nTerminating...", Color.RED);
            e.printStackTrace();
        }
    }
}
