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

import static Display.GamePanel.isFirstEnemy;

// Runnable allows us to implement Thread objects
public class BattlePanel extends JPanel implements Runnable {

    // Screen settings
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
    private int selectedWeaponIndex = 0;

    // UI Buttons
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

    // Tutorial Message

    private int tutorialStep = 0;
    private static final int TUTORIAL_NAVIGATION = 0;
    private static final int TUTORIAL_SPACE = 1;
    private static final int TUTORIAL_ENTER = 2;
    private static final int TUTORIAL_COMPLETE = 3;

    // Health bar settings
    private double heroMaxHealth;
    private double enemyMaxHealth;
    int healthBarWidth = 100;
    int healthBarHeight = 20;

    // Battle Metrics
    private Rectangle BattleUI;
    private boolean isPlayerTurn = true;
    private boolean waitingForAnimation = false;
    private boolean usedItemThisTurn = false;

    // Message
    private final ArrayList<String> playerAttackMessage = new ArrayList<>();
    private final ArrayList<String> enemyAttackMessage = new ArrayList<>();
    private final ArrayList<String> playerDefenseMessage = new ArrayList<>();
    private final ArrayList<String> playerCritMessage = new ArrayList<>();
    private final ArrayList<String> enemyCritMessage = new ArrayList<>();
    private final ArrayList<String> playerMissMessage = new ArrayList<>();
    private final ArrayList<String> enemyMissMessage = new ArrayList<>();

    // Random initialization
    private final Random random = new Random();

    // Battle popups
    private String critPopupText = "CRITICAL!";
    private int critPopupTimer = 0;
    private int critPopupX = 0;
    private int critPopupY = 0;
    private String missPopupText = "MISS!";
    private int missPopupTimer = 0;
    private int missPopupX = 0;
    private int missPopupY = 0;

    // "Volatility" ensures threads to "skip" reading these values
    private volatile boolean isRunning = true;
    private volatile String currentMessage = "";
    private volatile String targetMessage = "";
    private volatile int charIndex = 0;

    /**
     * Creates battlePanel object that allows for battle to initiate.
     * @param gamePanel gamePanel logic to be created
     * @param enemy Enemy to be in battle with
     * @param window JFrame window to be created
     */
    public BattlePanel(GamePanel gamePanel, Enemy enemy, JFrame window) {
        this.gamePanel = gamePanel;
        this.keyH = gamePanel.keyH; // steal gamePanel keyhandler
        this.currentEnemy = enemy;
        this.window = window;

        // Set up the battle panel properties
        this.setPreferredSize(new Dimension(gamePanel.screenWidth, gamePanel.screenHeight));
        this.setDoubleBuffered(true);

        // Initialize buttons
        BattleUI = new Rectangle(0, 575, 960, 145);

        // Initialize max healths
        heroMaxHealth = gamePanel.hero.getHP();
        enemyMaxHealth = currentEnemy.getHP();

        // Key listeners
        this.addKeyListener(keyH);
        this.setFocusable(true);
        startBattleThread();

        // Create custom font
        try {
        customFont = Font.createFont(Font.TRUETYPE_FONT,
                        getClass().getResourceAsStream("/Backend/font/undertale.ttf"))
                .deriveFont(22f);
        } catch (Exception e) {
        }

        // Add messages to list
        addMessages();

        // Tutorial messages
        if (isFirstEnemy) {
            showTypewriterText("Use arrow keys to navigate the menu!");
        } else {
            showTypewriterText(enemy.getName() + " has transported you to another dimension!");
        }
    }

