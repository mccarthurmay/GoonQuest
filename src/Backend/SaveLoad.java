package Backend;

import Backend.Characters.Hero;
import Backend.Characters.HeroFactory;
import Backend.Characters.Stats;
import Backend.Items.HpItem;
import Backend.Items.Item;
import Backend.Items.StatusEffect;
import Backend.Weapons.Weapon;
import Display.GamePanel;
import Display.KeyHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class SaveLoad {
    /**
     * Saves the current progress (location and hero details) to a file.
     * @param hero The character that is saved to the file
     */
    public static void save(Hero hero){
        try {
            // Create new file object
            PrintStream writer = new PrintStream(new File("out.sav"));

            // Name block
            writer.println("<name>");
            writer.println(hero.getName());
            writer.println("</name>");

            // Weapon block
            writer.println("<weapon>");
            for (Weapon weapon : hero.getOwnedWeapons()){ // for every weapon you have in your inventory
                writer.println(weapon.toString()); // write them all to the file
            }
            writer.println("</weapon>");

            // item block
            writer.println("<item>");
            for (Item item : hero.getOwnedItems()){ // for every item in your inventory
                writer.println(item.toString()); // write them to the file
            }
            writer.println("</item>");

            // stat block
            writer.println("<stats>");
            writer.println(hero.getStats());
            writer.println("</stats>");

            // location block
            writer.println("<location>");
            writer.println(hero.worldX + "," + hero.worldY); // writes current character coordinates
            writer.println("</location>");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load character from file
     * @param filepath Where to find the save file
     * @param gp The gamepanel you want to pass into the Hero object
     * @param keyH The keyHandler you want to pass into the hero object
     * @return the newly restored hero
     */
    public static Hero load(String filepath, GamePanel gp, KeyHandler keyH) {
        try {
            // initialize
            File f = new File(filepath);
            Scanner fileread = null;
            fileread = new Scanner(f);
            String name = "";
            ArrayList<Weapon> weapons = new ArrayList<>();
            ArrayList<Item> items = new ArrayList<>();
            Stats stats = new Stats(1, 1, 1, 1, 1);
            int worldX= 20; int worldY= 20;

            // read file
            while (fileread.hasNextLine()) {
                String newline = fileread.nextLine();

                // name block
                if (newline.equals("<name>")) {
                    newline = fileread.nextLine(); // go to the next line to find the name
                    while (!(newline.equals("</name>"))) {
                        name = newline;
                        //System.out.println(name);
                        newline = fileread.nextLine();
                    }
                }
                newline = fileread.nextLine(); // go to the next block

                // weapon block
                if (newline.equals("<weapon>")) {
                    newline = fileread.nextLine(); // go to the next line for the first weapon
                    while (!(newline.equals("</weapon>"))) {
                        String[] lineBroken = newline.split(","); // split up weapon info as an array of strings
                        String weaponName = lineBroken[0]; // Weapon Name = First part
                        double weaponDmg = Double.parseDouble(lineBroken[1]); // Weapon Damage = Second part
                        String weaponEffect = lineBroken[2]; // weapon effect (fire, etc) = third part
                        double weaponCrit = Double.parseDouble(lineBroken[3]); // weapon crit = fourth part
                        double weaponHit = Double.parseDouble(lineBroken[4]); // weapon hit chance = fifth part
                        String weaponMessage = lineBroken[5]; // weapon message = sixth part
                        String weaponPath = " "; // optional weapon path part
                        try {
                            weaponPath = lineBroken[6];
                        } catch(Exception e){ // if there's no given weapon path, handle the error with a default value
                            weaponPath = " ";
                        }
                        Weapon nextWeapon = new Weapon(weaponName, weaponDmg, weaponEffect, weaponCrit, weaponHit, weaponMessage, weaponPath);
                        weapons.add(nextWeapon); // add the new weapon to the list
                        newline = fileread.nextLine(); // test if the next line is also a weapon
                    }
                }
                newline = fileread.nextLine(); // go to the next block

                // item block
                if (newline.equals("<item>")) {
                    newline = fileread.nextLine(); // go to next line for first item
                    while (!(newline.equals("</item>"))) {
                        Item nextItem;
                        String[] lineBroken = newline.split(","); // break up information about the weapon
                        String itemPath = " ";

                        if (lineBroken[0].equals("HpItem")) { // if the item is a healing item
                            String hpName = lineBroken[1]; // save info about the item
                            double hpGain = Double.parseDouble(lineBroken[2]);
                            try {
                                itemPath = lineBroken[3]; // if item has an image, save it, otherwise get a default
                            } catch(Exception e){
                                itemPath = " ";
                            }
                            nextItem = new HpItem(hpName, hpGain,itemPath);
                        } else { // if the item is a damage item
                            String statusName = lineBroken[1]; // save info about the item
                            String statusClassification = lineBroken[2];
                            boolean statusBuff = Boolean.parseBoolean(lineBroken[3]);
                            double statusNumChange = Double.parseDouble(lineBroken[4]);
                            double statusDuration = Double.parseDouble(lineBroken[5]);
                            try {
                                itemPath = lineBroken[6]; // if item has an image, save it, otherwise get a default
                            } catch(Exception e){
                                itemPath = " ";
                            }
                            nextItem = new StatusEffect(statusName, statusClassification, statusBuff, statusNumChange, (int) statusDuration,itemPath);
                        }
                        items.add(nextItem); // add the new item to the list
                        newline = fileread.nextLine(); // check if the next line is an item as well
                    }
                }
                newline = fileread.nextLine(); // go to the next block

                // stat block
                if (newline.equals("<stats>")) {
                    newline = fileread.nextLine(); // go to next line for stats
                    while (!(newline.equals("</stats>"))) {
                        String[] lineBroken = newline.split(","); // break up line into individual stats
                        double statAttack = Double.parseDouble(lineBroken[0]); // get info about stats
                        double statDef = Double.parseDouble(lineBroken[1]);
                        double statHP = Double.parseDouble(lineBroken[2]);
                        double statCrit = Double.parseDouble(lineBroken[3]);
                        double statHit = Double.parseDouble(lineBroken[4]);
                        stats = new Stats(statAttack, statDef, statHP, statCrit, statHit); // save as new stats object
                        newline = fileread.nextLine();
                    }
                }
                newline = fileread.nextLine(); // go to next block

                // location block
                if (newline.equals("<location>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</location>"))) {
                        String[] lineBroken = newline.split(",");
                        worldX = Integer.parseInt(lineBroken[0]); // get coordinates of character
                        worldY = Integer.parseInt(lineBroken[1]);
                        newline = fileread.nextLine();
                    }
                }
            }

            // create new hero with these parameters
            Hero newH = HeroFactory.createCustomHero(name, weapons, items, stats, gp, keyH);
            newH.worldX = worldX;
            newH.worldY = worldY;
            return newH;

        } catch (FileNotFoundException e) {
            System.out.println("Bad file or file not found.");
            return HeroFactory.createDefaultHero(gp, keyH); // makes default hero if error
        }

    }
}