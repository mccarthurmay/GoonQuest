package Backend.Characters;
import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
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
        System.out.println(this.gp);
        this.keyH = keyH;
        this.screenX = gp.screenWidth/2 - (gp.tileSize/2);
        this.screenY = gp.screenHeight/2 - (gp.tileSize/2);

        // Initialize collision area
        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 10;
        solidArea.width = 36;
        solidArea.height = 36;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     * Old constructor for character testing
     */
    public Hero(String name, ArrayList<Weapon> ownedWeapons, ArrayList<Item> ownedItems, Stats stats) {
        super(stats, ownedWeapons.get(0), name);
        this.name = name;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;
        // Default values for screen position
        this.screenX = 0;  // or some other default value
        this.screenY = 0;  // or some other default value

    }
    /**
     * Old constructor for display testing, so you don't gotta type all that shit.
     */
    public Hero(GamePanel gp, KeyHandler keyH) {
        super(gp);
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
        worldY = gp.tileSize * 30;
        worldX = gp.tileSize * 30;
        speed = 20;
        direction = "down";
    }

    public ArrayList<Weapon> getOwnedWeapons(){
        return ownedWeapons;
    }

    public ArrayList<Item> getOwnedItems() {return ownedItems;}



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
            int enemyIndex = gp.collisionChecker.checkEnemy(this, gp.enemy); // check if any enemies are colliding with the player
            // fight enemy!

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
               }
           }



            spriteCounter++;
            if(spriteCounter > 10) {
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }
        }
    }

    public void drawHero(Graphics g2) {
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