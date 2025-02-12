package Backend.Items;
import Backend.Characters.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Item {
    String name;
    BufferedImage sprite;
    String spritePath;

    /**
     * Abstract constructor for item. Needs to be an HpItem or StatusEffect eventually
     * @param name Name of the item
     * @param spritePath Path of the item's image
     */
    public Item(String name, String spritePath) {
        this.name = name;
        this.spritePath = spritePath;
        // try to set item's image, using a default if the path is invalid
        try {
            if (spritePath == null || spritePath.isEmpty()) {
                spritePath = "./src/Backend/Images/itemSprites/default.png";
            }
            this.sprite = ImageIO.read(new File(spritePath));
        } catch (IOException e) {
            System.out.println("Warning: Could not load weapon sprite for " + name + ". Using null sprite.");
            this.sprite = null;
        }
    }


    public BufferedImage getSprite () {
        return sprite;
    }

    public String getSpritePath () {
        return spritePath;
    }

    public abstract void useItem(CharacterManager character);

    public String getName() {return name;}
    abstract public String toString();
}