    /**
     * Attack animation handling - goes through entire attack sequence
     */
    private void updateAttackAnimation() {
        // If enemy is defeated, stop any ongoing animations
        if (currentEnemy.getHP() <= 0) {
            isEnemyAttacking = false;
            isPlayerAttacking = false;
            enemyX = enemyBaseX;
            enemyY = enemyBaseY;
            playerX = playerBaseX;
            playerY = playerBaseY;
            return;
        }


        if (isPlayerAttacking) {

            if (attackAnimationTicks < ATTACK_ANIMATION_DURATION / 2) {
                // First half of animation going towards enemy (0-5 ticks)
                float progress = (float) attackAnimationTicks / (ATTACK_ANIMATION_DURATION / 2); // Calculate progress (0% to 100%)
                // Do "jutt" movement
                playerX = (int) (playerBaseX + (juttDistance * progress));
                playerY = (int) (playerBaseY - (juttDistance * progress));
            } else if (attackAnimationTicks == 5){
                // During middle of animation, do damage to enemy
                Weapon selectedWeapon = getSelectedWeapon();
                String status = gamePanel.hero.attack(currentEnemy, selectedWeapon);

                if (status.equals("crit")){
                    // Print custom "critical attack" message
                    showTypewriterText(playerCritMessage.get(random.nextInt(playerCritMessage.size())));
                    critPopupX = enemyX + 50;
                    critPopupY = enemyY;
                    critPopupTimer = 60;
                } else if (status.equals("miss")){
                    // Print custom "miss attack" message
                    showTypewriterText(playerMissMessage.get(random.nextInt(playerMissMessage.size())));
                    missPopupX = enemyX + 50;
                    missPopupY = enemyY;
                    missPopupTimer = 60;
                } else {
                    // Print regular attack message
                    showTypewriterText(playerAttackMessage.get(random.nextInt(playerAttackMessage.size())));
                }

            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {
                // Second half (6-10 ticks)
                float progress = (float) (attackAnimationTicks - (ATTACK_ANIMATION_DURATION / 2)) / (ATTACK_ANIMATION_DURATION / 2);// Calculate progress (0% to 100%)
                // Move character back to start according to progress
                playerX = (int) (playerBaseX + (juttDistance * (1- progress)));
                playerY = (int) (playerBaseY - (juttDistance * (1- progress)));
            } else {
                // Return character to original spot.
                isPlayerAttacking = false;
                playerX = playerBaseX;
                playerY = playerBaseY;
                waitingForMessage = true;
            }
            attackAnimationTicks++; // Move to next frame of animation

        } else if (isEnemyAttacking) {

            // Follows mirrored logic as above
            if (attackAnimationTicks < ATTACK_ANIMATION_DURATION / 2) {
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
            } else if (attackAnimationTicks < ATTACK_ANIMATION_DURATION) {
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

        // Checks for message to be complete, running enemy attack after short delay
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

        // Adding player crit messages
        playerCritMessage.add("A devastating strike! Your weapon does massive damage!");
        playerCritMessage.add("The stars align as your weapon finds a vital point! ");
        playerCritMessage.add("Your precision is rewarded! Critical damage!");
        playerCritMessage.add("CRITICAL HIT! The attack channels extraordinary power!");
        playerCritMessage.add("You expose a weakness! Crushing force was applied!");

        // Adding enemy crit messages
        enemyCritMessage.add("The enemy's attack finds your weakness! A terrible blow!");
        enemyCritMessage.add("CRITICAL! The enemy's attack pierces your guard!");
        enemyCritMessage.add("A devastating hit! Attack hits with doubled force!");
        enemyCritMessage.add("The enemy exploits an opening! Massive damage!");
        enemyCritMessage.add("A perfect hit! Attack connects with brutal efficiency!");

        // Adding player miss message
        playerMissMessage.add("Your attack slices through empty air!");
        playerMissMessage.add("The enemy evades your strike with surprising agility!");
        playerMissMessage.add("Your weapon fails to find its mark!");
        playerMissMessage.add("The enemy shifts away at the last moment!");
        playerMissMessage.add("Your attack goes wide, missing the target!");

        // Adding enemy miss messages
        enemyMissMessage.add("You deftly dodge the enemy's attack!");
        enemyMissMessage.add("The enemy's strike misses by a hair's breadth!");
        enemyMissMessage.add("Their attack meets nothing but air!");
        enemyMissMessage.add("You skillfully evade the enemy's assault!");
        enemyMissMessage.add("The enemy's attack fails to connect!");

    }

    /**
     * Starts a thread handling battle animations, inputs, UI, and state
     */
    public void startBattleThread() {
        battleThread = new Thread(this);
        battleThread.start();
    }

    /**
     * Draw Battle UI/buttons
     * @param g2 Input Graphics2D object
     */
    public void drawBattleUI(Graphics2D g2){
        // Main UI background
        g2.setColor(new Color(175, 0, 0));
        g2.fill(BattleUI);
        g2.setColor(Color.WHITE);
        g2.draw(BattleUI);

        // Calculate dimensions
        int uiWidth = BattleUI.width;
        int uiHeight = BattleUI.height;
        int buttonSectionWidth = uiWidth/3;
        int listSectionWidth = (uiWidth * 2) / 3;

        // Draw button section background
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(BattleUI.x, BattleUI.y, buttonSectionWidth, uiHeight);

        // Button drawing
        int buttonWidth = buttonSectionWidth/2 - 15; // -15 for padding
        int buttonHeight = uiHeight / 2 - 15;

        // Create a 2x2 button UI
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                int i = row * 2 + col;
                int x = BattleUI.x + (col * (buttonWidth + 10)) + 10; // +10 for padding
                int y = BattleUI.y + (row * (buttonHeight + 10)) + 10;

                // Draw buttons
                buttons[i] = new Rectangle(x, y, buttonWidth, buttonHeight);
                g2.setColor(!inSubmenu && i == selectedButtonIndex ? Color.DARK_GRAY : Color.GRAY);// If selected and not inSubmenu, change color
                g2.fill(buttons[i]);
                g2.setColor(Color.WHITE);
                g2.draw(buttons[i]);

                g2.setFont(customFont);

                // FontMetrics class calculates text dimensions
                FontMetrics fm = g2.getFontMetrics();
                int textX = buttons[i].x + (buttons[i].width - fm.stringWidth(buttonLabels[i])) / 2;
                int textY = buttons[i].y + ((buttons[i].height + fm.getAscent()) / 2);
                g2.drawString(buttonLabels[i], textX, textY);
            }
        }

        // Calculates position and size of content area
        int contentX = BattleUI.x + buttonSectionWidth + 10;
        int contentY = BattleUI.y + 10;
        int contentWidth = listSectionWidth - 20;
        int contentHeight = uiHeight - 20;

        // Always show attack message in content area
        drawAttackMessage(g2, contentX, contentY, contentWidth, contentHeight);

        if (inSubmenu) {
            if (currentUIState == STATE_WEAPONS) {
                // Show scrollable weapon area
                drawWeaponsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ITEMS) {
                // Show scrollable item area
                drawItemsList(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_ATTACK) {
                // Show attack message
                drawAttackMessage(g2, contentX, contentY, contentWidth, contentHeight);
            } else if (currentUIState == STATE_DEFEND) {
                // Show defend message
                drawDefendMessage(g2, contentX, contentY, contentWidth, contentHeight);
            }
        }
    }

    /**
     * Draw scrollable weapon list onto UI
     */
    private void drawWeaponsList(Graphics g2, int x, int y, int width, int height){
        // Fill default rectangle for weapons to be drawn on
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        // Initialize ArrayList each time weapon list is drawn
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        if (weapons.isEmpty()) return; // If no weapons are present, end here

        // Set parameters
        int weaponBoxSize = 50;
        int padding = 27;
        int visibleWeapons = 7;
        float scale = 4.0f;

        // Set left-most visible item
        int startIndex = Math.max(0, selectedWeaponIndex - (visibleWeapons/2));
        startIndex = Math.min(startIndex, Math.max(0, weapons.size() - visibleWeapons));

        // Calculate the total width of the visible weapons
        int totalVisibleWidth = Math.min(weapons.size(), visibleWeapons) * (weaponBoxSize + padding) - padding;

        // Center list horizontally and vertically
        int startX = x + (width - totalVisibleWidth) / 2;
        int startY = y + (height - weaponBoxSize) / 2;

        // Show left arrow when first index on the left is not visible
        if (startIndex > 0) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString("<", x + 15, y + height/2+ 10);
        }

        // Show right arrow when last index on the right is not visible
        if (startIndex + visibleWeapons < weapons.size()) {
            g2.setColor(Color.WHITE);
            g2.setFont(customFont.deriveFont(28f));
            g2.drawString(">", x + width - 25, y + height/2 + 10);
        }

        // Loop through visible weapons
        for (int i = 0; i < Math.min(visibleWeapons, weapons.size() - startIndex); i++) {
            // Position calculation
            int weaponIndex = startIndex + i;
            int weaponX = startX + i * (weaponBoxSize + padding);
            int weaponY = startY;

            // If weapon is selected
            if (weaponIndex == selectedWeaponIndex) {
                weaponY -= 10;  // Raise weapon by 10 pixels

                // Print weapon name
                g2.setColor(Color.WHITE);
                g2.setFont(customFont.deriveFont(16f));
                String weaponName = weapons.get(weaponIndex).getName();
                FontMetrics metrics = g2.getFontMetrics();
                int textWidth = metrics.stringWidth(weaponName);
                int textX = weaponX + (weaponBoxSize - textWidth) / 2;
                int textY = weaponY + weaponBoxSize + 24 ;
                g2.drawString(weaponName, textX, textY);
            }

            // Get weapon sprite and scale image
            BufferedImage weaponSprite = weapons.get(weaponIndex).getSprite();
            int scaledWidth = (int)(weaponSprite.getWidth() * scale);
            int scaledHeight = (int)(weaponSprite.getHeight() * scale);

            // Center the scaled sprite in the box
            int spriteX = weaponX + (weaponBoxSize - scaledWidth) / 2;
            int spriteY = weaponY + (weaponBoxSize - scaledHeight) / 2;

            g2.drawImage(weaponSprite, spriteX, spriteY, scaledWidth, scaledHeight, null);
        }
    }

    /**
     * Draw scrollable weapon list onto UI.
     * All logic follows previous method.
     */
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

    /**
     * Draw attack message and background
     */
    private void drawAttackMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(customFont);
        g2.drawString(currentMessage , x + 15, y + 25);
    }

