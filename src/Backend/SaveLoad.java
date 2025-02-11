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
    public static void save(Hero hero){
        try {
            PrintStream writer = new PrintStream(new File("out.txt"));
            writer.println("<name>");
            writer.println(hero.getName());
            writer.println("</name>");
            writer.println("<weapon>");
            for (Weapon weapon : hero.getOwnedWeapons()){
                writer.println(weapon.toString());
            }
            writer.println("</weapon>");
            writer.println("<item>");
            for (Item item : hero.getOwnedItems()){
                writer.println(item.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
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
            int worldX= 20; int worldY= 20;
            while (fileread.hasNextLine()) {
                String newline = fileread.nextLine();
                if (newline.equals("<name>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</name>"))) {
                        name = newline;
                        System.out.println(name);
                        newline = fileread.nextLine();
                    }
                }
                newline = fileread.nextLine();
                if (newline.equals("<weapon>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</weapon>"))) {
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
                if (newline.equals("<item>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</item>"))) {
                        Item nextItem;
                        String[] lineBroken = newline.split(",");
                        if (lineBroken[0].equals("HpItem")) { // if the item is a healing item
                            String hpName = lineBroken[1];
                            double hpGain = Double.parseDouble(lineBroken[2]);
                            nextItem = new HpItem(hpName, hpGain);
                        } else { // damage item
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
                if (newline.equals("<stats>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</stats>"))) {
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
                newline = fileread.nextLine();
                if (newline.equals("<location>")) {
                    newline = fileread.nextLine();
                    while (!(newline.equals("</location>"))) {
                        System.out.println(newline);
                        String[] lineBroken = newline.split(",");
                        worldX = Integer.parseInt(lineBroken[0]);
                        worldY = Integer.parseInt(lineBroken[1]);
                        newline = fileread.nextLine();
                    }
                }
            }
            Hero newH = HeroFactory.createCustomHero(name, weapons, items, stats, gp, keyH);
            newH.worldX = worldX;
            newH.worldY = worldY;

            return newH;

        } catch (FileNotFoundException e) {
            System.out.println("Bad file or file not found.");
            return HeroFactory.createDefaultHero(gp, keyH);
        }

    }
}