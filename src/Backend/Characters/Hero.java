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

public class Hero extends CharacterManager {
    // Game mechanics fields
    private ArrayList<Weapon> ownedWeapons = new ArrayList<>();
    private ArrayList<Item> ownedItems = new ArrayList<>();
    private String name;
    private Stats stats;

    // Display and movement fields
    private GamePanel gp;
    private KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    private BufferedImage img = null;

    /**
     * Create a character - now with Christian's game panel and keyhandler
     */
    public Hero(String name, ArrayList<Weapon> ownedWeapons,
                ArrayList<Item> ownedItems, Stats stats, GamePanel gp, KeyHandler keyH) {

        super(stats, ownedWeapons.get(0), name);

        // Game mechanics initialization
        this.name = name;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;

        // Display and movement initialization
        this.gp = gp;
        this.keyH = keyH;
        this.screenX = gp.screenWidth/2 - (gp.tileSize/2);
        this.screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Initialize collision area
        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Old constructor for display testing, so you don't gotta type all that shit.
     */
    public Hero(GamePanel gp, KeyHandler keyH) {
        super(gp, keyH);
        this.gp = gp;
        this.keyH = keyH;
        this.screenX = gp.screenWidth/2 - (gp.tileSize/2); // needed to add this. bc yellow underline told me to
        this.screenY = gp.screenHeight/2 - (gp.tileSize/2);

        solidArea = new Rectangle();
        solidArea.x = 8;
        solidArea.y = 16;
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues();
        getPlayerImage();
    }


    // Use one of those special milks (items)
    public void useItem(Item item) {
        ownedItems.remove(item);
        item.useItem(this);
    }

    // Display and movement methods
    public void setDefaultValues() {
        worldY = gp.tileSize * 20;
        worldX = gp.tileSize * 20;
        speed = 4;
        direction = "down";
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

    // I just followed the underlined stuff... don't need boolean == here
    public void update() {
        if(keyH.rightPressed || keyH.leftPressed || keyH.upPressed || keyH.downPressed) {
            if(keyH.upPressed) {
                direction = "up";
            } else if(keyH.downPressed) {
                direction = "down";
            } else if(keyH.leftPressed) {
                direction = "left";
            } else if(keyH.rightPressed) {
                direction = "right";
            }

            // Check Tile Collision
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
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }
        }
    }

    public void draw(Graphics g2) {
        BufferedImage img = null;

        switch(direction) {
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

    @Override
    public String toString() {
        return "Hero{" +
                "ownedWeapons=" + ownedWeapons +
                ", ownedItems=" + ownedItems +
                ", name='" + name + '\'' +
                ", stats=" + stats +
                '}';
    }
}