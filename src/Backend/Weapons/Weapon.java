package Backend.Weapons;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Weapon {
    String name;
    double dmg;
    String effect;
    double crit;
    double hit;
    String message;
    BufferedImage sprite;
// double durability???

    /**
     * Create a weapon
     * @param name Name of weapon
     * @param dmg Damage
     * @param effect Effect (wind, fire, etc)
     * @param crit Criticals chance (1-100)
     * @param hit Hit chance (1-100)
     * @param message Unique message
     * @param spritePath Path of weapon sprite
     */
    public Weapon(String name, double dmg, String effect, double crit, double hit, String message, String spritePath) throws IOException {
        this.name = name;
        this.dmg = dmg;
        this.effect = effect;
        this.crit = crit;
        this.hit = hit;
        this.message = message; // We can make messages unique for every weapon

        if (spritePath == null || spritePath.isEmpty()) {
            spritePath = "./src/Backend/Images/weaponSprites/default.png";  // If "" or null, this is the default sprite. For testing purposes, saves a lot of time.
        }
        this.sprite = ImageIO.read(new File(spritePath));
    }

    public double getDmg(){
        return dmg;
    }
    public void attack(){ // Since we're just printing a special message for attack, seems like we don't even need the other classes.
        System.out.println(message);
    }

    public BufferedImage getSprite() {
        return sprite;
    }

}
