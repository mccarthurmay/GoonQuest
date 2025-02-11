package Backend.Items;
import Backend.Characters.CharacterManager;
import Backend.Characters.Hero;

public class StatusEffect extends Item{
    String classification; // defense, health, dmg, etc...
    boolean buff; // can rename, basically is it a buff or debuff?
    double numChange;
    int duration; //later used for duration of rounds


    /**
     * Give buff/debuff to any character. Buff = increases a stat, Debuff = decreases a stat
     * @param name Input name of effect
     * @param classification Input class: "defense", "hp", "dmg", "crit", or "hitchance"
     * @param buff Input bool of buff; true = Buff, false = DeBuff
     * @param numChange Input the value of the change. ex, 20 with false is -20 whatevers (dmg maybe)
     * @param duration Input number of rounds this effect will last.
     */
    public StatusEffect(String name, String classification, boolean buff, double numChange, int duration, String spritePath) {
        super(name, spritePath);
        this.classification = classification;
        this.buff = buff;
        this.numChange = numChange;
        this.duration = duration;
    }

    public void useItem(CharacterManager character) {
        if (buff) { // if buff is true, then give buff
            sortBuff(character);
        } else { // else buff is false, give debuff
            sortDebuff(character);
        }
    }

    /**
     * Tells you what kind of buff will be applied
     * @param character the character receiving  the buff
     */
    public void sortBuff(CharacterManager character){
        if (classification.equals("defense")){ // changes defense
            double defense = character.getDefense();
            double newDefense = defense + numChange;
            character.setDefense(newDefense);
        } else if (classification.equals("hp")){ // changes hp
            double HP = character.getHP();
            double newHP = HP + numChange;
            character.setHP(newHP);
        } else if (classification.equals("dmg")) { // changes damage
            double dmg = character.getDamage();
            double newDmg = dmg + numChange;
            character.setDamage(newDmg);
        } else if (classification.equals("crit")){ // changes crit
            double crit = character.getCrit();
            double newCrit = crit + numChange;
            character.setCrit(newCrit);
        } else if (classification.equals("hitchance")){ // changes hitchance
            double hc = character.getHitChance();
            double newHc = hc + numChange;
            character.setHitChance(newHc);
        }
    }

    /**
     *Tells you what kind of debuff will be applied
     * @param character the character receiving the debuff
     */
    public void sortDebuff(CharacterManager character){
        if (classification.equals("defense")){ // changes defense
            double defense = character.getDefense();
            double newDefense = defense - numChange;
            character.setDefense(newDefense);
        } else if (classification.equals("hp")){ // changes hp
            double HP = character.getHP();
            double newHP = HP - numChange;
            character.setHP(newHP);
        } else if (classification.equals("dmg")) { // changes damage
            double dmg = character.getDamage();
            double newDmg = dmg - numChange;
            character.setDamage(newDmg);
        } else if (classification.equals("crit")){ // changes crit
            double crit = character.getCrit();
            double newCrit = crit - numChange;
            character.setCrit(newCrit);
        } else if (classification.equals("hitchance")){ // changes hit chance
            double hc = character.getHitChance();
            double newHc = hc - numChange;
            character.setHitChance(newHc);
        }
    }

    /**
     * Gets rid of the buff by doing the opposite: if it's a buff, debuff back to normal. If it's a debuff, buff back to normal.
     * @param character character to be returned to normal
     */
    public void removeBuff(CharacterManager character){
        if (buff){
            sortDebuff(character);
        }
        else{
            sortBuff(character);
        }
    }

    // Debuffs should never lower duration... can fix later when battle is created

    /**
     * Tick the buff/debuff's duration until it reaches 0, so battle can keep track of when to run removeBuff
     * @param character
     */
    public void reduceDuration(CharacterManager character){
        if (duration > 0) {
            duration--;
        }
        if (duration == 0) {
            removeBuff(character);
            duration--; // Makes it so removeBuff is only ran once
        }

    }

    public int getDuration(){
        return duration;
    }

    public String toString(){
        return "StatusEffect" + ',' + this.getName() + ',' + this.classification + ',' + this.buff + ',' +this.numChange + ',' + this.getDuration();
    }

    // Battle logic:
    // continually call "if buffWasUsed" then "reduce duration" "if duration == 0, removeBuff"
    // just have to track "if buffWasUsed" variable

}

