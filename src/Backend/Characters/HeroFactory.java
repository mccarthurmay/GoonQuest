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

    public static Hero load(String filepath, GamePanel gp, KeyHandler keyH) {

        try {
            File f = new File(filepath);
            Scanner fileread = null;
            fileread = new Scanner(f);
            // load each part of the hero from the text file
            String name = "";
            ArrayList<Weapon> weapons = new ArrayList<>();
            ArrayList<Item> items = new ArrayList<>();
            Stats stats = new Stats(1, 1, 1, 1, 1);

            while(fileread.hasNextLine()){
                String newline = fileread.nextLine();
                if (newline.equals("<name>")){
                    newline = fileread.nextLine();
                    while (!(newline.equals("</name>"))){
                        name = newline;
                        System.out.println(name);
                        newline = fileread.nextLine();
                    }
                }
                newline = fileread.nextLine();
                if (newline.equals("<weapon>")){
                    newline = fileread.nextLine();
                    while (!(newline.equals("</weapon>"))){
                        System.out.println(newline);
                        String[] lineBroken = newline.split(",");
                        String weaponName = lineBroken[0];
                        double weaponDmg = Double.parseDouble(lineBroken[1]);
                        String weaponEffect = lineBroken[2];
                        double weaponCrit = Double.parseDouble(lineBroken[3]);
                        double weaponHit = Double.parseDouble(lineBroken[4]);
                        String weaponMessage = lineBroken[5];
                        String weaponPath = lineBroken[6];
                        Weapon nextWeapon = new Weapon(weaponName, weaponDmg, weaponEffect, weaponCrit, weaponHit, weaponMessage, weaponPath);
                        weapons.add(nextWeapon);
                        newline = fileread.nextLine();
                    }
                }
                newline = fileread.nextLine();
                if (newline.equals("<item>")){
                    newline = fileread.nextLine();
                    while (!(newline.equals("</item>"))){
                        Item nextItem;
                        String[] lineBroken = newline.split(",");
                        if (lineBroken[0].equals("HpItem")){ // if the item is a healing item
                            String hpName = lineBroken[1];
                            double hpGain = Double.parseDouble(lineBroken[2]);
                            nextItem = new HpItem(hpName, hpGain);
                        } else{ // damage item
                            String statusName = lineBroken[1];
                            String statusClassification = lineBroken[2];
                            boolean statusBuff = Boolean.parseBoolean(lineBroken[3]);
                            double statusNumChange = Double.parseDouble(lineBroken[4]);
                            double statusDuration = Double.parseDouble(lineBroken[5]);
                            nextItem = new StatusEffect(statusName, statusClassification, statusBuff, statusNumChange, (int) statusDuration);
                        }
                        items.add(nextItem);
                        newline = fileread.nextLine();

                    }
                }
                newline = fileread.nextLine();
                if (newline.equals("<stats>")){
                    newline = fileread.nextLine();
                    while (!(newline.equals("</stats>"))){
                        System.out.println(newline);
                        String[] lineBroken = newline.split(",");
                        double statAttack = Double.parseDouble(lineBroken[0]);
                        double statDef = Double.parseDouble(lineBroken[1]);
                        double statHP = Double.parseDouble(lineBroken[2]);
                        double statCrit = Double.parseDouble(lineBroken[3]);
                        double statHit = Double.parseDouble(lineBroken[4]);
                        stats = new Stats(statAttack, statDef, statHP, statCrit, statHit);
                        newline = fileread.nextLine();
                    }
                }
            }

            return createCustomHero(name, weapons, items, stats, gp, keyH);

        } catch (FileNotFoundException e) {
            System.out.println("Bad file or file not found.");
            return createDefaultHero(gp, keyH);
        }

//        } catch (Exception e){
//            System.out.println("Couldn't find that file, have a default hero instead");
//            return createDefaultHero(gp, keyH);
//        }


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