    /**
     * Draw defend message and background
     */
    private void drawDefendMessage(Graphics g2, int x, int y, int width, int height){
        g2.setColor(new Color(40,40,40));
        g2.fillRect(x, y, width, height);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString(currentMessage, x + 20, y + 20);
    }

    /**
     * Update UI state based on which button is selected
     */
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

    /**
     * Print text in typewriter style
     * @param message Message to be printed
     */
    private void showTypewriterText(String message) {
        targetMessage = message;
        currentMessage = "";
        charIndex = 0;
    }

    /**
     * Run through each letter of the text per number of frames
     */
    private void updateTypewriterText() {
        if (charIndex < targetMessage.length()) {
            frameCount++;
            if (frameCount >= TEXT_SPEED) {
                currentMessage = targetMessage.substring(0, charIndex + 1); // Go through each substring per tick
                charIndex++;
                frameCount = 0;
                isMessageComplete = false;
                messageEndDelay = 0;
            }
        } else {
            isMessageComplete = true;
            if (messageEndDelay < MESSAGE_DELAY_TIME) {
                messageEndDelay++;
            } else {
                updateTutorial();
            }
        }
    }

    /**
     * Run tutorial text printing if it is the first enemy encountered
     */
    private void updateTutorial() {
        if (!isFirstEnemy || !isMessageComplete) return;

        if (messageEndDelay >= MESSAGE_DELAY_TIME) {
            switch (tutorialStep) {
                case TUTORIAL_NAVIGATION:
                    showTypewriterText("Press SPACE to use buttons!");
                    tutorialStep = TUTORIAL_SPACE;
                    break;
                case TUTORIAL_SPACE:
                    showTypewriterText("Press ENTER to use items!");
                    tutorialStep = TUTORIAL_ENTER;
                    break;
                case TUTORIAL_ENTER:
                    showTypewriterText(currentEnemy.getName() + " has transported you to another dimension!");
                    tutorialStep = TUTORIAL_COMPLETE;
                    isFirstEnemy = false;
                    break;
            }
        }
    }

