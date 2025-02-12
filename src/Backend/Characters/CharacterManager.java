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

    // Trying some stuff
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea = new Rectangle(0, 0, 48, 48); // default collision for characters

    public int solidAreaDefaultX, solidAreaDefaultY;

    public boolean collisionsOn = false;


    // Parent
    Stats stats;
    Weapon weapon; // Going to give bosses/mobs/minibosses only one weapon vs an arraylist
    String name;
    GamePanel gp;
    KeyHandler keyH;
    boolean guarding = false;

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

    public String attack(CharacterManager foe, Weapon weapon){
        Random rand = new Random();
        double hitDecider = rand.nextDouble();
        double critDecider = rand.nextDouble();
        double hitVal = 0;
        String status = "";
        if (hitDecider < stats.hitChance){
            System.out.println("Attack connects!");
            weapon.attack(); // print out a special message
            if (critDecider < stats.crit){
                System.out.println("Critical hit! Double damage!");
                hitVal = 2* (stats.attackMod + weapon.getDamage());
                status = "crit";
            }
            else {
               hitVal = stats.attackMod + weapon.getDamage();
            }
        } else {
            status = "miss";
        }
        foe.takeDamage(hitVal);
        return status;
    }

    public void guard(){
        System.out.println(name + " is guarding!");
        guarding = true;
    }

    public void takeDamage(double hitVal){
        if (guarding){
            System.out.println("Got hit for " + hitVal + ", but was guarding!");
            hitVal = hitVal * .1;
            stats.HP -= hitVal;
            System.out.println(name + " HP is now " + stats.HP);
            guarding = false;
        }
        System.out.println("hitval: " + hitVal);
        stats.HP -= (hitVal);
        System.out.println("Got hit for " + hitVal);
        System.out.println(name + " HP is now " + stats.HP);

    }
    ArrayList<Item> ownedItems = new ArrayList<>();



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

    // Use this like "if Hero attacks, reduce buff. else if hero blocks, don't use this fucking command"
    public void reduceBuff(StatusEffect buff) {
        buff.reduceDuration(this);
    }
    // I think this would be used every round. Poison goes away in a linear fashion, damage reduction goes away in linear fashion
    public void reduceDebuff(StatusEffect debuff) {
        debuff.reduceDuration(this);
    }

    public String toString() {
        return "EnemyType {" +
                "Name=" + name +
                ", ownedItems=" + ownedItems +
                ", Weapon='" + weapon + '\'' +
                ", stats=" + stats +
                '}';
    }

    public void setAction() {

    }
    public void update() {
        setAction();
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

    public void getPlayerImage() {
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