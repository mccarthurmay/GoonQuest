package Backend.ObjectsRendering;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Extends superObject for some specific attributes and
 * to be able to add this object along weapons, and enemies in a array within asset
 * setter
 */
public class ItemRendering extends Superobjects{

    /**
     * We re-use a lot of the variables from superObjects
     * @param path Unlike enemy, we input a string path that marks to an specific image
     *             rather than passing an item object and getting the sprite.
     *             This is done for developer facilitation
     * @param worldX coordinates
     * @param worldY coordinates
     */


    public ItemRendering(String path, int worldX, int worldY){

        /**
         * We use name and spritePath to connect the images we draw on the world to
         * an actual enemy object when we collide.
         */
        super(worldX, worldY);
        name  = "Item";
        spritePath = path;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;

    }
}
