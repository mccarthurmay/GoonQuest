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
     *
     * @param name       Name of weapon
     * @param dmg        Damage
     * @param effect     Effect (wind, fire, etc)
     * @param crit       Criticals chance (1-100)
     * @param hit        Hit chance (1-100)
     * @param message    Unique message
     * @param spritePath Path of weapon sprite
     */
    public Weapon(String name, double dmg, String effect, double crit, double hit, String message, String spritePath) {
        this.name = name;
        this.dmg = dmg;
        this.effect = effect;
        this.crit = crit;
        this.hit = hit;
        this.message = message;

        try {
            if (spritePath == null || spritePath.isEmpty()) {
                spritePath = "./src/Backend/Images/weaponSprites/default.png";
            }
            this.sprite = ImageIO.read(new File(spritePath));
        } catch (IOException e) {
            System.out.println("Warning: Could not load weapon sprite for " + name + ". Using null sprite.");
            this.sprite = null;
        }
    }

        public double getDamage () {
            return dmg;
        }

        public String getName(){
        return name;
        }

        public double getCritChance () {
            return crit;
        }
        public double getHitChance () {
            return hit;
        }
        public void attack ()
        { // Since we're just printing a special message for attack, seems like we don't even need the other classes.
            System.out.println(message);
        }


        public BufferedImage getSprite () {
            return sprite;
        }

    }

