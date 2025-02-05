package Backend.Items;

import Backend.Characters.Hero;

public class StatusEffect extends Item{
    String classification; // defense, health, dmg, etc...
    //int rounds; // number of rounds the buff lasts for?
    boolean buff; // can rename, basically is it a buff or debuff?

    public StatusEffect(String name, String classification, boolean buff) {
        super(name);
        this.classification = classification;
        this.buff = buff;
    }

    public void giveBuff(){
        // if classification.equals(defense) then add 10 defense for the round etc
    }
    public void useItem(Hero hero) {
    }

    public void giveDebuff(){

    }
}

