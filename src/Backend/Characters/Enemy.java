package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;
import Display.GamePanel;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class Enemy extends CharacterManager{
    public int worldX, worldY;
    GamePanel gp;
    String path;
    int actionLockCounter = 0;

    /**
     * Create enemy object
     * @param stats Stats object of enemy
     * @param weapon
     * @param name
     * @param ownedItems
     * @param gp
     * @param path
     * @param worldX
     * @param worldY
     */
    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems, GamePanel gp, String path, int worldX, int worldY){
        super(stats, weapon, name);
        this.gp = gp;
        this.ownedItems = ownedItems;
        this.path = path;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    public Enemy(GamePanel gp, String path){
        super(gp);
        this.gp = gp;
        this.path = path;
        direction = "down";
        speed = 3;
        getImage();
    }
    public void update() {
        collisionsOn = true;
        //gp.collisionChecker.checkTile(this);
    }

    public BufferedImage getImage() {
        try {
            down1 = ImageIO.read(new File(path));
            return down1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setAction() {

        actionLockCounter++;
        if(actionLockCounter == 10){
            Random random = new Random();
            int i = random.nextInt(100);
            if (i == 1){
                direction = "right";
            }
            else if (i == 2){
                direction = "up";
            }
            else if (i == 3){
                direction = "down";
            }
            else if (i  == 4){
                direction = "left";
            }
            else{
                direction = "none";
            }
            actionLockCounter = 0;
        }


    }

    public String getSpritePath(){
        return path;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}



