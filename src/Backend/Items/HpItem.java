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
    public void useItem(Hero hero) {
        double HP = hero.getHP();
        hero.setHP(HP);
    }

}


// Milk
// Tree Bark
// Etc... we can have fun with item stuff