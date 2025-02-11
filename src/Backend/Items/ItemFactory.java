package Backend.Items;

public class ItemFactory {
    public static void main(String[] args) {
        HpItem beaf = new HpItem("Beaf", 10.0,"./src/Backend/Images/itemSprites/default.png");
        HpItem treasureChest = new HpItem("Mystery Chest", 20.0,"./src/Backend/Images/itemSprites/default.png");
        HpItem calamari = new HpItem("Calamari", 30.0,"./src/Backend/Images/itemSprites/default.png");
        HpItem fish = new HpItem("Fish", 40.0,"./src/Backend/Images/itemSprites/default.png");


        StatusEffect invincibility = new StatusEffect("Seed", "hp", true, 1000, 1, "./src/Backend/Images/itemSprites/default.png");
        StatusEffect fortune = new StatusEffect("Fortune Cookie", "hit_chance", true, 100, 1, "./src/Backend/Images/itemSprites/default.png");
        StatusEffect goldCoin = new StatusEffect("Gold Coin", "crit", true, 100, 1, "./src/Backend/Images/itemSprites/default.png");
    }
}
