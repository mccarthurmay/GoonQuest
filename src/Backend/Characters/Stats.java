package Backend.Characters;

public class Stats {
    double attackMod;
    double defMod;
    double HP;
    double crit;
    double hitChance; //

    /**
     * Stats object for all enemies and the hero
     * @param attackMod Weapons are the primary way to increase the damage given, but the attackMod will also increase this
     * @param defMod Affects how much damage you take, and how likely you are to dodge
     * @param HP How much health you have left. 0 = game over, reload save
     * @param crit Chance to deal double damage! added to the weapon crit chance
     * @param hitChance Chance to hit the enemy. added to the weapon hit chance
     */
    public Stats(double attackMod, double defMod, double HP, double crit, double hitChance){
        this.attackMod = attackMod;
        this.defMod = defMod;
        this.HP = HP;
        this.crit = crit;
        this.hitChance = hitChance;
    }

    public void takeDamage(double taken){
        HP -= taken - (taken*defMod); // reduces damage taken the higher your defense modifier is
    }
    
}
