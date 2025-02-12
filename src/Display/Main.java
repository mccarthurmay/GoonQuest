package Display;

import javax.swing.JFrame;
public class Main{
    public static void main(String[] args) {

        // Initialize the window info
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Sets a title to the window
        window.setTitle("Goon Quest");
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);


        // Create battle system
        Battle battle = new Battle(gamePanel, window);
        gamePanel.battle = battle;

        // Load game
        gamePanel.setupGame();
        gamePanel.startGameThread();

    }

}
