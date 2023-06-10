import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MenuPanel extends JPanel {

    JButton spButton;
    JButton hostButton;
    JButton joinButton;
    JLabel mainImage;
    BufferedImage img;

    MenuPanel(GameWindow game){
        setLayout(new BorderLayout());
        spButton = new JButton("PLAY SOLO");
        hostButton = new JButton("INVITE PLAYER");
        joinButton = new JButton("JOIN GAME");

        try {
            img = ImageIO.read(new File("main.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainImage = new JLabel(new ImageIcon(img.getScaledInstance(900, 667, Image.SCALE_SMOOTH)));

        JPanel northPanel = new JPanel();
        spButton.addActionListener(ee -> game.NewSoloGame());
        hostButton.addActionListener(e -> game.NewServerGame());
        joinButton.addActionListener(e -> game.NewClientGame());
        northPanel.add(spButton);
        northPanel.add(hostButton);
        northPanel.add(joinButton);
        add(northPanel, BorderLayout.NORTH);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());
        southPanel.add(mainImage, BorderLayout.CENTER);
        add(southPanel, BorderLayout.CENTER);

        setMinimumSize(new Dimension(10, 10));
        setOpaque(true);
        setBackground(Color.lightGray);
    }
}
