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
     * @param weapon Gives a weapon to an enemy
     * @param name Gives a name to an enemy that we can later reference
     * @param ownedItems Gives some items to enemy
     * @param gp Gives a gamePanel in which we draw on
     * @param path Gives a path that we later reference for collision and battle purposes on Hero.pickUpObject.
     * @param worldX Coordinates use for later placement on the map.
     * @param worldY Coordinates use for later placement on the map.
     */
    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems, GamePanel gp, String path, int worldX, int worldY){
        super(stats, weapon, name);
        this.gp = gp;
        this.ownedItems = ownedItems;
        this.path = path;
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * Used mostly for collision detection with hero.
     */
    public void update() {
        collisionsOn = true;
    }

    /**
     * Gets the image related to an specific enemy.
     * @return
     */
    public BufferedImage getImage() {
        try {
            down1 = ImageIO.read(new File(path));
            return down1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Few getters.
     * @return some attributes we need later
     */
    public String getSpritePath(){
        return path;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}



