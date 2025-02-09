package Display;

import javax.swing.JPanel;
import java.awt.*;

public class BattlePanel extends JPanel {

    GamePanel gamePanel; // copy original panel

    public BattlePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Set up the battle panel properties
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setBackground(Color.BLACK); // black because I didn't look at your map code
        this.setDoubleBuffered(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // Add battle rendering

        g2.dispose();
    }
}