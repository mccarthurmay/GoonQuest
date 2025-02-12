package Backend.Characters;

import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;
import java.io.File;
import java.io.FileNotFoundException;
import java.security.Key;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class HeroFactory {

    /**
     * Create full default hero, saves some time
     */
    public static ArrayList<Weapon> unfoundWeapons = new ArrayList<>();
    public static ArrayList<Item> unfoundItems = new ArrayList<>();
    public static ArrayList<Enemy> unfoundEnemies = new ArrayList<>();

    public static Hero createDefaultHero(GamePanel gp, KeyHandler keyH) {
        // Create default weapon
        Weapon defaultWeapon = new Weapon("Book", 2, "Basic weapon", 2, 3, "a book ", "./src/Backend/Images/Weapons/Book/Sprite.png");
        Weapon battleAxe = new Weapon("Battle Axe", 25, "wind", 8, 85, "Man, it's windy", "./src/Backend/Images/Weapons/Axe/Sprite.png");
        Weapon normalAxe = new Weapon("Basic Axe", 8, "wind", 5, 85, "Man, it's windy", "./src/Backend/Images/Weapons/AxeTool/Sprite.png");
        Weapon bigSword = new Weapon("Big sword", 28, "wind", 7, 85, "Man, it's windy", "./src/Backend/Images/Weapons/BigSword/Sprite.png");
        Weapon club = new Weapon("Club", 18, "wind", 15, 99, "Man, it's windy", "./src/Backend/Images/Weapons/Club/Sprite.png");
        Weapon Lance = new Weapon("Knight's lance", 22, "wind", 11, 90, "Man, it's windy", "./src/Backend/Images/Weapons/Lance/Sprite.png");
        Weapon katana = new Weapon("Kusanagi-no-tsurugi", 30, "wind", 10, 80, "Man, it's windy", "./src/Backend/Images/Weapons/Katana/Sprite.png");
        Weapon hammer = new Weapon("Sledgehammer.", 14, "wind", 12, 99, "Man, it's wind1y", "./src/Backend/Images/Weapons/Hammer/Sprite.png");
        Weapon sword = new Weapon("Dawnbreaker", 20, "wind", 20, 100, "Man, it's windy", "./src/Backend/Images/Weapons/Sword/Sprite.png");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(defaultWeapon);

        unfoundWeapons.add(battleAxe);
        unfoundWeapons.add(normalAxe);
        unfoundWeapons.add(bigSword);
        unfoundWeapons.add(club);
        unfoundWeapons.add(Lance);
        unfoundWeapons.add(katana);
        unfoundWeapons.add(hammer);
        unfoundWeapons.add(sword);



        // Create default items
        ArrayList<Item> items = new ArrayList<>();
        HpItem beaf = new HpItem("Beaf", 100.0,"./src/Backend/Images/itemSprites/Beaf.png");
        HpItem medipack = new HpItem("MediPack", 200,"./src/Backend/Images/itemSprites/Medipack.png");
        HpItem calamari = new HpItem("Calamari", 75,"./src/Backend/Images/itemSprites/Calamari.png");
        HpItem fortune = new HpItem("Fortune Cookie", 50,"./src/Backend/Images/itemSprites/FortuneCookie.png");
        HpItem onigiri = new HpItem("Onigiri", 50,"./src/Backend/Images/itemSprites/Onigiri.png");
        StatusEffect milk = new StatusEffect("Milk", "defense", true, 30, 3, "./src/Backend/Images/itemSprites/MilkPot.png");
        StatusEffect noodle = new StatusEffect("Noodles", "dmg", true, 30, 3, "./src/Backend/Images/itemSprites/Noodle.png");

        unfoundItems.add(beaf);
        unfoundItems.add(medipack);
        unfoundItems.add(calamari);
        unfoundItems.add(fortune);
        unfoundItems.add(onigiri);
        unfoundItems.add(milk);
        unfoundItems.add(noodle);

        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new StatusEffect("Strength Boost", "dmg", true, 5, 3, ""));
        items.add(new HpItem("Health 1", 10, ""));
        items.add(new HpItem("Health3 Potion", 10, ""));
        items.add(new HpItem("Health P4236otion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Heal56th 234Potion", 10, ""));
        items.add(new HpItem("Health62 Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Healt4235h Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Health Potion", 10, ""));
        items.add(new HpItem("Health P6623otion", 10, ""));


        ArrayList<Item> blankItems = new ArrayList<>();
        Stats gHoly = new Stats(15.0, 8.0, 1, 0.1, 0.75); // angelic, high HP
        Stats gRed = new Stats(20.0, 3.0, 300, 0.5, 0.3); // he sees red, low hit chance but it'll hurt when it connects!
        Stats gVoid = new Stats(100.0, 200.0, 10.0, 0.0, 1); // super easy to kill, but oh boy will he CORRUPT you
        Stats gKawaii = new Stats(5.0, 20.0, 600.0, 0.5, 0.8); // kawaii!
        Stats gStar = new Stats(10.0, 50.0, 300.0, 0.7, 0.99); // skilled sharpshooter. rarely misses.
        Stats gSad = new Stats(15.0, 3.0, 200.0, 0.5, 0.3); // he's doing his best, alright?
        Weapon lameStick = new Weapon ("Lame stick", 1.0, "None", 0.2, 0.3, "Ehhhh.. it's alright.", "");

        Enemy holyEnemy = new Enemy(gHoly, lameStick, "Glargo Holy", blankItems, gp, "src/Backend/Images/sprites/glargo_holy.png", 10864, 6824);
        Enemy redEnemy = new Enemy(gRed, lameStick, "Glargo Red", blankItems, gp, "src/Backend/Images/sprites/glargo_red.png", 2104, 2520);
        Enemy kawaiiEnemy = new Enemy(gKawaii, lameStick, "Glargo Kawaii", blankItems, gp, "src/Backend/Images/sprites/glargo_kawaii.png", 5200, 1888);
        Enemy starEnemy = new Enemy(gStar, lameStick, "Glargo Star", blankItems, gp, "src/Backend/Images/sprites/glargo_star.png", 1624, 4048);
        Enemy sadEnemy = new Enemy(gSad, lameStick, "Glargo Sad", blankItems, gp, "src/Backend/Images/sprites/glargo_sad.png", 724, 3000);
        Enemy voidEnemy = new Enemy(gVoid, lameStick, "Glargo Void", blankItems, gp, "src/Backend/Images/sprites/glargo_void.png", 600, 6772);

        unfoundEnemies.add(holyEnemy);
        unfoundEnemies.add(redEnemy);
        unfoundEnemies.add(voidEnemy);
        unfoundEnemies.add(kawaiiEnemy);
        unfoundEnemies.add(starEnemy);
        unfoundEnemies.add(sadEnemy);




        // Create default stats
        Stats stats = new Stats(100.0, 10.0, 200.0, 0.15, 0.85);

        // Create and return the hero
        return new Hero("Player", weapons, items, stats, gp, keyH);
    }



    /**
     * CUSTOM FULL HERO CREATION!!!
     */
    public static Hero createCustomHero(String name, ArrayList<Weapon> weapons,
                                        ArrayList<Item> items, Stats stats,
                                        GamePanel gp, KeyHandler keyH) {
        return new Hero(name, weapons, items, stats, gp, keyH);
    }

}