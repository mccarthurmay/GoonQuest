package Backend.ObjectsRendering;

import javax.imageio.ImageIO;
import java.io.IOException;

public class WeaponRendering extends Superobjects {

    public WeaponRendering(){
        name  = "Sword";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Backend/Images/Weapons/Axe/Sprite.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
