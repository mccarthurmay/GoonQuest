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


public class Hero {

    public int x, y;
    public int speed;

    public int spriteCounter = 0;
    public int spriteNum = 1;


    ArrayList<Weapon> ownedWeapons = new ArrayList<>(); // MAke all acharacters own an arraylist, of only one weapon
    ArrayList<Item> ownedItems = new ArrayList<>();
    Double position;
    String name;
    Stats stats;

    public Hero(String name, Double position, ArrayList<Weapon> ownedWeapons, ArrayList<Item> ownedItems, Stats stats) {
        this.name = name;
        this.position = position;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;
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

    // use item

    public void useItem(Item item, Hero hero){
        ownedItems.remove(item);
        item.useItem(this);
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
    int playerY;
    int playerX;
    int playerSpeed;
    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;
    public String direction;








    /**
     * Christian- I added a new constructor to check display on the screen
     */
    public Hero( GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        setDeaultValues();
        getPlayerImage ();

    }


    public void setDeaultValues(){
        playerY= 100;
        playerX= 100;
        playerSpeed = 4;

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
        if(keyH.upPressed == true) {
            direction = "up";
            playerY -= playerSpeed;
        }else if (keyH.downPressed == true) {
            direction = "down";
            playerY += playerSpeed;
        }else if(keyH.leftPressed == true) {
            direction = "left";
            playerX -= playerSpeed;
        }else if(keyH.rightPressed == true) {
            direction = "right";
            playerX += playerSpeed;
        }
        spriteCounter++;

        if(spriteCounter > 10) {
            if(spriteNum + 1>4){
                spriteNum = 1;
            }else{
            spriteNum++;
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
        g2.drawImage(img, playerX, playerY, gp.tileSize, gp.tileSize, null);
    }

}
