import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GameConsole extends JPanel {
    GameWindow game;
    private JTextField inputField;
    private JTextPane outputPane;
    private StyledDocument outputDocument;

    Thread serverThread;
    Thread clientThread;
    ConcurrentLinkedQueue<String> msgQueue;

    public void StartServer() {
        msgQueue.clear();
        serverThread = new ServerThread(game);
        serverThread.start();
        Print("Server started...", Color.orange);
    }
    public void StartClient(String serverIP) {
        msgQueue.clear();
        clientThread = new TCPClientThread(serverIP, game);
        clientThread.start();
        Print("Client started...");
    }

    private void createInputField() {
        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String input = inputField.getText();
            Print("You: " + input);
            // Process the input and perform appropriate actions
            handleInput(input);
            inputField.setText("");
        });
        add(inputField, BorderLayout.SOUTH);
    }
    private void createOutputPane() {
        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setOpaque(true);
        outputPane.setBackground(Color.black);
        outputDocument = outputPane.getStyledDocument();

        Style defaultStyle = StyleContext.getDefaultStyleContext().getStyle(
                StyleContext.DEFAULT_STYLE
        );
        StyleConstants.setFontFamily(defaultStyle, "Arial");
        StyleConstants.setFontSize(defaultStyle, 14);
    }

    public GameConsole(GameWindow game) {
        this.game = game;

        // Set up the JFrame
        setLayout(new BorderLayout());

        // Create the output area
        createOutputPane();

        JScrollPane scrollPane = new JScrollPane(outputPane);
        add(scrollPane, BorderLayout.CENTER);

        // Create the input field
        createInputField();

        msgQueue = new ConcurrentLinkedQueue<>();

        // Set the JFrame size and make it visible
        setSize(400, 300);
        setVisible(true);
    }

    private void handleInput(String input) {
        msgQueue.add(input);
    }

    public void Print(String message) {
        Style style = outputDocument.addStyle("coloredText", null);
        StyleConstants.setForeground(style, Color.WHITE);
        try {
            outputDocument.insertString(outputDocument.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    public void Print(String message, Color color) {
        Style style = outputDocument.addStyle("coloredText", null);
        StyleConstants.setForeground(style, color);
        try {
            outputDocument.insertString(outputDocument.getLength(), message + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}

