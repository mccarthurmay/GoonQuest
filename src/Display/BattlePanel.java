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
    private int playerBaseX = 150;
    private int playerBaseY = 300;
    private int enemyBaseX = 625;
    private int enemyBaseY = 125;
    private int playerX = playerBaseX;
    private int playerY = playerBaseY;
    private int enemyX = enemyBaseX;
    private int enemyY = enemyBaseY;

    // Animation states
    private boolean isPlayerAttacking = false;
    private boolean isEnemyAttacking = false;
    private int attackAnimationTicks = 0;
    private final int ATTACK_ANIMATION_DURATION = 10; // 1 second at 60 FPS
    int juttDistance = 30; // Smaller distance

    // UI states
    private final int STATE_WEAPONS = 0;
    private final int STATE_ITEMS = 1;
    private final int STATE_ATTACK = 2;
    private final int STATE_DEFEND = 3;

    private boolean inSubmenu = false;

    private int currentUIState = STATE_WEAPONS;
    private int selectedButtonIndex = 0;
    private Rectangle[] buttons = new Rectangle[4];
    private final String[] buttonLabels = {"Attack", "Weapon", "Defend", "Items"};

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
    private Rectangle BattleUI;
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

            BattleUI = new Rectangle(0, 575, 960, 145);

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


                // Calculate jutt movement
                playerX = (int) (playerBaseX + (juttDistance * progress));
                playerY = (int) (playerBaseY - (juttDistance * progress));

                // Right at reverse, take damage
            } else if (attackAnimationTicks == 5){
                Weapon selectedWeapon = getSelectedWeapon();
                gamePanel.hero.attack(currentEnemy, selectedWeapon);

                // Second half (31-60 ticks)
            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {

                // Calculate progress
                float progress = (float) (attackAnimationTicks - (ATTACK_ANIMATION_DURATION / 2)) / (ATTACK_ANIMATION_DURATION / 2);


                // Move character back to start + down according to progress
                playerX = (int) (playerBaseX + (juttDistance * (1- progress)));
                playerY = (int) (playerBaseY - (juttDistance * (1- progress)));
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

                enemyX = (int) (enemyBaseX - (juttDistance * progress));
                enemyY = (int) (enemyBaseY + (juttDistance * progress));
            } else if (attackAnimationTicks == 5){
                Weapon weapon = currentEnemy.getWeapon();
                currentEnemy.attack(gamePanel.hero, weapon);
                // Second half (31-60 ticks)
            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {
                // Calculate progress
                float progress = (float) (attackAnimationTicks - (ATTACK_ANIMATION_DURATION / 2)) / (ATTACK_ANIMATION_DURATION / 2);
                enemyX = (int) (enemyBaseX - (juttDistance * (1- progress)));
                enemyY = (int) (enemyBaseY + (juttDistance * (1- progress)));
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


    public void drawBattleUI(Graphics2D g2){
        // Main UI background
        g2.setColor(Color.GRAY);  // This is a neat way to shorthand if statements
        g2.fill(BattleUI);
        g2.setColor(Color.WHITE);
        g2.draw(BattleUI);

        // Calculate dimensions
        int uiWidth = BattleUI.width;
        int uiHeight = BattleUI.height;
        // Use thirds
        int buttonSectionWidth = uiWidth/3;
        int listSectionWidth = (uiWidth * 2) / 3;

        // Draw button section background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(BattleUI.x, BattleUI.y, buttonSectionWidth, uiHeight);

        // Button drawing
        int buttonWidth = buttonSectionWidth/2 - 15;
        int buttonHeight = uiHeight / 2 - 15; // - 15 is for padding

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                int i = row * 2 + col;
                int x = BattleUI.x + (col * (buttonWidth + 10)) + 10; // +10 for padding
                int y = BattleUI.y + (row * (buttonHeight + 10)) + 10;

                buttons[i] = new Rectangle(x, y, buttonWidth, buttonHeight);

                g2.setColor(!inSubmenu && i == selectedButtonIndex ? Color.YELLOW : Color.GRAY);
                g2.fill(buttons[i]);

                g2.setColor(Color.WHITE);
                g2.draw(buttons[i]);

                g2.setFont(new Font("Arial", Font.BOLD, 20));
                FontMetrics fm = g2.getFontMetrics();
                int textX = buttons[i].x + (buttons[i].width - fm.stringWidth(buttonLabels[i])) / 2;
                int textY = buttons[i].y + ((buttons[i].height + fm.getAscent()) / 2);
                g2.drawString(buttonLabels[i], textX, textY);
            }
        }
        int contentX = BattleUI.x + buttonSectionWidth + 10;
        int contentY = BattleUI.y + 10;
        int contentWidth = listSectionWidth - 20;
        int contentHeight = uiHeight - 20;

        if (inSubmenu) {
            if (currentUIState == STATE_WEAPONS) {
                drawWeaponsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ITEMS) {
                drawItemsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ATTACK) {
                drawAttackMessage(g2, contentX, contentY, contentWidth, contentHeight);
                performPlayerAttack();
            } else if (currentUIState == STATE_DEFEND) {
                drawDefendMessage(g2, contentX, contentY, contentWidth, contentHeight);
            }
        }



    }

    private void drawWeaponsList(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        int weaponBoxSize = 80;
        int weaponsPerRow = width / (weaponBoxSize + 10);

        for (int i = 0; i < weapons.size(); i++) {
            int row = i / weaponsPerRow;
            int col = i % weaponsPerRow;

            int weaponX = x + col * (weaponBoxSize + 10);
            int weaponY = y + row * (weaponBoxSize + 10);

            g2.setColor(i == selectedWeaponIndex ? Color.YELLOW : Color.WHITE);

            g2.fillRect(weaponX, weaponY, weaponBoxSize, weaponBoxSize);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            String weaponName = weapons.get(i).getName();
            g2.drawString(weaponName, weaponX + 5, weaponY + weaponBoxSize / 2);
        }
    }

    private void drawItemsList(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));

    }

    private void drawAttackMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Select option!", x + 20, y + 20);


    }
    private void drawDefendMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("__ blocked the attack!", x + 20, y + 20);
    }


    private void updateUIState(){
        if (selectedButtonIndex == 0) {
            currentUIState = STATE_ATTACK;
        } else if (selectedButtonIndex == 1) {
            currentUIState = STATE_WEAPONS;
        } else if (selectedButtonIndex == 2) {
            currentUIState = STATE_DEFEND;
        } else if (selectedButtonIndex == 3) {
            currentUIState = STATE_ITEMS;
        }
    }



    private void performPlayerAttack(){
        if (isPlayerTurn && !waitingForAnimation){
            isPlayerAttacking = true;
            attackAnimationTicks = 0;
            waitingForAnimation = true;
            isPlayerTurn = false;
            battleMessage = "Attacking!";

            if (currentEnemy.getHP() <= 0) {
                battleMessage = "Enemy was defeated!";
                return;
            }
        }
        isPlayerTurn = false;
        battleMessage = "Enemy's turn!";

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
        drawBattleUI(g2);
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
        updateAttackAnimation();

        if (!inSubmenu) {
            if (keyH.upPressed) {
                selectedButtonIndex = (selectedButtonIndex + 2) % 4;
                updateUIState();
                keyH.upPressed = false;
            }
            if (keyH.downPressed) {
                selectedButtonIndex = (selectedButtonIndex + 2) % 4;
                updateUIState();
                keyH.downPressed = false;
            }
            if (keyH.leftPressed) {
                selectedButtonIndex = selectedButtonIndex % 2 == 0 ? selectedButtonIndex + 1 : selectedButtonIndex - 1;
                updateUIState();
                keyH.leftPressed = false;
            }
            if (keyH.rightPressed) {
                selectedButtonIndex = selectedButtonIndex % 2 == 0 ? selectedButtonIndex + 1 : selectedButtonIndex - 1;
                updateUIState();
                keyH.rightPressed = false;
            }

            if (keyH.spacePressed) {
                if (selectedButtonIndex == 0) {
                    if (isPlayerTurn && !waitingForAnimation){
                        performPlayerAttack();
                    }
                } else if (selectedButtonIndex == 2) {
                    if (isPlayerTurn && !waitingForAnimation){
                        performEnemyAttack();
                    }
                }
                else {
                    inSubmenu = true;
                    keyH.spacePressed = false;
                }
            }
        }else {
            if(keyH.leftPressed) {
                if (currentUIState == STATE_WEAPONS) {
                    selectPreviousWeapon();
                }
                // Add similar for items when implemented
                keyH.leftPressed = false;
            }
            if(keyH.rightPressed) {
                if (currentUIState == STATE_WEAPONS) {
                    selectNextWeapon();
                }
                // Add similar for items when implemented
                keyH.rightPressed = false;
            }

            // Exit submenu on space
            if(keyH.spacePressed) {
                if (currentUIState == STATE_ATTACK) {
                    performPlayerAttack();
                } else if (currentUIState == STATE_DEFEND) {
                    battleMessage = "Defense stance!";
                    isPlayerTurn = false;
                    performEnemyAttack();
                }
                inSubmenu = false;
                keyH.spacePressed = false;
            }
        }
    }




    public void stopBattleThread(){
        battleThread = null;
    }
}