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

    final int HERO_MAX_HEALTH = 200; // Temporary, will set player max health in hero later
    final int ENEMY_MAX_HEALTH = 500; // Temporary, enemy max health will change
    int currentEnemyHealth = ENEMY_MAX_HEALTH;

    // Health bar settings
    int healthBarWidth = 100;
    int healthBarHeight = 20;


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

        drawHealthBar(g2, playerX, playerY + battleSize + 10, gamePanel.hero.getHP(), HERO_MAX_HEALTH);

    }

    public void drawHealthBar(Graphics g2, int x, int y, double currentHealth, int maxHealth) {
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, healthBarWidth, healthBarHeight);

        int healthWidth = (int)((currentHealth / (double)maxHealth) * healthBarWidth);

        // Draw current health
        g2.setColor(Color.GREEN);
        g2.fillRect(x, y, healthWidth, healthBarHeight);

        // Draw Border
        g2.setColor(Color.WHITE);
        g2.drawRect(x, y, healthWidth, healthBarHeight);

        // Draw health number
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString(currentHealth + "/" + maxHealth, x + healthBarWidth/3, y + 15);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        // Draw hero
        drawHeroBattle(g2);

        // IDK how to get the enemy
        g2.fillRect(enemyX, enemyY, 150, 100);
        drawHealthBar(g2, enemyX, enemyY + 60, currentEnemyHealth, ENEMY_MAX_HEALTH);

        // Battle UI
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2.drawString("In Battle with + Enemy name", getWidth()/2 - 100, 50);


        g2.dispose();
    }
}