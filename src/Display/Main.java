package Display;

import javax.swing.JFrame;
public class Main{
    public static void main(String[] args) {

        // Initialize the window info
        JFrame window = new JFrame();

        // This let the window properly close when the user clicks on the
        // close ("x") button.
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Blocks the user from making the screen bigger
        window.setResizable(false);

        // Sets a title to the window
        window.setTitle("Goon Quest");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        // Causes this window to be sized to fit the preferred size
        // and layouts of its subcomponents
        window.pack();
        window.setLocationRelativeTo(null);

        // Lets you see the window
        window.setVisible(true);

        // Create battle system
        Battle battle = new Battle(gamePanel, window);
        gamePanel.battle = battle;

        // Load game
        gamePanel.setupGame();
        gamePanel.startGameThread();

    }

}
