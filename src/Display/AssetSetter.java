package Display;

import Backend.Characters.CharacterFactory;
import Backend.ObjectsRendering.EnemyRendering;
import Backend.ObjectsRendering.ItemRendering;
import Backend.ObjectsRendering.WeaponRendering;

public class AssetSetter {
    GamePanel gp;


    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    
    public void setObject () {
        String battleAxe = "/Backend/Images/Weapons/Axe/Sprite.png";
        String normalAxe = "/Backend/Images/Weapons/AxeTool/Sprite.png";
        String bigSword = "/Backend/Images/Weapons/BigSword/Sprite.png";
        String club = "/Backend/Images/Weapons/Club/Sprite.png";
        String Lance = "/Backend/Images/Weapons/Lance/Sprite.png";
        String katana = "/Backend/Images/Weapons/Katana/Sprite.png";
        String hammer = "/Backend/Images/Weapons/Hammer/Sprite.png";
        String sword = "/Backend/Images/Weapons/Sword/Sprite.png";


        String beaf = "/Backend/Images/itemSprites/Beaf.png";
        String noodle = "/Backend/Images/itemSprites/Noodle.png";
        String milk = "/Backend/Images/itemSprites/MilkPot.png";
        String onigiri = "/Backend/Images/itemSprites/Onigiri.png";
        String fortune = "/Backend/Images/itemSprites/FortuneCookie.png";
        String calamari = "/Backend/Images/itemSprites/Calamari.png";
        String medipack = "/Backend/Images/itemSprites/Medipack.png";



        gp.obj[0] = new WeaponRendering(battleAxe, 3344, 1888);
        gp.obj[1] = new WeaponRendering(normalAxe, 3340, 1900);
        gp.obj[2] = new WeaponRendering(bigSword, 7420, 10805);
        gp.obj[3] = new WeaponRendering(club, 412, 8172);
        gp.obj[4] = new WeaponRendering(Lance, 3864, 212);
        gp.obj[5] = new WeaponRendering(katana, 9752, 9377);
        gp.obj[6] = new WeaponRendering(hammer, 9968, 1504);
        gp.obj[7] = new WeaponRendering(sword, 2556, 11328);

        gp.obj[8] = new ItemRendering(beaf, 2468, 8704);
        gp.obj[9] = new ItemRendering(noodle, 992, 11260);
        gp.obj[10] = new ItemRendering(medipack, 4980, 2568);
        gp.obj[11] = new ItemRendering(milk, 9420, 3968);
        gp.obj[12] = new ItemRendering(onigiri, 10048, 2832);
        gp.obj[13] = new ItemRendering(fortune, 9008, 11041);
        gp.obj[14] = new ItemRendering(calamari, 468, 2816);


        for (int i = 0; i < CharacterFactory.unfoundEnemies.size(); i++){
            System.out.println("adding" + CharacterFactory.unfoundEnemies.get(i).getName());
            gp.obj[i + 14] = new EnemyRendering(CharacterFactory.unfoundEnemies.get(i));
        }
    }
}
