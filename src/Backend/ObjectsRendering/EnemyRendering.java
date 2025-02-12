package Backend.ObjectsRendering;

import Backend.Characters.Enemy;
import Backend.Weapons.Weapon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * Extends superObject for some specific attributes and
 * to be able to add this object along weapons, and enemies in a array within asset
 * setter
 */
public class EnemyRendering extends Superobjects {

    /**
     * We re use a lot of the variables from superObjects,
     * @param enemy: it let us get the sprite path for this enemy
     *             and helps with redundancy
     */
    public EnemyRendering(Enemy enemy) {
        super(enemy.worldX, enemy.worldY);

        /**
         * We use name and spritePath to connect the images we draw on the world to
         * an actual enemy object when we collide.
         */
        name = "Enemy";
        spritePath = enemy.getSpritePath();

        /**
         * The next lines of code just focus on loading the image and
         * fixing it size for better display on the screen (scaling)
         */
        try {
            System.out.println();
            image = ImageIO.read(getClass().getResourceAsStream(spritePath.replace("src", "")));

            /**
             * Scale the image by a factor of 5
             */
            int newWidth = image.getWidth() * 2;
            int newHeight = image.getHeight() * 2;

            /**
             * Create a new buffered image with the scaled dimensions
             */
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
            g2d.dispose();  // Clean up

            /**
             *  Set the scaled image to the class's image field
             */
            image = scaledImage;

        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
