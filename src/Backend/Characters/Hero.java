package Backend.Characters;
import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Hero extends CharacterManager{


    ArrayList<Weapon> ownedWeapons = new ArrayList<>(); // MAke all acharacters own an arraylist, of only one weapon
    ArrayList<Item> ownedItems = new ArrayList<>();
    Double position;
    String name;
    Stats stats;

    /**
     * Create playable hero for game
     * @param name Input name
     * @param position Input position (need to be changed)
     * @param ownedWeapons Input ArrayList of owned weapons (default: Stick)
     * @param ownedItems Input ArrayList of owned items
     * @param stats Input stats of hero
     */
    public Hero(String name, Double position, ArrayList<Weapon> ownedWeapons, ArrayList<Item> ownedItems, Stats stats) {
        super(stats, ownedWeapons.get(0), name);
        this.name = name;
        this.position = position;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;

        // Christian while drew at susssies

        screenX = gp.screenWidth/2;
        screenY = gp.screenHeight/2;

    }

    @Override
    public String toString() {
        return "Hero{" +
                "ownedWeapons=" + ownedWeapons +
                ", ownedItems=" + ownedItems +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", stats=" + stats +
                '}';
    }


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

    // use item

    public void useItem(Item item){
        ownedItems.remove(item);
        item.useItem(this);
    }

    // Will have different situations where a buff should be reduced
    public void reduceBuff(StatusEffect buff){
        buff.reduceDuration(this);
    }
    public void reduceDebuff(StatusEffect debuff){
        debuff.reduceDuration(this);
    }

    // obtain weapon

    // obtain items

    // read stats

    // select weapon  (should this just be done locally in battle?)i

    // A function to update a player's coordinates

    /**
     * Start of map update area for Christian
     */

    // Display and drawings
    GamePanel gp;
    // Moving and changing player's coordinates on the screen
    KeyHandler keyH;


    public final int screenX;
    public final int screenY;
    /**
     * Christian- I added a new constructor to check display on the screen
     */
    public Hero(GamePanel gp, KeyHandler keyH) {
        super(gp, keyH);
        this.gp = gp;
        this.keyH = keyH;

        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);


        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;




        setDefaultValues();
        getPlayerImage ();

    }


    public void setDefaultValues(){
        worldY = gp.tileSize * 20;
        worldX = gp.tileSize * 20;
        speed = 4;

        direction = "down";
    }

    BufferedImage img = null;

    // Can be moved to CharacterManager later on - have each .png match name of NPC so it's universal
    // ex. getPlayerImage(String CharName) would input CharName string value into "CharName".png
    public void getPlayerImage (){
        try{
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

        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void update() {



       if(keyH.rightPressed == true || keyH.leftPressed == true|| keyH.upPressed == true || keyH.downPressed == true){
           if(keyH.upPressed == true) {
               direction = "up";
           }else if (keyH.downPressed == true) {
               direction = "down";
           }else if(keyH.leftPressed == true) {
               direction = "left";
           }else if(keyH.rightPressed == true) {
               direction = "right";
           }

           //Check Tile Collision
           collisionsOn = false;
           gp.collisionChecker.checkTile(this);

           if (collisionsOn == false) {
               switch (direction) {
                   case "up":
                       worldY -= speed;
                       break;
                   case "down":
                       worldY += speed;
                       break;
                   case "left":
                       worldX -= speed;
                       break;
                   case "right":
                       worldX += speed;
                       break;
               }
           }



           spriteCounter++;

           if(spriteCounter > 10) {
               if(spriteNum == 1){
                   spriteNum = 2;
               }else if(spriteNum == 2){
                   spriteNum = 3;
               }else if(spriteNum == 3){
                   spriteNum = 4;
               }else if(spriteNum == 4){
                   spriteNum = 1;
               }
               spriteCounter = 0;

           }
       }
    }

    public void draw(Graphics g2) {
        BufferedImage img = null;

        switch (direction) {
            case "up":
                if(spriteNum == 1){
                    img = up1;
                    break;
                }
                if(spriteNum == 2){
                    img = up2;
                    break;
                }
                if(spriteNum == 3){
                    img = up3;
                    break;
                }
                if(spriteNum == 4){
                    img = up4;
                    break;
                }
            case "down":
                if(spriteNum == 1){
                    img = down1;
                    break;
                }
                if(spriteNum == 2){
                    img = down2;
                    break;
                }
                if(spriteNum == 3){
                    img = down3;
                    break;
                }
                if(spriteNum == 4) {
                    img = down4;
                    break;
                }

            case "left":
                if(spriteNum == 1){
                    img = left1;
                    break;
                }
                if(spriteNum == 2){
                    img = left2;
                    break;
                }
                if(spriteNum == 3){
                    img = left3;
                    break;
                }
                if(spriteNum == 4){
                    img = left4;
                    break;
                }

            case "right":
                if(spriteNum == 1){
                    img = right1;
                    break;
                }
                if(spriteNum == 2){
                    img = right2;
                    break;
                }
                if(spriteNum == 3){
                    img = right3;
                    break;
                }
                if(spriteNum == 4){
                    img = right4;
                    break;
                }

        }
        // Probably move this eventually too
        g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

}
