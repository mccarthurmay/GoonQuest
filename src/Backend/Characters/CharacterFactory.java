package Backend.Characters;

import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;

import java.util.ArrayList;

public class CharacterFactory {

    /**
     * Static ArrayList that we use later in order to display and store items on the map.
     */
    public static ArrayList<Weapon> unfoundWeapons = new ArrayList<>();
    public static ArrayList<Item> unfoundItems = new ArrayList<>();
    public static ArrayList<Enemy> unfoundEnemies = new ArrayList<>();

    /**
     * Create full default hero, saves some time
     */
    public static Hero createDefaultHero(GamePanel gp, KeyHandler keyH) {
        // Create default weapon
        Weapon defaultWeapon = new Weapon("Book", 2, "Basic weapon", 2, 3, "a book ", "./src/Backend/Images/Weapons/Book/Sprite.png");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(defaultWeapon);

        // Base path for all weapon sprites
        String weaponBasePath = "./src/Backend/Images/Weapons/";

        // Define weapon data in a 2D array: {name, damage, element, crit, accuracy, description, sprite name}
        Object[][] weaponData = {
                {"Battle Axe", 25, "wind", 8, 85, "Man, it's windy", "Axe"},
                {"Basic Axe", 8, "wind", 5, 85, "Man, it's windy", "AxeTool"},
                {"Big sword", 28, "wind", 7, 85, "Man, it's windy", "BigSword"},
                {"Club", 18, "wind", 15, 99, "Man, it's windy", "Club"},
                {"Knight's lance", 22, "wind", 11, 90, "Man, it's windy", "Lance"},
                {"Kusanagi-no-tsurugi", 30, "wind", 10, 80, "Man, it's windy", "Katana"},
                {"Sledgehammer", 14, "wind", 12, 99, "Man, it's windy", "Hammer"},
                {"Dawnbreaker", 20, "wind", 20, 100, "Man, it's windy", "Sword"},
                {"Poseidon's Fork", 35, "wind", 30, 93, "Man, it's windy", "Fork"},
                {"The 'Chucks", 15, "wind", 3, 40, "Man, it's windy", "Ninjaku"},
                {"Fortnite", 1, "wind", 0, 0, "Man, it's windy", "Pickaxe"},
                {"Pointy", 12, "wind", 10, 85, "Man, it's windy", "Rapier"},
                {"Trunk", 15, "wind", 50, 90, "Man, it's windy", "Sai"},
                {"Mid Sword", 10, "wind", 20, 85, "Man, it's windy", "Sword2"},
                {"Lucky Whip", 99999999, "", 100, 1, "", "Whip"}
        };

        // Create and add weapons to unfoundWeapons list
        for (Object[] data : weaponData) {
            unfoundWeapons.add(new Weapon(
                    (String)data[0],                    // name
                    (Integer)data[1],                   // damage
                    (String)data[2],                    // element
                    (Integer)data[3],                   // crit
                    (Integer)data[4],                   // accuracy
                    (String)data[5],                    // description
                    weaponBasePath + data[6] + "/Sprite.png"  // spritePath
            ));
        }




        // Base path for all item sprites
        String itemBasePath = "./src/Backend/Images/itemSprites/";

        // HP Items data: {name, hp amount, sprite filename}
        Object[][] hpItemsData = {
                {"Beaf", 100.0, "Beaf"},
                {"MediPack", 200.0, "Medipack"},
                {"Calamari", 75.0, "Calamari"},
                {"Fortune Cookie", 50.0, "FortuneCookie"},
                {"Onigiri", 50.0, "Onigiri"},
                {"Life Pot", 100.0, "LifePot"},
                {"Octopus", 25.0, "Octopus"},
                {"Seed", 10.0, "Seed1"},
                {"Larger Seed", 25.0, "SeedLarge"},
                {"Sushi", 50.0, "Sushi"},
                {"Sushi #2", 50.0, "Sushi2"},
                {"Water Pot", 1.0, "WaterPot"}
        };

        // Status Effect items data: {name, effect type, is buff, amount, duration, sprite filename}
        Object[][] statusItemsData = {
                {"Milk", "defense", true, 30, 3, "MilkPot"},
                {"Noodles", "dmg", true, 30, 3, "Noodle"},
                {"Treasure Chest", "hit_chance", true, 100, 1, "BigTreasureChest"},
                {"goldCoin", "hit_chance", true, 30, 1, "GoldCoin"}
        };

        // Create and add all HP items
        for (Object[] data : hpItemsData) {
            unfoundItems.add(new HpItem(
                    (String)data[0],             // name
                    (Double)data[1],             // hp amount
                    itemBasePath + data[2] + ".png"  // sprite path
            ));
        }

        // Create and add all Status Effect items
        for (Object[] data : statusItemsData) {
            unfoundItems.add(new StatusEffect(
                    (String)data[0],             // name
                    (String)data[1],             // effect type
                    (Boolean)data[2],            // is buff
                    (Integer)data[3],            // amount
                    (Integer)data[4],            // duration
                    itemBasePath + data[5] + ".png"  // sprite path
            ));
        }

        /**
         *  Create default items
         */
        ArrayList<Item> items = new ArrayList<>();
        items.add(new HpItem("Shrimp", 50, "./src/Backend/Images/itemSprites/Shrimp.png"));
        items.add(new HpItem("Honey", 50, "./src/Backend/Images/itemSprites/Honey.png"));
        items.add(new HpItem("Fish", 50, "./src/Backend/Images/itemSprites/Fish.png"));




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


        /**
         * Create default stats
         */

        Stats stats = new Stats(100.0, 10.0, 200.0, 0.15, 0.85);

        /**
         * Create and return the hero
         */
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