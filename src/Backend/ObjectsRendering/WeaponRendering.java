package Backend.ObjectsRendering;

import Display.Superobjects;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class WeaponRendering  extends Superobjects {

    public WeaponRendering(){
        name  = "Sword";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/Backend/Images/Weapons/Axe/Sprite.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
