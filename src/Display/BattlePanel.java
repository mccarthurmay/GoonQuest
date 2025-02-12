package Display;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import Backend.Weapons.Weapon;
import java.util.ArrayList;
import Backend.Characters.Enemy;
import Backend.Items.*;
public class BattlePanel extends JPanel implements Runnable {

    GamePanel gamePanel;
    private JFrame window;
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
    private int selectedItemIndex = 0;
    private Rectangle[] buttons = new Rectangle[4];
    private final String[] buttonLabels = {"Attack", "Weapon", "Defend", "Items"};

    // UI Message
    private int frameCount = 0;
    private final int TEXT_SPEED = 2; // higher is slower, default 3
    private Font customFont;
    private boolean isMessageComplete = false;
    private int messageEndDelay = 0;
    private final int MESSAGE_DELAY_TIME = 60; // abt 1 second (60 fps)
    private boolean waitingForMessage = false;
    private boolean enemyDefeated = false;
    private boolean heroDefeated = false;

    // Health bar settings
    private double heroMaxHealth;
    private double enemyMaxHealth;
    int healthBarWidth = 100;
    int healthBarHeight = 20;

    // Weapon fields
    private int selectedWeaponIndex = 0;


    // Battle Metrics
    private Rectangle BattleUI;
    private boolean isPlayerTurn = true;
    private boolean waitingForAnimation = false;

    // Message
    private final ArrayList<String> playerAttackMessage = new ArrayList<>();
    private final ArrayList<String> enemyAttackMessage = new ArrayList<>();
    private final ArrayList<String> playerDefenseMessage = new ArrayList<>();
    private final ArrayList<String> playerCritMessage = new ArrayList<>();
    private final ArrayList<String> enemyCritMessage = new ArrayList<>();
    private final ArrayList<String> playerMissMessage = new ArrayList<>();
    private final ArrayList<String> enemyMissMessage = new ArrayList<>();

    private final Random random = new Random();

    private boolean usedItemThisTurn = false;

    // popups
    private String critPopupText = "CRITICAL!";
    private int critPopupTimer = 0;
    private int critPopupX = 0;
    private int critPopupY = 0;

    private String missPopupText = "MISS!";
    private int missPopupTimer = 0;
    private int missPopupX = 0;
    private int missPopupY = 0;

    private volatile boolean isRunning = true;
    private volatile String currentMessage = "";
    private volatile String targetMessage = "";
    private volatile int charIndex = 0;

    public BattlePanel(GamePanel gamePanel, Enemy enemy, JFrame window) {
        this.gamePanel = gamePanel;
        this.keyH = gamePanel.keyH; // steal gamePanel keyhandler
        this.currentEnemy = enemy;
        this.window = window;

        // Set up the battle panel properties
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setDoubleBuffered(true);

        // Initialize bounce effect
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();

        // Initialize buttons
        BattleUI = new Rectangle(0, 575, 960, 145);

        // Initialize max healths
        heroMaxHealth = gamePanel.hero.getHP();
        enemyMaxHealth = currentEnemy.getHP();

        // Key listeners
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startBattleThread();

        try {
        customFont = Font.createFont(Font.TRUETYPE_FONT,
                        getClass().getResourceAsStream("/Backend/font/undertale.ttf"))
                .deriveFont(22f);
        } catch (Exception e) {
        }

        // Add messages to list
        addMessages();

        // Start message
        showTypewriterText(enemy.getName() + " has transported you to another dimension!");

    }

