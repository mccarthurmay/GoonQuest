package Display;

import Backend.Characters.Enemy;
import Backend.ObjectsRendering.WeaponRendering;

public class AssetSetter {
    GamePanel gp;

    public AssetSetter(GamePanel gp){

        this.gp = gp;
    }
    public void setEnemy() {
        gp.enemy[0] = new Enemy(gp);
        gp.enemy[0].worldX = gp.tileSize * 20; // creates a new enemy
        gp.enemy[0].worldY = gp.tileSize * 20; // at this position
    }

    public void setObject () {
        gp.obj[0] = new WeaponRendering();
        gp.obj[0].worldX = gp.tileSize * 30;
        gp.obj[0].worldY = gp.tileSize * 32;

    }
}
