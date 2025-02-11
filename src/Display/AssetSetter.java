package Display;

import Backend.Characters.Enemy;
import Backend.Characters.Hero;
import Backend.Characters.Stats;
import Backend.Items.Item;
import Backend.ObjectsRendering.WeaponRendering;
import Backend.Weapons.Weapon;

import java.util.ArrayList;

public class AssetSetter {
    GamePanel gp;


    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }
    public void setEnemy() {
        gp.enemy[0] = new Enemy(gp, "");
        gp.enemy[0].worldX = gp.tileSize * 20; // creates a new enemy
        gp.enemy[0].worldY = gp.tileSize * 20; // at this position
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

        gp.obj[0] = new WeaponRendering(battleAxe);
        gp.obj[1] = new WeaponRendering(normalAxe);
        gp.obj[2] = new WeaponRendering(bigSword);
        gp.obj[3] = new WeaponRendering(club);
        gp.obj[4] = new WeaponRendering(Lance);
        gp.obj[5] = new WeaponRendering(katana);
        gp.obj[6] = new WeaponRendering(hammer);
        gp.obj[7] = new WeaponRendering(sword);


        for(int i = 0; i < 8; i++){
            gp.obj[i].worldX = gp.tileSize * (30 + i);
            gp.obj[i].worldY = gp.tileSize * 32;
        }


    }
}
