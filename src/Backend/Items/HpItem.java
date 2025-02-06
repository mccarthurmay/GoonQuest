package Backend.Items;
import Backend.Characters.*;

public class HpItem extends Item {

    double hpGain;

    public HpItem(String name, double hpGain) {
        super(name);
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

}


// Milk
// Tree Bark
// Etc... we can have fun with item stuff