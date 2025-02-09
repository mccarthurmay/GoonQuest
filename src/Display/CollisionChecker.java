package Display;

import Backend.Characters.CharacterManager;
import Backend.Characters.Enemy;
import Backend.Characters.Hero;

public class CollisionChecker {
    GamePanel gp;
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    public void checkTile(CharacterManager entity) {
        int entityLeftWorldX = entity.worldX + entity.solidArea.x ;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height ;

        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBottomRow = entityBottomWorldY / gp.tileSize;

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case ("up"):
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionsOn = true;
                }
                break;

            case ("down"):
                System.out.println(entity);
                System.out.println( entityBottomWorldY + ", " + entity.speed);
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize;
                System.out.println(entityBottomRow);
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionsOn = true;
                }
                break;
            case("left"):
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionsOn = true;
                }
                break;

            case("right"):
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize;
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true) {
                    entity.collisionsOn = true;
                }
                break;


        }
    }

    public int checkEnemy(CharacterManager hero, CharacterManager[] target){
        int index = 999;

        for (int i = 0; i < target.length; i++){
            if (target[i] != null){
                hero.solidArea.x = hero.worldX + hero.solidArea.x;
                hero.solidArea.y = hero.worldY + hero.solidArea.y;

                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (hero.direction){
                    case "up":
                        hero.solidArea.y -= hero.speed;
                        if(hero.solidArea.intersects(target[i].solidArea)){
                            hero.collisionsOn = true;
                            index = i;
                        }
                        break;
                    case "down":
                        hero.solidArea.y += hero.speed;
                        if (hero.solidArea.intersects(target[i].solidArea)){
                            hero.collisionsOn = true;
                            index = i;
                        }
                        break;
                    case "left":
                        hero.solidArea.x -= hero.speed;
                        if (hero.solidArea.intersects(target[i].solidArea)){
                            hero.collisionsOn = true;
                            index = i;
                        }
                        break;
                    case "right":
                        hero.solidArea.x += hero.speed;
                        if (hero.solidArea.intersects(target[i].solidArea)){
                            hero.collisionsOn = true;
                            index = i;
                        }
                        break;
                }

            }
        }
        return index;

    }
}

