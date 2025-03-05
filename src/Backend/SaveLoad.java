package Backend;

import Backend.Characters.Hero;
import Backend.Characters.CharacterFactory;
import Backend.Characters.Stats;
import Backend.Items.HpItem;
import Backend.Items.Item;
import Backend.Items.StatusEffect;
import Backend.ObjectsRendering.Superobjects;
import Backend.Weapons.Weapon;
import Display.GamePanel;
import Display.KeyHandler;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SaveLoad {
    // Store coordinates of collected objects to be processed after the game is fully set up
    private static ArrayList<String> pendingCollectedObjects = new ArrayList<>();

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

            // Enemies/Weapons/Items block
            writer.println("<collected_objects>");
            // Record found weapons (that have been removed from unfoundWeapons list)
            for (Superobjects obj : CharacterFactory.foundObjects) {
                writer.println(obj.name + "," + obj.worldX + "," + obj.worldY);
            }

            writer.println("</collected_objects>");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Load character from file using a JFileChooser dialog
     * @param filepath Where to find the save file (no longer used directly, now comes from dialog)
     * @param gp The gamepanel you want to pass into the Hero object
     * @param keyH The keyHandler you want to pass into the hero object
     * @return the newly restored hero
     */
    public static Hero load(String filepath, GamePanel gp, KeyHandler keyH) {
        // Create a default hero in case loading fails
        Hero defaultHero = CharacterFactory.createDefaultHero(gp, keyH);

        // Show an option pane to ask if the user wants to load a saved game
        int option = JOptionPane.showConfirmDialog(
                null,
                "Would you like to load a saved game?",
                "Load Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (option != JOptionPane.YES_OPTION) {
            // If user doesn't want to load a game, return the default hero
            return defaultHero;
        }

        // Show a file chooser dialog if user wants to load a game
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Save File");
        fileChooser.setFileFilter(new FileNameExtensionFilter(".sav Files", "sav"));
        fileChooser.setCurrentDirectory(new File(".")); // Start in current directory

        int response = fileChooser.showOpenDialog(null);

        if (response != JFileChooser.APPROVE_OPTION) {
            // If user cancels file selection, return default hero
            return defaultHero;
        }

        File selectedFile = fileChooser.getSelectedFile();
        String savePath = selectedFile.getAbsolutePath();

        try { // error handling loop
            // initialize
            File f = new File(savePath);
            Scanner fileread = new Scanner(f);
            String name = "";
            ArrayList<Weapon> weapons = new ArrayList<>();
            ArrayList<Item> items = new ArrayList<>();
            Stats stats = new Stats(1, 1, 1, 1, 1);
            int worldX = 20; int worldY = 20;

            // Clear any existing pending objects
            pendingCollectedObjects.clear();

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

                if (!fileread.hasNextLine()) break;
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
                            weaponPath = "./src/Backend/Images/Weapons/Bone/Sprite.png";
                        }
                        Weapon nextWeapon = new Weapon(weaponName, weaponDmg, weaponEffect, weaponCrit, weaponHit, weaponMessage, weaponPath);
                        weapons.add(nextWeapon); // add the new weapon to the list

                        if (!fileread.hasNextLine()) break;
                        newline = fileread.nextLine(); // test if the next line is also a weapon
                    }
                }

                if (!fileread.hasNextLine()) break;
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
                                itemPath = "./src/Backend/Images/itemSprites/Beaf.png";
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
                                itemPath = "./src/Backend/Images/itemSprites/Beaf.png";
                            }
                            nextItem = new StatusEffect(statusName, statusClassification, statusBuff, statusNumChange, (int) statusDuration,itemPath);
                        }
                        items.add(nextItem); // add the new item to the list

                        if (!fileread.hasNextLine()) break;
                        newline = fileread.nextLine(); // check if the next line is an item as well
                    }
                }

                if (!fileread.hasNextLine()) break;
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

                        if (!fileread.hasNextLine()) break;
                        newline = fileread.nextLine();
                    }
                }

                if (!fileread.hasNextLine()) break;
                newline = fileread.nextLine(); // go to next block

                // location block
                if (newline.equals("<location>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</location>"))) {
                        String[] lineBroken = newline.split(",");
                        worldX = Integer.parseInt(lineBroken[0]); // get coordinates of character
                        worldY = Integer.parseInt(lineBroken[1]);

                        if (!fileread.hasNextLine()) break;
                        newline = fileread.nextLine();
                    }
                }

                if (!fileread.hasNextLine()) break;
                newline = fileread.nextLine(); // go to next block

                // Parse collected objects section
                if (newline.equals("<collected_objects>")) {
                    newline = fileread.nextLine();

                    while (!(newline.equals("</collected_objects>"))) {
                        // Store object information to be processed later
                        pendingCollectedObjects.add(newline);

                        if (!fileread.hasNextLine()) break;
                        newline = fileread.nextLine();
                    }
                }
            }

            // create new hero with these parameters
            Hero newH = CharacterFactory.createCustomHero(name, weapons, items, stats, gp, keyH);
            newH.worldX = worldX;
            newH.worldY = worldY;

            // Show success message
            JOptionPane.showMessageDialog(
                    null,
                    "Game loaded successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return newH;

        } catch (FileNotFoundException e) {
            // If file not found or error reading file, show error message
            JOptionPane.showMessageDialog(
                    null,
                    "Failed to load save file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return defaultHero; // makes default hero
        } catch (NoSuchElementException e) {
            // Handle the case where Scanner.nextLine() can't find a line
            JOptionPane.showMessageDialog(
                    null,
                    "Save file appears to be malformed: unexpected end of file",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return defaultHero;
        } catch (Exception e) {
            // Handle any other exceptions that might occur
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred while loading the save file: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            e.printStackTrace();
            return defaultHero;
        }
    }

    /**
     * Process pending collected objects after the game is fully set up
     * @param gp The GamePanel with initialized objects
     */
    public static void processPendingCollectedObjects(GamePanel gp) {
        if (pendingCollectedObjects.isEmpty() || gp.obj == null) {
            return;
        }

        for (String coordInfo : pendingCollectedObjects) {
            try {
                String[] parts = coordInfo.split(",");
                String objType = parts[0];
                int objX = Integer.parseInt(parts[1]);
                int objY = Integer.parseInt(parts[2]);

                // Find and remove the object from the world
                removeObjectFromWorld(gp, objType, objX, objY);
            } catch (Exception e) {
                System.out.println("Error processing collected object: " + coordInfo);
                e.printStackTrace();
            }
        }

        // Clear the list after processing
        pendingCollectedObjects.clear();
    }

    // Helper method to remove an object from the game world based on coordinates
    private static void removeObjectFromWorld(GamePanel gp, String objType, int objX, int objY) {
        if (gp.obj == null) return;

        for (int i = 0; i < gp.obj.length; i++) {
            if (gp.obj[i] != null &&
                    gp.obj[i].name.equals(objType) &&
                    gp.obj[i].worldX == objX &&
                    gp.obj[i].worldY == objY) {

                // Add to found objects list
                CharacterFactory.foundObjects.add(gp.obj[i]);

                // Remove from game world
                gp.obj[i] = null;
                break;
            }
        }
    }
}