package Backend.Items;
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

    public void useItem(Hero hero) {
        if (buff) { // if buff is true, then give buff
            sortBuff(hero);
        } else { // else buff is false, give debuff
            sortDebuff(hero);
        }
    }

    public void sortBuff(Hero hero){
        if (classification.equals("defense")){
            double defense = hero.getDefense();
            double newDefense = defense + numChange;
            hero.setDefense(newDefense);
        } else if (classification.equals("hp")){
            double HP = hero.getHP();
            double newHP = HP + numChange;
            hero.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = hero.getDamage();
            double newDmg = dmg + numChange;
            hero.setDamage(newDmg);
        } else if (classification.equals("crit")){
            double crit = hero.getCrit();
            double newCrit = crit + numChange;
            hero.setCrit(newCrit);
        } else if (classification.equals("hitchance")){
            double hc = hero.getHitChance();
            double newHc = hc + numChange;
            hero.setHitChance(newHc);
        }
    }

    public void sortDebuff(Hero hero){
        if (classification.equals("defense")){
            double defense = hero.getDefense();
            double newDefense = defense - numChange;
            hero.setDefense(newDefense);
        } else if (classification.equals("hp")){
            double HP = hero.getHP();
            double newHP = HP - numChange;
            hero.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = hero.getDamage();
            double newDmg = dmg - numChange;
            hero.setDamage(newDmg);
        } else if (classification.equals("crit")){
            double crit = hero.getCrit();
            double newCrit = crit - numChange;
            hero.setCrit(newCrit);
        } else if (classification.equals("hitchance")){
            double hc = hero.getHitChance();
            double newHc = hc - numChange;
            hero.setHitChance(newHc);
        }
    }

    public void removeBuff(Hero hero){
        if (buff){
            sortDebuff(hero);
        }
        else{
            sortBuff(hero);
        }
    }

    // Debuffs should never lower duration... can fix later when battle is created
    public void reduceDuration(Hero hero){
        if (duration > 0) {
            duration--;
        }
        if (duration == 0) {
            removeBuff(hero);
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

