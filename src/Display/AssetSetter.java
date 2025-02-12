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
        // Get hero's current position
        int heroTileX = gp.hero.worldX / gp.tileSize;
        int heroTileY = gp.hero.worldY / gp.tileSize;

        gp.enemy[0] = new Enemy(gp, "src/Backend/Images/sprites/glargo_holy.png");
        gp.enemy[0].worldX = heroTileX * gp.tileSize; // Set to hero's X position
        gp.enemy[0].worldY = heroTileY * gp.tileSize; // Set to hero's Y position

        System.out.println("Enemy spawned at: " + heroTileX + ", " + heroTileY);
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
