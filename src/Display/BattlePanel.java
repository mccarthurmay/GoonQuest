package Display;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BattlePanel extends JPanel {

    GamePanel gamePanel; // copy original panel

    // Battle positions
    int playerX = 100;
    int playerY = 300;
    int enemyX = 500;
    int enemyY = 300;



    public BattlePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        // Set up the battle panel properties
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setBackground(Color.BLACK); // black because I didn't look at your map code
        this.setDoubleBuffered(true);
    }

    public void drawHeroBattle(Graphics2D g2) {
        BufferedImage image = gamePanel.hero.up1; //Sets him looking up

        // Draw hero but big
        int battleSize = gamePanel.tileSize * 3;
        g2.drawImage(image, playerX, playerY, battleSize, battleSize, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // Draw hero
        drawHeroBattle(g2);

        // IDK how to get the enemy
        g2.fillRect(enemyX, enemyY, 150, 100);

        // Battle UI
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2.drawString("In Battle with + Enemy name", getWidth()/2 - 100, 50);


        g2.dispose();
    }
}