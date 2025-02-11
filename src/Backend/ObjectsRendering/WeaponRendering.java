package Backend.ObjectsRendering;

import Backend.Weapons.Weapon;

import javax.imageio.ImageIO;
import java.io.IOException;

public class WeaponRendering extends Superobjects {

    public WeaponRendering(Weapon weapon){
        name  = "Sword";
        image = weapon.getSprite();

        collision = true;

        // Add deletion code back into hero, charactermanager
        // Add weapon in one of these.

    }
}
