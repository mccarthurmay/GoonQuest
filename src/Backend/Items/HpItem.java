package Backend.Items;
import Backend.Characters.*;

public class HpItem extends Item {

    double hpGain;

    public HpItem(String name, double hpGain, String spritePath) {
        super(name, spritePath);
        this.hpGain = hpGain;
    }

    public String getName() {
        return name;
    }

    public double getHpGain() {
        return hpGain;
    }

    @Override
    public void useItem(CharacterManager character) {
        double HP = character.getHP();
        double newHP = HP + hpGain;
        character.setHP(newHP);
    }
    public String toString(){
        return "HpItem" + ',' + getName() + ',' + getHpGain();
    }
}


// Milk
// Tree Bark
// Etc... we can have fun with item stuff