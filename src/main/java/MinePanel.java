import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

//Wrapper class for MineBoard placement.
class MinePanel extends JPanel {
    public static GameInfo info;
    MineBoard mineboard;
    GameWindow game;

    //ask user for Board size.
    private int GetBoardSize() {
        String[] options = {"Large", "Medium", "Small"};
        String msg = "Small:  15 x 15\nMedium:  25 x 25\nLarge:  35 x 35";
        int opt = JOptionPane.showOptionDialog(this, msg, "Choose Board Size", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, 1);
        if (opt == JOptionPane.CLOSED_OPTION)
            opt = 2;
        switch (opt) {
            case 2:
                return 15;
            case 1:
                return 25;
            case 0:
                return 35;
            default:
                break;
        }
        return 15;
    }

    //ask user for difficulty
    private float GetDifficulty() {
        String[] options = {"Hard", "Normal", "Easy"};
        int opt = JOptionPane.showOptionDialog(this, "", "Choose Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, null);
        if (opt == JOptionPane.CLOSED_OPTION)
            opt = 2;
        switch (opt) {
            case 2:
                return 0.1f;
            case 1:
                return 0.25f;
            case 0:
                return 0.5f;
            default:
                break;
        }
        return 0.1f;
    }
    MinePanel(int cellSize, GameWindow game, GameInfo info) {
        setLayout(null);
        this.game = game;
        this.info = info;
        //create new mineboard based on user input
        int boardSizeCount = info.size;
        int mineCount = info.mineCount;
        long seed = info.seed;
        mineboard = new MineBoard(seed, boardSizeCount, boardSizeCount, mineCount, cellSize, game);
        add(mineboard);
        addPositionCorrector(cellSize, boardSizeCount);
    }
    MinePanel(int cellSize, GameWindow game, long seed) {
        setLayout(null);
        info = new GameInfo(GetBoardSize(), GetDifficulty(), seed);
        this.game = game;

        //create new mineboard based on user input
        int boardSizeCount = info.size;
        int mineCount = info.mineCount;
        mineboard = new MineBoard(seed, boardSizeCount, boardSizeCount, mineCount, cellSize, game);
        add(mineboard);
        addPositionCorrector(cellSize, boardSizeCount);
    }
    void addPositionCorrector(int cellSize, int boardSizeCount) {
        //manually update mineboard position whenever resized
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int boardSizeX = cellSize * boardSizeCount;
                int boardSizeY = cellSize * boardSizeCount;
                mineboard.setLocation((getSize().width - boardSizeX)/2, (getSize().height - boardSizeY) / 2);
                super.componentResized(e);
            }
        });
    }
}
