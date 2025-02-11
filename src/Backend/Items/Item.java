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


    public Item(String name, String spritePath) {
        this.name = name;
        this.spritePath = spritePath;
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

    public abstract void useItem(CharacterManager character);

    public String getName() {return name;}
    abstract public String toString();
}
