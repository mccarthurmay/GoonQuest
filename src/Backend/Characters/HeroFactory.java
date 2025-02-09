package Backend.Characters;

import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;
import java.util.ArrayList;

public class HeroFactory {

    /**
     * Create full default hero, saves some time
     */
    public static Hero createDefaultHero(GamePanel gp, KeyHandler keyH) {
        // Create default weapon
        Weapon defaultWeapon = new Weapon("Stick", 2, "Basic weapon", 2, 3, "A simple stick", "");
        Weapon defaultWeapon2 = new Weapon("Stick2", 2, "Basic", 2, 3, "A simple stick", "");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(defaultWeapon);
        weapons.add(defaultWeapon2);

        // Create default items
        ArrayList<Item> items = new ArrayList<>();
        items.add(new HpItem("Health Potion", 10));
        items.add(new StatusEffect("Strength Boost", "dmg", true, 5, 3));

        // Create default stats
        Stats stats = new Stats(100.0, 10.0, 5.0, 0.15, 0.85);

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


    /**
     *Character test hero creation - no gp no key handler
     */
    public static Hero createCharacterTestHero(){
        // Create default weapon
        Weapon defaultWeapon = new Weapon("Stick", 2, "Basic weapon", 2, 3, "A simple stick", "");
        ArrayList<Weapon> weapons = new ArrayList<>();
        weapons.add(defaultWeapon);

        // Create default items
        ArrayList<Item> items = new ArrayList<>();
        items.add(new HpItem("Health Potion", 10));
        items.add(new StatusEffect("Strength Boost", "dmg", true, 5, 3));

        // Create default stats
        Stats stats = new Stats(100.0, 10.0, 5.0, 0.15, 0.85);

        return new Hero("Player", weapons, items, stats);
    }


    /**
     * Creates a simplified hero for display testing
     */
    public static Hero createDisplayTestHero(GamePanel gp, KeyHandler keyH) {
        return new Hero(gp, keyH);
    }

}