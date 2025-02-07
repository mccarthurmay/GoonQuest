package Backend.Characters;
import Backend.Items.Item;
import Backend.Items.StatusEffect;
import Backend.Weapons.Weapon;
import Display.GamePanel;
import Display.KeyHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.Key;
import java.util.ArrayList;
import java.util.Random;

abstract public class CharacterManager {

    // Trying some stuff
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3, up4, down1, down2, down3, down4, left1, left2, left3, left4, right1, right2, right3, right4;
    public String direction;

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public boolean collisionsOn = false;








    // Parent
    Stats stats;
    Weapon weapon; // Going to give bosses/mobs/minibosses only one weapon vs an arraylist
    String name;
    GamePanel gp;
    KeyHandler keyH;
    boolean guarding = false;

    public CharacterManager(Stats stats, Weapon weapon, String name){
        this.stats = stats;
        this.weapon = weapon;
        this.name = name;
    }
    public CharacterManager(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;
    }
    public String getName(){
        return name;
    }

    public void attack(CharacterManager foe){
        Random rand = new Random();
        double hitDecider = rand.nextDouble();
        double critDecider = rand.nextDouble();
        double hitVal = 0;
        if (hitDecider < stats.hitChance){
            System.out.println("Attack connects!");
            weapon.attack(); // print out a special message
            if (critDecider < stats.crit){
                System.out.println("Critical hit! Double damage!");
                hitVal = 2* (stats.attackMod + weapon.getDmg());
            }
            else {
               hitVal = stats.attackMod + weapon.getDmg();
            }
        }
        foe.takeDamage(hitVal);
    }

    public void guard(){
        System.out.println(name + " is guarding!");
        guarding = true;
    }

    public void takeDamage(double hitVal){
        if (guarding){
            stats.HP -= (hitVal);
            System.out.println("Got hit for " + hitVal + ", but was guarding!");
            System.out.println(name + " HP is now " + stats.HP);
        }
        System.out.println("hitval: " + hitVal);
        stats.HP -= (hitVal);
        System.out.println("Got hit for " + hitVal);
        System.out.println(name + " HP is now " + stats.HP);
    }
    ArrayList<Item> ownedItems = new ArrayList<>();



    public double getHP(){
        return stats.getHP();
    }
    public void setHP(Double newHP){
        stats.setHP(newHP);
    }
    public double getDamage(){
        return stats.attackMod;
    }
    public void setDamage(Double newDamage){
        stats.setAttackMod(newDamage);
    }
    public double getCrit(){
        return stats.crit;
    }
    public void setCrit(Double newCrit){
        stats.setCrit(newCrit);
    }
    public double getDefense(){
        return stats.defMod;
    }
    public void setDefense(Double newDefense){
        stats.setDefMod(newDefense);
    }
    public double getHitChance(){
        return stats.hitChance;
    }
    public void setHitChance(Double newHitChance){
        stats.setHitChance(newHitChance);
    }

    public Stats getStats() {
        return stats;
    }

    public void reduceBuff(StatusEffect buff){
        buff.reduceDuration(this);
    }
    public void reduceDebuff(StatusEffect debuff){
        debuff.reduceDuration(this);
    }

    public String toString() {
        return "EnemyType {" +
                "Name=" + name +
                ", ownedItems=" + ownedItems +
                ", Weapon='" + weapon + '\'' +
                ", stats=" + stats +
                '}';
    }




}


// STATS SHOULD BE HERE - i think. It may be easier to just keep track of all stats in parent. Every
// single character has the same stats - HP defence crit chance hit chance etc