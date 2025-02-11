package Display;

import javax.swing.JFrame;
public class Main{
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setTitle("2D game");


        GamePanel gamePanel = new GamePanel();

        // Create battle system
        Battle battle = new Battle(gamePanel, window);
        gamePanel.battle = battle;


        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.setupGame();
        gamePanel.startGameThread();

    }

}
