package Backend.ObjectsRendering;

import Backend.Weapons.Weapon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class EnemyRendering extends Superobjects {

    public EnemyRendering(String path, int worldX, int worldY) {
        super(worldX, worldY);
        name = "Enemy";
        try {
            image = ImageIO.read(getClass().getResourceAsStream(path));

            // Scale the image by a factor of 5
            int newWidth = image.getWidth() * 2;
            int newHeight = image.getHeight() * 2;

            // Create a new buffered image with the scaled dimensions
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();
            g2d.drawImage(image, 0, 0, newWidth, newHeight, null);
            g2d.dispose();  // Clean up

            // Set the scaled image to the class's image field
            image = scaledImage;

        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;
    }
}
