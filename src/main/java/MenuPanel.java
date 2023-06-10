import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MenuPanel extends JPanel {

    JButton spButton;
    JButton hostButton;
    JButton joinButton;
    JLabel mainImage;
    BufferedImage img;

    MenuPanel(GameWindow game){
        spButton = new JButton("PLAY SOLO");
        hostButton = new JButton("INVITE PLAYER");
        joinButton = new JButton("JOIN GAME");

        spButton.addActionListener(ee -> game.NewSoloGame());
        hostButton.addActionListener(e -> game.NewServerGame());
        joinButton.addActionListener(e -> game.NewClientGame());

        add(spButton);
        add(hostButton);
        add(joinButton);

        setOpaque(true);
        setBackground(Color.lightGray);
    }
}
