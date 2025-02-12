package Display;
import Backend.Characters.CharacterFactory;
import Backend.Items.HpItem;
import Backend.Items.StatusEffect;
import Backend.ObjectsRendering.EnemyRendering;
import Backend.ObjectsRendering.ItemRendering;
import Backend.ObjectsRendering.WeaponRendering;

public class AssetSetter {
    GamePanel gp;

    /**
     * Constructor for an Asset Setter
     * @param gp the game panel object that the assets will be set with
     */
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    /**
     * Gets objects ready to be added to the game world (weapons, enemies, items)
     */
    public void setThings () {

        // Weapon paths
        String battleAxe = "/Backend/Images/Weapons/Axe/Sprite.png";
        String normalAxe = "/Backend/Images/Weapons/AxeTool/Sprite.png";
        String bigSword = "/Backend/Images/Weapons/BigSword/Sprite.png";
        String club = "/Backend/Images/Weapons/Club/Sprite.png";
        String Lance = "/Backend/Images/Weapons/Lance/Sprite.png";
        String katana = "/Backend/Images/Weapons/Katana/Sprite.png";
        String hammer = "/Backend/Images/Weapons/Hammer/Sprite.png";
        String sword = "/Backend/Images/Weapons/Sword/Sprite.png";

        // Item paths
        String beaf = "/Backend/Images/itemSprites/Beaf.png";
        String noodle = "/Backend/Images/itemSprites/Noodle.png";
        String milk = "/Backend/Images/itemSprites/MilkPot.png";
        String onigiri = "/Backend/Images/itemSprites/Onigiri.png";
        String fortune = "/Backend/Images/itemSprites/FortuneCookie.png";
        String calamari = "/Backend/Images/itemSprites/Calamari.png";
        String medipack = "/Backend/Images/itemSprites/Medipack.png";

        String treasure = "/Backend/Images/itemSprites/BigTreasureChest.png";
        String goldCoin = "/Backend/Images/itemSprites/GoldCoin.png";
        String lifePot = "/Backend/Images/itemSprites/LifePot.png";
        String octopus =  "/Backend/Images/itemSprites/Octopus.png";
        String seed = "/Backend/Images/itemSprites/Seed1.png";
        String seedLarge = "/Backend/Images/itemSprites/SeedLarge.png";
        String sushi = "/Backend/Images/itemSprites/Sushi.png";
        String sushi2 = "/Backend/Images/itemSprites/Sushi2.png";
        String waterPot = "/Backend/Images/itemSprites/WaterPot.png";


        // Create new renderable weapons, and pass their locations in the world
        gp.obj[0] = new WeaponRendering(battleAxe, 428, 6096);
        gp.obj[1] = new WeaponRendering(normalAxe, 3340, 1900);
        gp.obj[2] = new WeaponRendering(bigSword, 7420, 10805);
        gp.obj[3] = new WeaponRendering(club, 412, 8172);
        gp.obj[4] = new WeaponRendering(Lance, 3864, 212);
        gp.obj[5] = new WeaponRendering(katana, 9752, 9377);
        gp.obj[6] = new WeaponRendering(hammer, 9968, 1504);
        gp.obj[7] = new WeaponRendering(sword, 2556, 11328);

        // create new renderable items, and pass their locations in the world
        gp.obj[8] = new ItemRendering(beaf, 2468, 8704);
        gp.obj[9] = new ItemRendering(noodle, 992, 11260);
        gp.obj[10] = new ItemRendering(medipack, 4980, 2568);
        gp.obj[11] = new ItemRendering(milk, 9420, 3968);
        gp.obj[12] = new ItemRendering(onigiri, 10048, 2832);
        gp.obj[13] = new ItemRendering(fortune, 9008, 11041);
        gp.obj[14] = new ItemRendering(calamari, 468, 2816);

        gp.obj[15] = new ItemRendering(lifePot, 1920, 1880);
        gp.obj[16] = new ItemRendering(treasure, 2112, 2060);
        gp.obj[17] = new ItemRendering(goldCoin, 2112, 2060);
        gp.obj[18] = new ItemRendering(octopus, 2112, 2006);
        gp.obj[19] = new ItemRendering(seed, 2112, 2060);
        gp.obj[20] = new ItemRendering(seedLarge, 2112, 2060);
        gp.obj[21] = new ItemRendering(sushi2, 2112, 2060);
        gp.obj[22] = new ItemRendering(sushi, 2112, 2060);
        gp.obj[23] = new ItemRendering(waterPot, 2112, 2060);


        // create new renderable enemies (locations have already been defined within unfoundEnemies)
        for (int i = 0; i < CharacterFactory.unfoundEnemies.size(); i++){
            gp.obj[i + 15] = new EnemyRendering(CharacterFactory.unfoundEnemies.get(i)); // start at 15 since we already have some items in the list that we don't want to overwrite
        }
    }
}
