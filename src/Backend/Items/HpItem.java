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

    public void useItem(Hero hero) {
        double HP = hero.getHP();
        double newHP = HP + hpGain;
        hero.setHP(newHP);
    }

}


// Milk
// Tree Bark
// Etc... we can have fun with item stuff