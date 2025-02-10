package Display;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;
import Backend.Characters.Hero;
import Backend.Characters.HeroFactory;
import Backend.Weapons.Weapon;
import java.util.ArrayList;
import Backend.Characters.Enemy;
import Backend.Characters.EnemyFactory;

public class BattlePanel extends JPanel implements Runnable {

    GamePanel gamePanel; // copy original panel
    private KeyHandler keyH;
    private Thread battleThread;
    private Enemy currentEnemy;

    // Battle positions
    private int playerBaseX = 100;
    private int playerBaseY = 300;
    private int enemyBaseX = 500;
    private int enemyBaseY = 300;
    private int playerX = playerBaseX;
    private int playerY = playerBaseY;
    private int enemyX = enemyBaseX;
    private int enemyY = enemyBaseY;

    // Animation states
    private boolean isPlayerAttacking = false;
    private boolean isEnemyAttacking = false;
    private int attackAnimationTicks = 0;
    private final int ATTACK_ANIMATION_DURATION = 60; // 1 second at 60 FPS
    private final int JUMP_HEIGHT = 100;
    private final int HORIZONTAL_JUMP_DISTANCE = 200;


    private double heroMaxHealth;
    private double enemyMaxHealth;

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

    // Battle Metrics
    private Rectangle attackButton;
    private boolean isPlayerTurn = true;
    private String battleMessage = "ATTACK IT";
    private boolean waitingForAnimation = false;

    // Item UI
    private Rectangle itemButton;

    public BattlePanel(GamePanel gamePanel, Enemy enemy) {
            this.gamePanel = gamePanel;
            this.keyH = gamePanel.keyH; // steal gamePanel keyhandler
            this.currentEnemy = enemy;

            // Set up the battle panel properties
            this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
            this.setBackground(Color.BLACK); // black because I didn't look at your map code
            this.setDoubleBuffered(true);

            // Initialize bounce effect
            ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
            weaponBounceOffsets = new float[weapons.size()];

            // Initialize buttons
            itemButton = new Rectangle(50, 550, 100, 40);
            attackButton = new Rectangle(50, 450, 100, 40);

            // Initialize max healths
            this.heroMaxHealth = gamePanel.hero.getHP();
            this.enemyMaxHealth = currentEnemy.getHP();

            // Key listeners
            this.addKeyListener(keyH);
            this.setFocusable(true);
            startBattleThread();

    }

