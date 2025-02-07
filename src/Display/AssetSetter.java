package Display;

import Backend.Characters.Enemy;

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
}
