package Backend.Items;

import Backend.Characters.Hero;

public class DmgItems extends Item {
    double dmg;

    DmgItems(String name, double dmg) {
        super(name);
        this.dmg = dmg;
    }

    public void useItem(Hero hero) {
    }
}