    private void updateAttackAnimation() {
        if (isPlayerAttacking) {
            // First half of animation going towards enemy (0-30 ticks)
            if (attackAnimationTicks < ATTACK_ANIMATION_DURATION / 2) {
                // Calculate progress (0.0-1.0)
                float progress = (float) attackAnimationTicks / (ATTACK_ANIMATION_DURATION / 2);
                // Calculate jump height (follow the sin wave)
                float jumpHeight = (float) Math.sin(progress * Math.PI) * JUMP_HEIGHT; // bounce animation lol

                // Move character forward + up according to progress
                playerX = (int) (playerBaseX + (HORIZONTAL_JUMP_DISTANCE * progress));
                playerY = (int) (playerBaseY - jumpHeight);

                // Right at reverse, take damage
            } else if (attackAnimationTicks == 30){
                Weapon selectedWeapon = getSelectedWeapon();
                gamePanel.hero.attack(currentEnemy, selectedWeapon);

                // Second half (31-60 ticks)
            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {

                // Calculate progress
                float progress = (float) (attackAnimationTicks - (ATTACK_ANIMATION_DURATION / 2)) / (ATTACK_ANIMATION_DURATION / 2);
                float jumpHeight = (float) Math.sin((1 - progress) * Math.PI) * JUMP_HEIGHT;

                // Move character back to start + down according to progress
                playerX = (int) (playerBaseX + HORIZONTAL_JUMP_DISTANCE * (1 - progress));
                playerY = (int) (playerBaseY - jumpHeight);
            } else {
                isPlayerAttacking = false;
                playerX = playerBaseX;
                playerY = playerBaseY;
                waitingForAnimation = false;
                performEnemyAttack();
            }
            attackAnimationTicks++;

        } else if (isEnemyAttacking) {
            if (attackAnimationTicks < ATTACK_ANIMATION_DURATION / 2) {
                // Calculate progress (0.0-1.0)
                float progress = (float) attackAnimationTicks / (ATTACK_ANIMATION_DURATION / 2);
                // Calculate jump height (follow the sin wave)
                float jumpHeight = (float) Math.sin(progress * Math.PI) * JUMP_HEIGHT; // bounce animation lol

                // Move character forward + up according to progress
                enemyX = (int) (enemyBaseX - (HORIZONTAL_JUMP_DISTANCE * progress));
                enemyY = (int) (enemyBaseY - jumpHeight);
            } else if (attackAnimationTicks == 30){
                Weapon weapon = currentEnemy.getWeapon();
                currentEnemy.attack(gamePanel.hero, weapon);
                // Second half (31-60 ticks)
            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {
                // Calculate progress
                float progress = (float) (attackAnimationTicks - (ATTACK_ANIMATION_DURATION / 2)) / (ATTACK_ANIMATION_DURATION / 2);
                float jumpHeight = (float) Math.sin((1 - progress) * Math.PI) * JUMP_HEIGHT;

                // Move character back to start + down according to progress
                enemyX = (int) (enemyBaseX - HORIZONTAL_JUMP_DISTANCE * (1 - progress));
                enemyY = (int) (enemyBaseY - jumpHeight);
            } else {
                isEnemyAttacking = false;
                enemyX = enemyBaseX;
                enemyY = enemyBaseY;
                waitingForAnimation = false;
            }
            attackAnimationTicks++;
        }

    }




    public void startBattleThread() {
        battleThread = new Thread(this);
        battleThread.start();
    }


    public void drawAttackButton(Graphics2D g2){
        // Button background
        g2.setColor(isPlayerTurn ? Color.RED : Color.GRAY);  // This is a neat way to shorthand if statements
        g2.fill(attackButton);

        // Button border
        g2.setColor(Color.WHITE);
        g2.draw(attackButton);

        // Button Text
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.WHITE);
        String buttonText = "ATTACK (button)- this is not in the right place";

        g2.drawString(buttonText, 50, 400); // in wrong place

    }

    private void performPlayerAttack(){
        if (isPlayerTurn && !waitingForAnimation){


            isPlayerAttacking = true;
            attackAnimationTicks = 0;
            waitingForAnimation = false;
            isPlayerTurn = false;
            battleMessage = "Attacking!";

            if (currentEnemy.getHP() <= 0) {
                battleMessage = "Enemy was defeated!";
                return;
            }
        }
        isPlayerTurn = false;
        battleMessage = "Enemy's turn!";

//        try{
//            TimeUnit.SECONDS.sleep(3);
//        }catch(InterruptedException _){
//        }

        performEnemyAttack();
    }

    private void performEnemyAttack(){


        isEnemyAttacking = true;
        attackAnimationTicks = 0;
        waitingForAnimation = true;
        battleMessage = "Enemy Attacking!";


        if (gamePanel.hero.getHP() <= 0) {
            battleMessage = "You were defeated!";
            return;
        }

        isPlayerTurn = true;
        battleMessage = "ATTACK AGAIN";
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
        BufferedImage image = gamePanel.hero.right1; //Sets him looking up

        // Draw hero but big
        int battleSize = gamePanel.tileSize * 2;
        g2.drawImage(image, playerX, playerY, battleSize, battleSize, null);

        drawHealthBar(g2, playerX, playerY + battleSize + 10, gamePanel.hero.getHP(), heroMaxHealth);

    }


    public void drawHealthBar(Graphics g2, int x, int y, double currentHealth, double maxHealth) {
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
        drawHealthBar(g2, enemyX, enemyY + 60, currentEnemy.getHP(), enemyMaxHealth);

        // Draw weapon selection
        drawWeaponSelect(g2);

        // Draw attack button
        drawAttackButton(g2);
        // Draw item button


        // Battle UI
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2.drawString("In Battle with + Enemy name", getWidth()/2 - 500, 50);

        // Draw battle message
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString(battleMessage, getWidth()/2 - 150, 50);

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

        updateAttackAnimation();

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
        if(keyH.spacePressed) {

            performPlayerAttack();
            keyH.spacePressed = false;
        }

    }

    public void stopBattleThread(){
        battleThread = null;
    }
}