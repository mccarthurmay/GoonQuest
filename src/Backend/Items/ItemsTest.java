package Backend.Items;

public class ItemsTest {
    public static void main(String[] args) {
        HpItem Milk = new HpItem("Milk", 10.0);
        HpItem Chicken = new HpItem("Chicken", 20.0);

        DmgItems Grenade = new DmgItems("Grenade", 100);
        DmgItems MagicSpear = new DmgItems("Magic Spear", 200);

//        StatusEffect SmokeBomb = new StatusEffect("Smoke Bomb", "hit_chance", false);
//        StatusEffect BadScroll = new StatusEffect("Bad Scroll", "defense", false);
//        StatusEffect GoodScroll = new StatusEffect("Good Scroll", "dmg", true);

    }
}
