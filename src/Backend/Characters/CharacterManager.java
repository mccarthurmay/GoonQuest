package Backend.Characters;
import Backend.Weapons.Weapon;

import java.util.Random;

abstract public class CharacterManager {
    // Parent
    Stats stats;
    Weapon weapon; // Going to give bosses/mobs/minibosses only one weapon vs an arraylist
    String name;
    public CharacterManager(Stats stats, Weapon weapon, String name){

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
        foe.stats.takeDamage(hitVal);
    }

}


// STATS SHOULD BE HERE - i think. It may be easier to just keep track of all stats in parent. Every
// single character has the same stats - HP defence crit chance hit chance etc