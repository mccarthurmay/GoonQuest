package Backend.Items;
import Backend.Characters.CharacterManager;
import Backend.Characters.Hero;

public class StatusEffect extends Item{
    String classification; // defense, health, dmg, etc...
    boolean buff; // can rename, basically is it a buff or debuff?
    double numChange;
    int duration; //later used for duration of rounds


    /**
     * Give buff/debuff to any character
     * @param name Input name of effect
     * @param classification Input class: "defense", "hp", "dmg", "crit", or "hitchance"
     * @param buff Input bool of buff; true = Buff, false = DeBuff
     * @param numChange Input the value of the change. ex, 20 with false is -20 whatevers (dmg maybe)
     * @param duration Input number of rounds this effect will last.
     */
    public StatusEffect(String name, String classification, boolean buff, double numChange, int duration) {
        super(name);
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

    public void sortBuff(CharacterManager character){
        if (classification.equals("defense")){
            double defense = character.getDefense();
            double newDefense = defense + numChange;
            character.setDefense(newDefense);
        } else if (classification.equals("hp")){
            double HP = character.getHP();
            double newHP = HP + numChange;
            character.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = character.getDamage();
            double newDmg = dmg + numChange;
            character.setDamage(newDmg);
        } else if (classification.equals("crit")){
            double crit = character.getCrit();
            double newCrit = crit + numChange;
            character.setCrit(newCrit);
        } else if (classification.equals("hitchance")){
            double hc = character.getHitChance();
            double newHc = hc + numChange;
            character.setHitChance(newHc);
        }
    }

    public void sortDebuff(CharacterManager character){
        if (classification.equals("defense")){
            double defense = character.getDefense();
            double newDefense = defense - numChange;
            character.setDefense(newDefense);
        } else if (classification.equals("hp")){
            double HP = character.getHP();
            double newHP = HP - numChange;
            character.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = character.getDamage();
            double newDmg = dmg - numChange;
            character.setDamage(newDmg);
        } else if (classification.equals("crit")){
            double crit = character.getCrit();
            double newCrit = crit - numChange;
            character.setCrit(newCrit);
        } else if (classification.equals("hitchance")){
            double hc = character.getHitChance();
            double newHc = hc - numChange;
            character.setHitChance(newHc);
        }
    }

    public void removeBuff(CharacterManager character){
        if (buff){
            sortDebuff(character);
        }
        else{
            sortBuff(character);
        }
    }

    // Debuffs should never lower duration... can fix later when battle is created
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

    // Battle logic:
    // continually call "if buffWasUsed" then "reduce duration" "if duration == 0, removeBuff"
    // just have to track "if buffWasUsed" variable

}

