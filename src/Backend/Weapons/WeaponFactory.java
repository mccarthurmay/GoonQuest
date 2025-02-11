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
        Weapon basicAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
        Weapon basicAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
        Weapon basicAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
        Weapon basicAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");
        Weapon basicAxe = new Weapon("Windswept Axe", 10, "wind", 5,100,"Man, it's windy", "./src/Backend/Images/weaponSprites/default.png");



    }
}