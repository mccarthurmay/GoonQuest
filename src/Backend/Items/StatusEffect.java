package Backend.Items;

public class StatusEffect {

    String name; // give each buff/debuff a cool name
    String classification; // defense, health, dmg, etc...
    //int rounds; // number of rounds the buff lasts for?
    boolean buff; // can rename, basically is it a buff or debuff?

    public StatusEffect(String name, String classification, boolean buff) {
        this.name = name;
        this.classification = classification;
        this.buff = buff;
    }

    public void giveBuff(){
        // if classification.equals(defense) then add 10 defense for the round etc
    }

    public void giveDebuff(){

    }
}

