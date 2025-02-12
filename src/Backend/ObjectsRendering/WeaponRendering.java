package Backend.ObjectsRendering;

import Backend.Weapons.Weapon;

import javax.imageio.ImageIO;
import java.io.IOException;

public class WeaponRendering extends Superobjects {

    public WeaponRendering(String path, int worldX, int worldY){
        super(worldX, worldY);
        name  = "Weapon";
        spritePath = path;
        try{
            image = ImageIO.read(getClass().getResourceAsStream(path));

        } catch (IOException e) {
            e.printStackTrace();
        }
        collision = true;

    }
}
