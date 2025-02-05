package Backend.Characters;
import Backend.Weapons.Weapon;
import Display.GamePanel;
import Display.KeyHandler;

import java.security.Key;
import java.util.Random;

abstract public class CharacterManager {
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

}


// STATS SHOULD BE HERE - i think. It may be easier to just keep track of all stats in parent. Every
// single character has the same stats - HP defence crit chance hit chance etc