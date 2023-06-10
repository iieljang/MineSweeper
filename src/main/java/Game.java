import javax.swing.*;
import java.awt.*;
import java.util.Random;

class GameWindow extends JFrame {
    MinePanel minePanel;
    MenuPanel menuPanel;
    JSplitPane splitPane;
    GameConsole console;
    int cellSize = 20;
    GameWindow(int wSizeX,int wSizeY){
        setSize(wSizeX, wSizeY);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        splitPane = new JSplitPane();
        console = new GameConsole(this);
        splitPane.setRightComponent(console);
        add(splitPane);

        NewMenu();

        setVisible(true);
        splitPane.setDividerLocation(0.70);
        console.Print("Welcome to MineSweeper!\nPlease select game mode.\n", Color.cyan);
    }

    //Place New Menu to left panel
    public void NewMenu() {
        menuPanel = new MenuPanel(this);
        splitPane.setLeftComponent(menuPanel);
        splitPane.setDividerLocation(0.7);
    }
    //Create New SoloPlayer Game
    public void NewSoloGame() {
        minePanel = new MinePanel(cellSize, this, new Random().nextLong());
        splitPane.setLeftComponent(minePanel);
        splitPane.setDividerLocation(0.7);
        console.Print("== NEW GAME ==", Color.cyan);
        revalidate();
    }
    //Place new multiplayer game-- separated for fair game
    public void SetMPGame() {
        splitPane.setLeftComponent(minePanel);
        splitPane.setDividerLocation(0.7);
        console.Print("== NEW GAME ==", Color.cyan);
    }
    //Start new server and create Game(not show)
    public void NewServerGame() {
        minePanel = new MinePanel(cellSize, this, new Random().nextLong());
        console.StartServer();
    }
    //Start new Client with user input as server IP
    public void NewClientGame() {
        String serverIP = JOptionPane.showInputDialog("Enter Host IP(blank for localhost)");
        if (serverIP.equals(""))
            System.out.println("localhost");
        console.StartClient(serverIP);
    }
    //Create New Gameboard with host game information.
    public void NewClientBoard(GameInfo info) {
        minePanel = new MinePanel(cellSize, this, info);
    }

    //return gameboard info
    public GameInfo GetGameInfo() {
        return minePanel.info;
    }
    public String GetGameState() {return minePanel.mineboard.gameState;}
}

public class Game {
    public static void main(String[] args) {
        GameWindow gm = new GameWindow(1200, 800);
        System.out.println(gm.getName());
    }
}