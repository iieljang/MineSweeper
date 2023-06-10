import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

//class for each cell of minesweeper game board
class MineCell extends JButton {
    Boolean flagged = false; //if this cell is flagged
    Boolean cracked = false; //if this cell is cracked(opened)
    int value = 0; // -1 if there's mine, else surrounding mine count
    MineBoard mb; //parent game board info

    //Constructor for Mine Cell
    MineCell(int size, int posX, int posY, MineBoard mb) {
        this.mb = mb;
        this.setOpaque(true);
        this.setBackground(Color.pink);
        this.setBounds(posX, posY, size, size);
        this.setBorder(new LineBorder(Color.BLACK, 1));
        this.setVisible(true);
        //add click event(Mouse) Listener
        //every click checks winning condition
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case 1:
                        mb.LeftClick(getLocation().x/size, getLocation().y/size);
                        mb.CheckWin();
                        break;
                    case 3:
                        mb.RightClick(getLocation().x/size, getLocation().y/size);
                        mb.CheckWin();
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
//base minesweeper board class
class MineBoard extends JPanel {
    MineCell[][] board;
    int mineNo;
    int cellCountX;
    int cellCountY;
    int mineSize;
    int mineFound = 0;
    Random rand;
    long seed;
    String gameState;
    //below two fields help communication between components.
    GameWindow game;
    GameConsole console;
    public void RightClick(int x, int y){
        //flag if not flagged, unflag if flagged.
        if (!board[x][y].flagged){
            board[x][y].setBackground(Color.red);
            board[x][y].flagged = true;
        } else {
            board[x][y].setBackground(Color.pink);
            board[x][y].flagged = false;
        }
        if (board[x][y].value == -1)
            mineFound += 1;
    }
    public void LeftClick(int x, int y) {
        //flood-fill like mine cracking implementation using recursive algorithm
        if(board[x][y].value == -1) {
            GameOver();
            return;
        }
        board[x][y].setBackground(Color.green);
        board[x][y].cracked = true;


        if(board[x][y].value != 0)
            board[x][y].setText(Integer.toString(board[x][y].value));

        if(board[x][y].value == 0){
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++){
                    if((0 <= i && i < cellCountX) && ( 0 <= j && j < cellCountY)){
                        if(!board[i][j].cracked)
                            LeftClick(i, j);
                        repaint();
                    }
                }
            }
        }
    }

    //Place Mines on the board.
    private void setMines() {
        for (int i = 0; i < mineNo; i++){
            int x = rand.nextInt(cellCountX);
            int y = rand.nextInt(cellCountY);
            //retry til clean cell with no mine is selected.
            while(board[x][y].value == -1){
                x = rand.nextInt(cellCountX);
                y = rand.nextInt(cellCountY);
            }
            board[x][y].value = -1;

            for (int j = x - 1; j <= x + 1; j++) {
                for (int k = y - 1; k <= y + 1; k++){
                    if(!((0 <= j && j < cellCountX) && ( 0 <= k && k < cellCountY)))
                        continue;
                    if(board[j][k].value != -1) {
                        board[j][k].value += 1;
                    }
                }
            }
        }
    }
    public void CheckWin() {
        //check if every non-mine cell is exposed
        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                if (board[i][j].value == -1)
                    continue;
                if(!board[i][j].cracked)
                    return;
            }
        }
        GameWin();
    }
    //Trigger Win function
    public void GameWin() {
        gameState = "W";
        console.Print("YOU WIN!", Color.green);
        int opt = JOptionPane.showConfirmDialog(this, "Play Again?", "YOU WIN!", JOptionPane.YES_NO_OPTION);
        if (opt == 1)
            System.exit(0);
        game.NewMenu();
    }
    //Trigger game over function
    public void GameOver() {
        //attempt to crack mine cell -> game over.
        //reveal all mines
        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                if(board[i][j].value == -1) {
                    board[i][j].setBackground(Color.red);
                    board[i][j].setText("X");
                    repaint();
                }
            }
        }
        gameState = "L";
        console.Print("YOU LOOSE!", Color.RED);
        int opt = JOptionPane.showConfirmDialog(this, "Play Again?", "GAME OVER!", JOptionPane.YES_NO_OPTION);
        if (opt == 1)
            System.exit(0);
        game.NewMenu();
    }
    MineBoard(long seed, int cellCountX, int cellCountY, int mineNo, int mineSize,GameWindow game) {
        this.cellCountX = cellCountX;
        this.cellCountY = cellCountY;
        this.mineNo = mineNo;
        this.mineSize = mineSize;
        this.game = game;
        this.console = game.console;
        this.rand = new Random();
        this.seed = seed;
        rand.setSeed(seed);


        board = new MineCell[cellCountX][cellCountY];
        setLayout(null);

        setSize(mineSize * cellCountX, mineSize * cellCountY);
        setLocation(0, 0);

        //place cells on panel
        for (int i = 0; i < cellCountX; i++) {
            for (int j = 0; j < cellCountY; j++) {
                add(board[i][j] = new MineCell(mineSize, i * mineSize, j * mineSize, this));
            }
        }
        //set mine cells
        setMines();

        setBackground(Color.cyan);
        setVisible(true);
    }
}