    private void updateAttackAnimation() {
        if (currentEnemy.getHP() <= 0) {
            // If enemy is defeated, stop any ongoing animations
            isEnemyAttacking = false;
            isPlayerAttacking = false;
            enemyX = enemyBaseX;
            enemyY = enemyBaseY;
            playerX = playerBaseX;
            playerY = playerBaseY;
            if (!enemyDefeated) {
                enemyDefeated = true;
                showTypewriterText("Enemy was defeated!");
            }
            return;
        }
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
                String status = gamePanel.hero.attack(currentEnemy, selectedWeapon);
                if (status.equals("crit")){
                    showTypewriterText(playerCritMessage.get(random.nextInt(playerCritMessage.size())));
                    critPopupX = enemyX + 50;
                    critPopupY = enemyY;
                    critPopupTimer = 60;
                } else if (status.equals("miss")){
                    showTypewriterText(playerMissMessage.get(random.nextInt(playerMissMessage.size())));
                    missPopupX = enemyX + 50;
                    missPopupY = enemyY;
                    missPopupTimer = 60;
                } else {
                    showTypewriterText(playerAttackMessage.get(random.nextInt(playerAttackMessage.size())));
                }

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
                waitingForMessage = true;
            }
            attackAnimationTicks++;

        } else if (isEnemyAttacking) {
            if (attackAnimationTicks < ATTACK_ANIMATION_DURATION / 2) {
                // Calculate progress (0.0-1.0)
                float progress = (float) attackAnimationTicks / (ATTACK_ANIMATION_DURATION / 2);
                enemyX = (int) (enemyBaseX - (juttDistance * progress));
                enemyY = (int) (enemyBaseY + (juttDistance * progress));
            } else if (attackAnimationTicks == 5){
                Weapon weapon = currentEnemy.getWeapon();
                String status = currentEnemy.attack(gamePanel.hero, weapon);
                if (status.equals("crit")){
                    showTypewriterText(enemyCritMessage.get(random.nextInt(enemyCritMessage.size())));
                    critPopupX = playerX + 50;
                    critPopupY = playerY;
                    critPopupTimer = 60;
                } else if (status.equals("miss")){
                    showTypewriterText(enemyMissMessage.get(random.nextInt(enemyMissMessage.size())));
                    missPopupX = playerX + 50;
                    missPopupY = playerY;
                    missPopupTimer = 60;
                } else {
                    showTypewriterText(enemyAttackMessage.get(random.nextInt(enemyAttackMessage.size())));
                }


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
                waitingForMessage = true;
            }

            attackAnimationTicks++;
        }
        if (waitingForMessage && isMessageComplete && messageEndDelay >= MESSAGE_DELAY_TIME) {
            waitingForMessage = false;
            if (!isPlayerTurn && !isEnemyAttacking) {
                performEnemyAttack(); // Start enemy attack after message completes
            } else if (isPlayerTurn) {
                waitingForAnimation = false; // Allow next turn to start
            }
        }
    }

    public void addMessages(){
        // Adding Player attack messages
        playerAttackMessage.add("You strike with all your might!");
        playerAttackMessage.add("Your attack lands with a fierce blow!");
        playerAttackMessage.add("The monster recoils as you slam it!");
        playerAttackMessage.add("A powerful strike pierces the monster's defense!");
        playerAttackMessage.add("You land a solid hit, shaking the creature!");
        playerAttackMessage.add("The ground trembles as you deliver your attack!");
        playerAttackMessage.add("With a mighty swing, you hit the monster!");
        playerAttackMessage.add("Your weapon sings through the air, striking true!");
        playerAttackMessage.add("You deliver a crushing blow to your enemy!");
        playerAttackMessage.add("The monster shudders under your powerful attack!");
        playerAttackMessage.add("You feel the impact as your strike lands!");
        playerAttackMessage.add("Your weapon finds its mark with deadly precision!");
        playerAttackMessage.add("The monster is rocked by the force of your strike!");
        playerAttackMessage.add("You cleave through the air, hitting your target!");
        playerAttackMessage.add("With a battle cry, your attack strikes hard!");
        playerAttackMessage.add("Your weapon crashes into the foe with force!");
        playerAttackMessage.add("You make a swift, brutal attack on the monster!");
        playerAttackMessage.add("The monster crumples under the weight of your blow!");
        playerAttackMessage.add("You strike with relentless fury!");
        playerAttackMessage.add("Your strike lands with a satisfying thud!");

        // Adding Enemy attack messages
        enemyAttackMessage.add("The enemy strikes with brutal force!");
        enemyAttackMessage.add("You feel the sting of the enemy's attack!");
        enemyAttackMessage.add("The monster lunges at you, slashing with claws!");
        enemyAttackMessage.add("The enemy's sword slashes across your armor!");
        enemyAttackMessage.add("A heavy blow from the creature sends you reeling!");
        enemyAttackMessage.add("You dodge just in time, but the monster grazes you!");
        enemyAttackMessage.add("The enemy’s tail whips at you, leaving a mark!");
        enemyAttackMessage.add("The monster slams into you, knocking you back!");
        enemyAttackMessage.add("With terrifying speed, the enemy strikes again!");
        enemyAttackMessage.add("The beast's fiery breath scorches you!");
        enemyAttackMessage.add("The creature's teeth sink deep into your arm!");
        enemyAttackMessage.add("You barely avoid the enemy's crushing blow!");
        enemyAttackMessage.add("The enemy's roar echoes as it swings its weapon!");
        enemyAttackMessage.add("The creature's claws tear through your defenses!");
        enemyAttackMessage.add("The monster's charge hits you with full force!");
        enemyAttackMessage.add("You feel a sharp pain as the enemy's strike lands!");
        enemyAttackMessage.add("The enemy's attack is overwhelming, but you stand strong!");
        enemyAttackMessage.add("The monster’s venomous bite poisons you!");
        enemyAttackMessage.add("The enemy's strike almost knocks you off your feet!");
        enemyAttackMessage.add("With a mighty blow, the enemy crushes your guard!");

        // Adding Player defense messages
        playerDefenseMessage.add("Your defensive technique reduces the attack's effectiveness!");
        playerDefenseMessage.add("You partially redirect the force of the blow!");
        playerDefenseMessage.add("Your battle stance helps minimize the damage!");
        playerDefenseMessage.add("You manage to reduce the impact through proper positioning!");
        playerDefenseMessage.add("Your defensive maneuver lessens the attack's power!");
        playerDefenseMessage.add("Your guard reduces the impact of the enemy's strike!");
        playerDefenseMessage.add("Your defensive stance lessens the blow!");
        playerDefenseMessage.add("You brace yourself, reducing the damage!");
        playerDefenseMessage.add("Your guard stance softens the enemy's attack!");

        //player  crit messages
        playerCritMessage.add("A devastating strike! Your weapon does massive damage!");
        playerCritMessage.add("The stars align as your weapon finds a vital point! ");
        playerCritMessage.add("Your precision is rewarded! Critical damage!");
        playerCritMessage.add("CRITICAL HIT! Your attack channels extraordinary power!");
        playerCritMessage.add("You expose a weakness! Your strike hits with crushing force!");

        // enemy crit messages
        enemyCritMessage.add("The enemy's attack finds your weak spot! A terrible blow!");
        enemyCritMessage.add("CRITICAL! The enemy's attack pierces your guard!");
        enemyCritMessage.add("A devastating hit! Attack hits with doubled force!");
        enemyCritMessage.add("The enemy exploits an opening! Massive damage!");
        enemyCritMessage.add("A perfect hit! Attack connects with brutal efficiency!");


        // player miss message
        playerMissMessage.add("Your attack slices through empty air!");
        playerMissMessage.add("The enemy evades your strike with surprising agility!");
        playerMissMessage.add("Your weapon fails to find its mark!");
        playerMissMessage.add("The enemy shifts away at the last moment!");
        playerMissMessage.add("Your attack goes wide, missing the target!");


        // Enemy miss messages
        enemyMissMessage.add("You deftly dodge the enemy's attack!");
        enemyMissMessage.add("The enemy's strike misses by a hair's breadth!");
        enemyMissMessage.add("Their attack meets nothing but air!");
        enemyMissMessage.add("You skillfully evade the enemy's assault!");
        enemyMissMessage.add("The enemy's attack fails to connect!");

    }




    public void startBattleThread() {
        battleThread = new Thread(this);
        battleThread.start();
    }


    public void drawBattleUI(Graphics2D g2){
        // Main UI background
        g2.setColor(new Color(175, 0, 0));
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

                g2.setColor(!inSubmenu && i == selectedButtonIndex ? Color.DARK_GRAY : Color.GRAY);
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

        drawAttackMessage(g2, contentX, contentY, contentWidth, contentHeight);

        if (inSubmenu) {
            if (currentUIState == STATE_WEAPONS) {
                drawWeaponsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ITEMS) {
                drawItemsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ATTACK) {
                drawAttackMessage(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_DEFEND) {
                drawDefendMessage(g2, contentX, contentY, contentWidth, contentHeight);
            }
        }



    }

    private void drawWeaponsList(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        if (weapons.isEmpty()) return;

        int weaponBoxSize = 50;
        int padding = 27;
        int visibleWeapons = 7;
        float scale = 4.0f;

        int startIndex = Math.max(0, selectedWeaponIndex - (visibleWeapons/2));
        startIndex = Math.min(startIndex, Math.max(0, weapons.size() - visibleWeapons));

        int totalVisibleWidth = Math.min(weapons.size(), visibleWeapons) * (weaponBoxSize + padding) - padding;
        int startX = x + (width - totalVisibleWidth) / 2;
        int startY = y + (height - weaponBoxSize) / 2;

        if (startIndex > 0) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString("<", x + 15, y + height/2+ 10);
        }
        if (startIndex + visibleWeapons < weapons.size()) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString(">", x + width - 25, y + height/2 + 10);
        }


        for (int i = 0; i < Math.min(visibleWeapons, weapons.size() - startIndex); i++) {
            int weaponIndex = startIndex + i;
            int weaponX = startX + i * (weaponBoxSize + padding);
            int weaponY = startY;

            if (weaponIndex == selectedWeaponIndex) {
                weaponY -= 10;  // Fixed lift amount
                g2.setColor(Color.WHITE);
                g2.setFont(customFont.deriveFont(16f));
                String weaponName = weapons.get(weaponIndex).getName();
                FontMetrics metrics = g2.getFontMetrics();
                int textWidth = metrics.stringWidth(weaponName);

                int textX = weaponX + (weaponBoxSize - textWidth) / 2;
                int textY = weaponY + weaponBoxSize + 24 ;
                g2.drawString(weaponName, textX, textY);
            }

            BufferedImage weaponSprite = weapons.get(weaponIndex).getSprite();

            int scaledWidth = (int)(weaponSprite.getWidth() * scale);
            int scaledHeight = (int)(weaponSprite.getHeight() * scale);

            // Center the scaled sprite in the box
            int spriteX = weaponX + (weaponBoxSize - scaledWidth) / 2;
            int spriteY = weaponY + (weaponBoxSize - scaledHeight) / 2;

            g2.drawImage(weaponSprite, spriteX, spriteY, scaledWidth, scaledHeight, null);
        }
    }

    private void drawItemsList(Graphics g2, int x, int y, int width, int height) {
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        ArrayList<Item> items = gamePanel.hero.getOwnedItems();
        if (items.isEmpty()) return;

        int itemBoxSize = 50;
        int padding = 27;
        int visibleItems = 7;



        int startIndex = Math.max(0, selectedItemIndex - (visibleItems/2));
        startIndex = Math.min(startIndex, Math.max(0, items.size() - visibleItems));

        int totalVisibleWidth = Math.min(items.size(), visibleItems) * (itemBoxSize + padding) - padding;
        int startX = x + (width - totalVisibleWidth)/2;
        int startY = y + (height - itemBoxSize) / 2;

        if (startIndex > 0) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString("<", x + 15, y + height/2+ 10);
        }
        if (startIndex + visibleItems < items.size()) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString(">", x + width - 25, y + height/2 + 10);
        }


        for (int i = 0; i < Math.min(visibleItems, items.size() - startIndex); i++) {
            int itemIndex = startIndex + i;
            int itemX = startX + i * (itemBoxSize + padding);
            int itemY = startY;

            if (itemIndex == selectedItemIndex) {
                itemY -= 10;
                g2.setColor(Color.WHITE);
                g2.setFont(customFont.deriveFont(16f));
                String itemName = items.get(itemIndex).getName();
                FontMetrics metrics = g2.getFontMetrics();
                int textWidth = metrics.stringWidth(itemName);

                int textX = itemX + (itemBoxSize - textWidth) / 2;
                int textY = itemY + itemBoxSize + 24 ;
                g2.drawString(itemName, textX, textY);
            }


            int spriteSize = 48;
            BufferedImage itemSprite = items.get(i).getSprite();
            int spriteX = itemX + (itemBoxSize-spriteSize)/2;
            int spriteY = itemY + 5;
            g2.drawImage(itemSprite, spriteX, spriteY, spriteSize, spriteSize, null);


        }
    }



    private void drawAttackMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(customFont);
        g2.drawString(currentMessage , x + 15, y + 25);


    }
    private void drawDefendMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString(currentMessage, x + 20, y + 20);
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

    private void showTypewriterText(String message) {
        targetMessage = message;
        currentMessage = "";
        charIndex = 0;
    }

    private void updateTypewriterText() {
        if (charIndex < targetMessage.length()) {
            frameCount++;
            if (frameCount >= TEXT_SPEED){
                currentMessage = targetMessage.substring(0, charIndex + 1);
                charIndex++;
                frameCount = 0;
                isMessageComplete = false;
                messageEndDelay=0;
            }
        } else {
            isMessageComplete = true;
        if (messageEndDelay < MESSAGE_DELAY_TIME) {
            messageEndDelay++;
        }
        }
    }

    private void performPlayerAttack(){
        if (enemyDefeated){
            return;
        }
        if (isPlayerTurn && !waitingForAnimation){
            isPlayerAttacking = true;
            attackAnimationTicks = 0;
            waitingForAnimation = true;
            isPlayerTurn = false;



        }
    }

    private void performEnemyAttack(){
        if (heroDefeated){
            return;
        }
        isEnemyAttacking = true;
        attackAnimationTicks = 0;
        waitingForAnimation = true;
        showTypewriterText(enemyAttackMessage.get(random.nextInt(enemyAttackMessage.size())));

        usedItemThisTurn = false;
        isPlayerTurn = true;
    }

    public void drawHeroBattle(Graphics2D g2) {
        BufferedImage image = gamePanel.hero.right1;

        int battleSize = gamePanel.tileSize * 2;
        g2.drawImage(image, playerX, playerY, battleSize, battleSize, null);
        drawHealthBar(g2, playerX, playerY + battleSize + 10, gamePanel.hero.getHP(), heroMaxHealth);

        Weapon weapon = getSelectedWeapon();
        if (weapon != null){
            String path = weapon.getSpritePath().replace("./src", "").replace("Sprite.png", "SpriteInHand.png");
            try{
                image = ImageIO.read(getClass().getResourceAsStream(path));

            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedImage weaponSprite = image;
            int weaponX = playerX + battleSize - 75;
            int weaponY = playerY + battleSize/2 + 30;
            float weaponScale = 3.0f;
            int weaponWidth = (int)(weaponSprite.getWidth() * weaponScale);
            int weaponHeight = (int)(weaponSprite.getHeight() * weaponScale);

            g2.drawImage(weaponSprite, weaponX, weaponY, weaponWidth, weaponHeight, null);
        }
    }

    public void drawEnemyBattle(Graphics2D g2) {
        BufferedImage image = currentEnemy.getImage();

        int battleSize = gamePanel.tileSize *4;
        g2.drawImage(image, enemyX, enemyY, battleSize, battleSize, null);
        drawHealthBar(g2, enemyX, enemyY + battleSize + 10, currentEnemy.getHP(), enemyMaxHealth );
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
        g2.drawString((int)currentHealth + "/" + (int)maxHealth, x + healthBarWidth/3, y + 15);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;
        try {
            BufferedImage image = ImageIO.read(new File("src/Backend/Images/Space-Background.jpg"));
            g2.drawImage(image, 0,0,null);
        } catch (IOException e) {
        }

        // Draw hero
        drawHeroBattle(g2);

        // IDK how to get the enemy
        drawEnemyBattle(g2);

        // Draw attack button
        drawBattleUI(g2);


        if (currentEnemy.getHP() <= 0) {
            drawVictoryMessage(g2);
        }
        if (gamePanel.hero.getHP() <= 0) {
            drawDefeatedMessage(g2);
        }

        if (critPopupTimer > 0) {
            g2.setFont(customFont.deriveFont(36f));  // Bigger font
            g2.setColor(Color.RED);
            g2.drawString(critPopupText, critPopupX + 1, critPopupY + 1);
            g2.setColor(Color.RED);
            g2.drawString(critPopupText, critPopupX, critPopupY);
        }

        if (missPopupTimer > 0) {
            g2.setFont(customFont.deriveFont(36f));
            g2.setColor(Color.RED);
            g2.drawString(missPopupText, missPopupX + 1, missPopupY + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(missPopupText, missPopupX, missPopupY);
        }

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

    public void selectNextItem() {
        ArrayList<Item> items = gamePanel.hero.getOwnedItems();
        selectedItemIndex = (selectedItemIndex + 1) % items.size();
    }
    public void selectPreviousItem() {
        ArrayList<Item> items = gamePanel.hero.getOwnedItems();
        selectedItemIndex--;
        if(selectedItemIndex < 0) {
            selectedItemIndex = items.size() - 1;
        }
    }
    private void drawVictoryMessage(Graphics2D g2) {
        String message = "Enemy Defeated!";
        this.enemyDefeated = true;
        g2.setFont(customFont.deriveFont(48f));
        FontMetrics metrics = g2.getFontMetrics();
        int messageWidth = metrics.stringWidth(message);

        // Center of screen
        int x = (getWidth() - messageWidth) / 2;
        int y = getHeight() / 2;

        // Draw shadow/outline effect
        g2.setColor(Color.BLACK);
        g2.drawString(message, x + 2, y + 2);

        // Draw main text
        g2.setColor(Color.YELLOW);
        g2.drawString(message, x, y);
        Timer timer = new Timer(3000, e -> returnToGame());
        timer.setRepeats(false);
        timer.start();


    }

    private void drawDefeatedMessage(Graphics2D g2) {
        String message = "Better luck next time...";
        this.heroDefeated = true;
        g2.setFont(customFont.deriveFont(48f));
        FontMetrics metrics = g2.getFontMetrics();
        int messageWidth = metrics.stringWidth(message);

        // Center of screen
        int x = (getWidth() - messageWidth) / 2;
        int y = getHeight() / 2;

        // Draw shadow/outline effect
        g2.setColor(Color.BLACK);
        g2.drawString(message, x + 2, y + 2);

        // Draw main text
        g2.setColor(Color.YELLOW);
        g2.drawString(message, x, y);
        Timer timer = new Timer(2000, e -> System.exit(0));
        timer.setRepeats(false);
        timer.start();

    }



    @Override
    public void run(){

        /**
         * These following variable help set time intervals that slow down
         * the number of times we update our program per second.
         * We restrict it to only update 60 times per second.
         */
        int FPS = 60;
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        /**
         * As long as this gameThreadExists, it represents the process that
         * is written inside this bracket.
         */
        while(battleThread != null) {

            /**
             * Delta is essentially a timer that signals the computer
             * when to update and redraw the screen.
             * it slowly approaches one and once it reaches a value of one, it signals it. git
             */
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {

                // Updates information as character position
                update();

                // Draw the screen with the updated information
                repaint();
                delta--;
            }

        }
    }

    public void update() {
        updateAttackAnimation();
        updateTypewriterText();

        if (critPopupTimer > 0) {
            critPopupTimer--;
            critPopupY--; // Float up
        }

        if (missPopupTimer > 0) {
            missPopupTimer--;
            missPopupY--; // Float up
        }

        if (!inSubmenu) {
            if (keyH.upArrow) {
                selectedButtonIndex = (selectedButtonIndex + 2) % 4;
                updateUIState();
                keyH.upArrow = false;
            }
            if (keyH.downArrow) {
                selectedButtonIndex = (selectedButtonIndex + 2) % 4;
                updateUIState();
                keyH.downArrow = false;

            }
            if (keyH.leftArrow) {
                selectedButtonIndex = selectedButtonIndex % 2 == 0 ? selectedButtonIndex + 1 : selectedButtonIndex - 1;
                updateUIState();
                keyH.leftArrow = false;
            }
            if (keyH.rightArrow) {
                selectedButtonIndex = selectedButtonIndex % 2 == 0 ? selectedButtonIndex + 1 : selectedButtonIndex - 1;
                updateUIState();
                keyH.rightArrow = false;
            }

            if (keyH.spacePressed) {
                if (selectedButtonIndex == 0) {
                    if (isPlayerTurn && !waitingForAnimation){
                        if (usedItemThisTurn) {
                            showTypewriterText("Cannot attack after using an item!");
                        } else {
                            performPlayerAttack();
                        }
                    }
                } else if (selectedButtonIndex == 2) {
                    if (isPlayerTurn && !waitingForAnimation){
                        showTypewriterText("Defense stance!");
                        isPlayerTurn = false;
                        gamePanel.hero.guard();
                        performEnemyAttack();
                    }
                }
                else {
                    inSubmenu = true;
                    keyH.spacePressed = false;
                }
            }
        }else {
            if(keyH.leftArrow) {
                if (currentUIState == STATE_WEAPONS) {
                    selectPreviousWeapon();
                } else if (currentUIState == STATE_ITEMS) {
                    selectPreviousItem();
                }

                keyH.leftArrow = false;
            }
            if(keyH.rightArrow) {
                if (currentUIState == STATE_WEAPONS) {
                    selectNextWeapon();
                } else if (currentUIState == STATE_ITEMS) {
                    selectNextItem();
                }
                // Add similar for items when implemented
                keyH.rightArrow = false;
            }

            // Exit submenu on space
            if(keyH.spacePressed) {
                inSubmenu = false;
                keyH.spacePressed = false;
            }

            if(keyH.enterPressed) {
                if (currentUIState == STATE_ITEMS) {
                    ArrayList<Item> items = gamePanel.hero.getOwnedItems();
                    if (!items.isEmpty()) {
                        Item selectedItem = items.get(selectedItemIndex);
                        gamePanel.hero.useItem(selectedItem);
                        showTypewriterText("Used " + selectedItem.getName() + "!");
                        usedItemThisTurn = true;
                    }
                }
                inSubmenu = false;
                keyH.enterPressed = false;
            }
        }
    }




    public void stopBattleThread(){
        battleThread = null;
    }

    void cleanupResources() {
        // Stop the battle thread properly
        isRunning = false;
        System.out.println("Battle thread stopped");

        if (battleThread != null) {
            battleThread = null;
        }

        // Reset all state variables
        currentMessage = "";
        targetMessage = "";
        charIndex = 0;

        // Clear any pending animations
        isPlayerAttacking = false;
        isEnemyAttacking = false;
        waitingForAnimation = false;
        waitingForMessage = false;
        inSubmenu = false;

        // Reset message system
        currentMessage = "";
        targetMessage = "";
        charIndex = 0;
        frameCount = 0;
        isMessageComplete = false;
        messageEndDelay = 0;

        // Clear any popups
        critPopupTimer = 0;
        missPopupTimer = 0;
        // Clear any pending keyboard states
        if (keyH != null) {
            keyH.upPressed = false;
            keyH.downPressed = false;
            keyH.leftPressed = false;
            keyH.rightPressed = false;
            keyH.spacePressed = false;
            keyH.enterPressed = false;
            keyH.shiftPressed = false;
        }
    }

    private void returnToGame() {
        // Clean up resources before switching back
        System.out.println(isRunning);
        if (!isRunning){
            return;
        }
        cleanupResources();
        gamePanel.stopMusic();

        window.add(gamePanel);
        window.revalidate();
        window.repaint();

        // Ensure game panel has focus and restart its thread
        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();

        // Remove battle panel and restore game panel
        window.remove(this);

        System.out.println("playin music");
        gamePanel.playMusic(0);




    }
}




