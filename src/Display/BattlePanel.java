package Display;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import Backend.Weapons.Weapon;
import java.util.ArrayList;

public class BattlePanel extends JPanel implements Runnable {

    GamePanel gamePanel; // copy original panel
    private KeyHandler keyH;
    private Thread battleThread;

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

    // Weapon fields
    private int selectedWeaponIndex = 0;
    private float[] weaponBounceOffsets; // For bouncing animation
    private final int WEAPON_Y = 500; // Position at bottom of screen

    // Animation fields
    private float bounceTime = 0;
    private final float BOUNCE_SPEED = 0.1f;
    private final float BOUNCE_HEIGHT = 10;




    public BattlePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.keyH = gamePanel.keyH; // steal gamePanel keyhandler

        // Set up the battle panel properties
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setBackground(Color.BLACK); // black because I didn't look at your map code
        this.setDoubleBuffered(true);

        // Initialize bounce effect
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        weaponBounceOffsets = new float[weapons.size()];

        // Key listeners
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startBattleThread();

    }

    public void startBattleThread() {
        battleThread = new Thread(this);
        battleThread.start();
    }

    public void drawWeaponSelect(Graphics2D g2) {
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        int startX = getWidth()/2 - (weapons.size() * 60)/2; // Center the weapons

        for(int i = 0; i < weapons.size(); i++) {
            int x = startX + (i * 60);
            int y = WEAPON_Y;

            // Selected weapon bounces up
            if(i == selectedWeaponIndex) {
                float bounce = (float)(Math.sin(bounceTime) * BOUNCE_HEIGHT);  // ngl, had to look this one up becuase I don't know math in java
                y += bounce;
            }


            // CAN CHANGE THIS TO JUST SHOW ICON OF WEAPON

            // Draw weapon box
            g2.setColor(i == selectedWeaponIndex ? Color.YELLOW : Color.WHITE);
            g2.fillRect(x, y, 50, 50);

            // Draw weapon name
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            String weaponName = weapons.get(i).getName();
            g2.drawString(weaponName, x + 5, y + 30);
        }
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

        // Draw weapon selection
        drawWeaponSelect(g2);

        // Battle UI
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2.drawString("In Battle with + Enemy name", getWidth()/2 - 100, 50);



        g2.dispose();
    }

    public void selectNextWeapon(){
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        selectedWeaponIndex = (selectedWeaponIndex + 1) % gamePanel.hero.getOwnedWeapons().size(); // Infinite cycle
    }

    public void selectPreviousWeapon(){
        selectedWeaponIndex--;
        if(selectedWeaponIndex < 0) {
            selectedWeaponIndex = gamePanel.hero.getOwnedWeapons().size() - 1; // Sends to back
        }
    }

    public Weapon getSelectedWeapon() {
        return gamePanel.hero.getOwnedWeapons().get(selectedWeaponIndex);
    }

    @Override
    public void run() {
        int FPS = 60;
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(battleThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    public void update() {
        bounceTime += BOUNCE_SPEED;

        if(keyH.weaponLeft) {
            //System.out.println("Selecting previous weapon");
            selectPreviousWeapon();
            keyH.weaponLeft = false; // Ensures one keypress... don't know if needed
        }
        if(keyH.weaponRight) {
            //System.out.println("Selecting next weapon");
            selectNextWeapon();
            keyH.weaponRight = false;
        }

    }

    public void stopBattleThread(){
        battleThread = null;
    }
}