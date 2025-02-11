package Backend.Weapons;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class WeaponFactory {
    public static void main(String[] args) {


        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        Weapon WindsweptAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
        BufferedImage AxeSprite = WindsweptAxe.getSprite();


    }



    public void createWeapons(){
        Weapon battleAxe = new Weapon("Battle Axe", 25, "wind", 8,85,"Man, it's windy", "./src/Backend/Images/Weapons/Axe/Sprite.png");
        Weapon normalAxe = new Weapon("Basic Axe", 8, "wind", 5,85,"Man, it's windy", "./src/Backend/Images/Weapons/AxeTool/Sprite.png");
        Weapon bigSword = new Weapon("Big sword", 28, "wind", 7,85,"Man, it's windy", "./src/Backend/Images/Weapons/BigSword/Sprite.png");
        Weapon club = new Weapon("Club", 18, "wind", 15,99,"Man, it's windy", "./src/Backend/Images/Weapons/Club/Sprite.png");
        Weapon Lance = new Weapon("Knight's lance", 22, "wind", 11,90,"Man, it's windy", "./src/Backend/Images/Weapons/Lance/Sprite.png");
        Weapon katana = new Weapon("Kusanagi-no-tsurugi", 30, "wind", 10,80,"Man, it's windy", "./src/Backend/Images/Weapons/Katana/Sprite.png");
        Weapon hammer = new Weapon("Sledgehammer.", 14, "wind", 12,99,"Man, it's windy", "./src/Backend/Images/Weapons/Hammer/Sprite.png");
        Weapon sword = new Weapon("Dawnbreaker", 20, "wind", 20,100,"Man, it's windy", "./src/Backend/Images/Weapons/Sword/Sprite.png");





    }
}