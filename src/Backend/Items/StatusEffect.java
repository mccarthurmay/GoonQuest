package Backend.Items;
import Backend.Characters.Hero;

public class StatusEffect extends Item{
    String classification; // defense, health, dmg, etc...
    //int rounds; // number of rounds the buff lasts for?
    boolean buff; // can rename, basically is it a buff or debuff?
    double numChange;
    int duration; //later used for duration of rounds

    public StatusEffect(String name, String classification, boolean buff, double numChange, int duration) {
        super(name);
        this.classification = classification;
        this.buff = buff;
        this.numChange = numChange;
        this.duration = duration;
    }

    public void giveBuff(Hero hero) { // remove 1 duration after each battle
        if (duration > 0) {
            applyBuff(hero);
            duration--;
        } else {
            removeBuff(hero);
        }
    }

    public void applyBuff(Hero hero){
        if (buff){ // if buff is true, then give buff
            sortBuff(hero);
        }
        else{ // else buff is false, give debuff
            sortDebuff(hero);
        }
    }

    public void removeBuff(Hero hero){
        if (buff){
            sortDebuff(hero);
        }
        else{
            sortDebuff(hero);
        }
    }

    public void sortBuff(Hero hero){
        if (classification.equals("defense")){
            double defense = hero.getDefense();
            double newDefense = defense + numChange;
            hero.setDefense(newDefense);
        } else if (classification.equals("health")){
            double HP = hero.getHP();
            double newHP = HP + numChange;
            hero.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = hero.getDamage();
            double newDmg = dmg + numChange;
            hero.setDamage(newDmg);
        }
    }

    public void sortDebuff(Hero hero){
        if (classification.equals("defense")){
            double defense = hero.getDefense();
            double newDefense = defense - numChange;
            hero.setDefense(newDefense);
        } else if (classification.equals("health")){
            double HP = hero.getHP();
            double newHP = HP - numChange;
            hero.setHP(newHP);
        } else if (classification.equals("dmg")) {
            double dmg = hero.getDamage();
            double newDmg = dmg - numChange;
            hero.setDamage(newDmg);
        }
    }


    public void useItem(Hero hero) {
    }

}

