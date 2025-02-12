package Display;

import Backend.Characters.Enemy;
import Backend.Characters.Hero;
import Backend.Characters.HeroFactory;
import Backend.Characters.Stats;
import Backend.Items.Item;
import Backend.ObjectsRendering.EnemyRendering;
import Backend.ObjectsRendering.ItemRendering;
import Backend.ObjectsRendering.WeaponRendering;
import Backend.Weapons.Weapon;

import java.util.ArrayList;

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

        String beaf2 = "/Backend/Images/itemSprites/default.png";


        gp.obj[0] = new WeaponRendering(battleAxe, 3344, 1888);
        gp.obj[1] = new WeaponRendering(normalAxe, 3340, 1900);
        gp.obj[2] = new WeaponRendering(bigSword, 7420, 10805);
        gp.obj[3] = new WeaponRendering(club, 412, 8172);
        gp.obj[4] = new WeaponRendering(Lance, 3864, 212);
        gp.obj[5] = new WeaponRendering(katana, 9752, 9377);
        gp.obj[6] = new WeaponRendering(hammer, 9968, 1504);
        gp.obj[7] = new WeaponRendering(sword, 2556, 11328);
        gp.obj[8] = new ItemRendering(beaf2, 2468, 8704);


//        for(int i = 0; i < 9; i++){
//            gp.obj[i].worldX = gp.tileSize * (30 + i);
//            gp.obj[i].worldY = gp.tileSize * 32;
//        }

        for (int i = 0; i < HeroFactory.unfoundEnemies.size(); i++){
            System.out.println("adding" + HeroFactory.unfoundEnemies.get(i).getName());
            gp.obj[i + 9] = new EnemyRendering(HeroFactory.unfoundEnemies.get(i));
        }
    }
}
