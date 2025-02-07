package Backend.Weapons;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class WeaponsTest {
    public static void main(String[] args) {


        // Due to the way BufferedImage works, need to have a catch IOException e... really stupid
        try {
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            Weapon WindsweptAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
            BufferedImage AxeSprite = WindsweptAxe.getSprite();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}