    /**
     * Set player attack parameters, ensuring proper attack
     */
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

    /**
     * Set enemy attack parameters, ensuring proper attack
     */
    private void performEnemyAttack(){
        if (heroDefeated){
            return;
        }
        isEnemyAttacking = true;
        attackAnimationTicks = 0;
        waitingForAnimation = true;

        // Reset item restriction logic
        usedItemThisTurn = false;
        isPlayerTurn = true;
    }

    /**
     * Draw hero character and their weapon during battle
     *  @Graphics: A class that has the necessary functions to draw on the screen
     */
    public void drawHeroBattle(Graphics2D g2) {
        // Hero character drawing
        BufferedImage image = gamePanel.hero.right1;
        int battleSize = gamePanel.tileSize * 2;
        g2.drawImage(image, playerX, playerY, battleSize, battleSize, null);

        // Draw health bar under player
        drawHealthBar(g2, playerX, playerY + battleSize + 10, gamePanel.hero.getHP(), heroMaxHealth);

        // Draw weapon on player
        Weapon weapon = getSelectedWeapon();
        if (weapon != null){
            // Replace parts of path to get corresponding png
            String path = weapon.getSpritePath().replace("./src", "").replace("Sprite.png", "SpriteInHand.png");

            try{
                image = ImageIO.read(getClass().getResourceAsStream(path));
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Weapon positioning and scaling
            BufferedImage weaponSprite = image;
            int weaponX = playerX + battleSize - 77;
            int weaponY = playerY + battleSize/2 + 30;
            float weaponScale = 3.0f;
            int weaponWidth = (int)(weaponSprite.getWidth() * weaponScale);
            int weaponHeight = (int)(weaponSprite.getHeight() * weaponScale);

            g2.drawImage(weaponSprite, weaponX, weaponY, weaponWidth, weaponHeight, null);
        }
    }

    /**
     * Draw enemy character
     *  @Graphics: A class that has the necessary functions to draw on the screen
     */
    public void drawEnemyBattle(Graphics2D g2) {
        BufferedImage image = currentEnemy.getImage();
        int battleSize = gamePanel.tileSize * 4;
        g2.drawImage(image, enemyX, enemyY, battleSize, battleSize, null);
        drawHealthBar(g2, enemyX, enemyY + battleSize + 10, currentEnemy.getHP(), enemyMaxHealth );
    }

    /**
     * Draw health bar of characters
     *  @Graphics: A class that has the necessary functions to draw on the screen
     */
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

    /**
     * Build in method in java, standard method to draw on JPanel
     * @Graphics: A class that has the necessary functions to draw on the screen
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        // Draw background
        try {
            BufferedImage image = ImageIO.read(new File("src/Backend/Images/Space-Background.jpg"));
            g2.drawImage(image, 0,0,null);
        } catch (IOException e) {
        }

        // Draw hero
        drawHeroBattle(g2);

        // Draw enemy
        drawEnemyBattle(g2);

        // Draw attack button
        drawBattleUI(g2);

        // Check if enemy is dead
        if (currentEnemy.getHP() <= 0) {
            drawVictoryMessage(g2);
        }

        // Check if hero is dead
        if (gamePanel.hero.getHP() <= 0) {
            drawDefeatedMessage(g2);
        }

        // Handle "crit" message animation
        if (critPopupTimer > 0) {
            g2.setFont(customFont.deriveFont(36f));  // Bigger font
            g2.setColor(Color.RED);
            g2.drawString(critPopupText, critPopupX + 1, critPopupY + 1);
            g2.setColor(Color.RED);
            g2.drawString(critPopupText, critPopupX, critPopupY);
        }

        // Handle "miss" message animation
        if (missPopupTimer > 0) {
            g2.setFont(customFont.deriveFont(36f));
            g2.setColor(Color.RED);
            g2.drawString(missPopupText, missPopupX + 1, missPopupY + 1);
            g2.setColor(Color.WHITE);
            g2.drawString(missPopupText, missPopupX, missPopupY);
        }

        g2.dispose();
    }

    /**
     * Select next weapon in list
     */
    public void selectNextWeapon(){
        ArrayList<Weapon> weapons = gamePanel.hero.getOwnedWeapons();
        selectedWeaponIndex = (selectedWeaponIndex + 1) % weapons.size(); // Infinite cycle
    }

    /**
     * Select last weapon in list
     */
    public void selectPreviousWeapon(){
        selectedWeaponIndex--;
        if(selectedWeaponIndex < 0) {
            selectedWeaponIndex = gamePanel.hero.getOwnedWeapons().size() - 1; // Sends to back
        }
    }

    public Weapon getSelectedWeapon() {
        return gamePanel.hero.getOwnedWeapons().get(selectedWeaponIndex);
    }

    /**
     * Select next item in list
     */
    public void selectNextItem() {
        ArrayList<Item> items = gamePanel.hero.getOwnedItems();
        selectedItemIndex = (selectedItemIndex + 1) % items.size();
    }

    /**
     * Select previous item in list
     */
    public void selectPreviousItem() {
        ArrayList<Item> items = gamePanel.hero.getOwnedItems();
        selectedItemIndex--;
        if(selectedItemIndex < 0) {
            selectedItemIndex = items.size() - 1;
        }
    }

    /**
     * Draw victory message on panel and return to main game panel
     * @Graphics: A class that has the necessary functions to draw on the screen
     */
    private void drawVictoryMessage(Graphics2D g2) {
        // Set message to be printed
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

        // Set timer to return to game
        Timer timer = new Timer(3000, e -> returnToGame());
        timer.setRepeats(false);
        timer.start();
    }

    /**
     * Draw defeated message on panel and exit game if lost.
     * @Graphics: A class that has the necessary functions to draw on the screen
     */
    private void drawDefeatedMessage(Graphics2D g2) {
        // Set message to be printed
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

        // Set timer to exit program
        Timer timer = new Timer(2000, e -> System.exit(0));
        timer.setRepeats(false);
        timer.start();
    }


    /**
     * In this loop, we will constantly be updating the animations and
     * draw the UI on the screen. Also, this function is necessary to use when working with Thread objects.
     * In this method we create a Game loop.
     */
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

    /**
     * This code manages the battle menu and input handling 
     * in a turn-based combat system
     */
    public void update() {
        updateAttackAnimation();
        updateTypewriterText();
        
        // Update floating combat text animations
        if (critPopupTimer > 0) {
            critPopupTimer--;
            critPopupY--; // Float up
        }

        if (missPopupTimer > 0) {
            missPopupTimer--;
            missPopupY--; // Float up
        }

        if (!inSubmenu) {
            // Up/Down arrows cycle between rows
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

            // Left/Right arrows move between columns
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
            
            // Main Menu Actions
            if (keyH.spacePressed) {
                if (selectedButtonIndex == 0) { // Attack
                    if (isPlayerTurn && !waitingForAnimation){
                        if (usedItemThisTurn) {
                            showTypewriterText("Cannot attack after using an item!");
                        } else {
                            performPlayerAttack();
                        }
                    }
                } else if (selectedButtonIndex == 2) { // Defend
                    if (isPlayerTurn && !waitingForAnimation){
                        showTypewriterText("Defense stance!");
                        isPlayerTurn = false;
                        gamePanel.hero.guard();
                        performEnemyAttack();
                    }
                }
                else { // Enter submenu (weapons or items)
                    inSubmenu = true;
                    keyH.spacePressed = false;
                }
            }
        }else { // Once in submenu,
            // Navigate weapons/items with left/right
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
                keyH.rightArrow = false;
            }

            // Exit submenu on space
            if(keyH.spacePressed) {
                inSubmenu = false;
                keyH.spacePressed = false;
            }
            
            // Enter uses selected item
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

    /**
     * Cleanup any pending or saved variables, resetting them for next usage
     */
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

    /**
     * Return to main game panel
     */
    private void returnToGame() {
        // Check if isRunning: returnToGame() was running 60 times before close
        if (!isRunning){
            return;
        }

        // Clean up resources before switching back
        cleanupResources();
        gamePanel.stopMusic();
        
        // Reinitiate game panel
        window.add(gamePanel);
        window.revalidate();
        window.repaint();

        // Ensure game panel has focus and restart its thread
        gamePanel.requestFocusInWindow();
        gamePanel.startGameThread();

        // Remove battle panel and restart music
        window.remove(this);
        gamePanel.playMusic(0);
    }
}




