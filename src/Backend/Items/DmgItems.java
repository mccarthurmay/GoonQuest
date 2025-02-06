package Backend.Items;

import Backend.Characters.Hero;

// Basically just gonna make this affect the character that it is applied to. Follows logic of StatusEffect.
// If we want enemy to get damaged, consume the damage item and do enemy.useItem()

public class DmgItems {
    double dmg;

    DmgItems(String name, double dmg) {
        this.dmg = dmg;
    }

    public void useItem(Hero hero) {
    }
}
