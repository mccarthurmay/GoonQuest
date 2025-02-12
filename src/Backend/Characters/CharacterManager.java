package Backend.Characters;
import Backend.Items.Item;
import Backend.Items.StatusEffect;
import Backend.Weapons.Weapon;
import Display.GamePanel;
import Display.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

abstract public class CharacterManager {

    /**
     * Declaring variable that keep track of player position
     * on the whole map.
     */

    public int worldX, worldY;
    public int speed;

    /**
     * BufferedImage describes an image with an accessible buffer of Image data.
     * We mostly use this to stores images files.
     * In this case, these images files will store different position of our player
     * to give a walking animation effect
     */
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;

    /**
     * Needed to later call our images in a correct order.
     */
    public String direction;

    /**
     * Needed to update our sprite current image to the next one.
     */
    public int spriteCounter = 0;
    public int spriteNum = 1;


    /**
     * Default collision for characters
     */
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);

    public int solidAreaDefaultX, solidAreaDefaultY; 

    public boolean collisionsOn = false;


    /**
     * Initializing some attributes of the CharacterManager class.
     */
    Stats stats;
    Weapon weapon; // Going to give bosses/mobs/minibosses only one weapon vs an arraylist
    String name;
    GamePanel gp;
    KeyHandler keyH;
    boolean guarding = false;

    /**
     * Defines a character object.
     * @param stats Sets of stats that define a character base attack, health, crit chance etc. 
     * @param weapon An object of class Weapon that allows the characters to deal more damage
     * @param name A method to refer to the character object later on.
     */
    public CharacterManager(Stats stats, Weapon weapon, String name){
        this.stats = stats;
        this.weapon = weapon;
        this.name = name;
    }
    public CharacterManager(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
    }
    public CharacterManager(GamePanel gp) {
        this.gp = gp;
    }
    public String getName(){
        return name;
    }

    /**
     * Decides on the damage between two parties.
     * @param foe The character to be attacked
     * @param weapon The weapon used in the attack
     * @return
     */
    public String attack(CharacterManager foe, Weapon weapon){
        Random rand = new Random(); // gets a random seed for a little more fun in battles
        double hitDecider = rand.nextDouble(); // gets random double from 0-1
        double critDecider = rand.nextDouble();// gets random double from 0-1
        double hitVal = 0;
        String status = "";
        if (hitDecider < stats.hitChance){ // if the decided value is lower than the hit chance, hit succeeds. higher hit chance = better odds.
            System.out.println("Attack connects!");
            weapon.attack(); // print out a special message
            if (critDecider < stats.crit){ // if decided value is lower than the crit chance, crit succeeds
                System.out.println("Critical hit! Double damage!");
                hitVal = 2* (stats.attackMod + weapon.getDamage()); // doubles the hit value
                status = "crit";
            }
            else {
               hitVal = stats.attackMod + weapon.getDamage(); // if it doesn't crit, add the attack modifier to the weapon's damage
            }
        } else {
            status = "miss";
        }
        foe.takeDamage(hitVal); // take the damage indicated by hit value
        return status;
    }

    /**
     * Function to trigger guarding
     */
    public void guard(){
        System.out.println(name + " is guarding!");
        guarding = true;
    }

    /**
     * If you're guarding, you should take less damage
     * @param hitVal The damage that will be received
     */
    public void takeDamage(double hitVal){
        if (guarding){ // if you're guarding, decrease damage taken
            System.out.println("Got hit for " + hitVal + ", but was guarding!");
            hitVal = hitVal * .1; // take one tenth of the damage
            stats.HP -= hitVal;
            System.out.println(name + " HP is now " + stats.HP);
            guarding = false; // you're not guarding after you guard and get hit
        }
        System.out.println("hitval: " + hitVal);
        stats.HP -= (hitVal); // decrease health
        System.out.println("Got hit for " + hitVal);
        System.out.println(name + " HP is now " + stats.HP);

    }
    ArrayList<Item> ownedItems = new ArrayList<>();


    /**
     * Creating some getter for later use.
     * @return attributes.
     */
    public double getHP(){
        return stats.getHP();
    }
    public void setHP(Double newHP){
        stats.setHP(newHP);
    }
    public double getDamage(){
        return stats.attackMod;
    }
    public void setDamage(Double newDamage){
        stats.setAttackMod(newDamage);
    }
    public double getCrit(){
        return stats.crit;
    }
    public void setCrit(Double newCrit){
        stats.setCrit(newCrit);
    }
    public double getDefense(){
        return stats.defMod;
    }
    public void setDefense(Double newDefense){
        stats.setDefMod(newDefense);
    }
    public double getHitChance(){
        return stats.hitChance;
    }
    public void setHitChance(Double newHitChance){
        stats.setHitChance(newHitChance);
    }

    public Stats getStats() {
        return stats;
    }


    public String toString() {
        return "EnemyType {" +
                "Name=" + name +
                ", ownedItems=" + ownedItems +
                ", Weapon='" + weapon + '\'' +
                ", stats=" + stats +
                '}';
    }




    // This method just keeps track of our character position and updates it on the Gamepanel.
    // We later override it for more specific use.
    public void update() {
        collisionsOn = false;
        gp.collisionChecker.checkTile(this);
        if (collisionsOn == false) {
            switch (direction) {
                case "up":
                    this.worldY -= speed;
                    break;
                case "down":
                    this.worldY += speed;
                    break;
                case "left":
                    this.worldX -= speed;
                    break;
                case "right":
                    this.worldX += speed;
                    break;
                case "none":
                    break;
            }
        }
        spriteCounter++;
        if(spriteCounter > 10) {
            spriteNum = (spriteNum % 4) + 1;
            spriteCounter = 0;
        }
    }

    /**
     * This method initializes some variable that we defined before, that will
     * be used for walking animation purposes on our hero class.
     */
    public void getPlayerImage() {
        /**
         * Whenever we need to read an images, we need to implement a try and catch
         */
        try {
            down1 = ImageIO.read(new File("src/Backend/Images/sprites/00_NPC_test.png"));
            down2 = ImageIO.read(new File("src/Backend/Images/sprites/01_NPC_test.png"));
            down3 = ImageIO.read(new File("src/Backend/Images/sprites/02_NPC_test.png"));
            down4 = ImageIO.read(new File("src/Backend/Images/sprites/03_NPC_test.png"));

            right1 = ImageIO.read(new File("src/Backend/Images/sprites/04_NPC_test.png"));
            right2 = ImageIO.read(new File("src/Backend/Images/sprites/05_NPC_test.png"));
            right3 = ImageIO.read(new File("src/Backend/Images/sprites/06_NPC_test.png"));
            right4 = ImageIO.read(new File("src/Backend/Images/sprites/07_NPC_test.png"));

            up1 = ImageIO.read(new File("src/Backend/Images/sprites/08_NPC_test.png"));
            up2 = ImageIO.read(new File("src/Backend/Images/sprites/09_NPC_test.png"));
            up3 = ImageIO.read(new File("src/Backend/Images/sprites/10_NPC_test.png"));
            up4 = ImageIO.read(new File("src/Backend/Images/sprites/11_NPC_test.png"));

            left1 = ImageIO.read(new File("src/Backend/Images/sprites/12_NPC_test.png"));
            left2 = ImageIO.read(new File("src/Backend/Images/sprites/13_NPC_test.png"));
            left3 = ImageIO.read(new File("src/Backend/Images/sprites/14_NPC_test.png"));
            left4 = ImageIO.read(new File("src/Backend/Images/sprites/15_NPC_test.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}