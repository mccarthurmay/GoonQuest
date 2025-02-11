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
        Weapon battleAxe = new Weapon("Battle Axe", 25, "wind", 8,85,"Man, it's windy", "./src/Backend/Images/Weapons/Axe/Sprite.png");
        ArrayList<Weapon> weapons = new ArrayList<>();
        ArrayList<Item> items = new ArrayList<>();
        Stats stats = new Stats(100.0, 10.0, 200.0, 0.15, 0.85);
        gp.obj[0] = new WeaponRendering(battleAxe);
        gp.obj[0].worldX = gp.tileSize * 30;
        gp.obj[0].worldY = gp.tileSize * 32;

    }
}
