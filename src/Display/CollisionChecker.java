package Display;

import Backend.Characters.CharacterManager;
import Backend.Characters.Enemy;
import Backend.Characters.Hero;

public class CollisionChecker {
    GamePanel gp;

    /**
     * Collision checker constructor
     * @param gp takes in gamepanel, where lots of essential information is stored
     */
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Check the nearby tiles for collision, and if they have collision, tell the character it should stop moving
     * @param entity the character to be checked
     */
    public void checkTile(CharacterManager entity) { // check tile for collision
        // create a virtual box for the character to be tested against in the world
        int entityLeftWorldX = entity.worldX + entity.solidArea.x; // left side
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width; //right side
        int entityTopWorldY = entity.worldY + entity.solidArea.y; // top side
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height ; // bottom side

        int entityLeftCol = entityLeftWorldX / gp.tileSize; // grabs whichever tile is to your left
        int entityRightCol = entityRightWorldX / gp.tileSize; // grabs whichever tile is to your right
        int entityTopRow = entityTopWorldY / gp.tileSize; // grabs top tile
        int entityBottomRow = entityBottomWorldY / gp.tileSize; // grabs bottom tile

        int tileNum1, tileNum2; // for every interaction with a tile, each of your sides will likely be touching two tiles

        switch (entity.direction) { // depending on the way you are facing, check collision with different tiles
            case ("up"):
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tileSize; // account for speed in the upwards direction
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow]; // get the tile map for your top left corner
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow]; // get the tile map for your top right corner
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) { // if either of those tiles have collision
                    entity.collisionsOn = true; // the character is colliding
                }
                break;

            case ("down"):
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tileSize; // account for speed downwards
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow]; // get the tile to your bottom left
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow]; // get the tile to your bottom right
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) { // if either of those have collision
                    entity.collisionsOn = true; // character is colliding
                }
                break;
            case("left"):
                entityLeftCol = (entityLeftWorldX - entity.speed)/gp.tileSize; // account for speed leftwards
                tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow]; // get tile to the top left
                tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow]; // get tile to bottom left
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) { // if either of those have collision
                    entity.collisionsOn = true; // you're colliding
                }
                break;

            case("right"):
                entityRightCol = (entityRightWorldX + entity.speed)/gp.tileSize; // account for speed rightwards
                tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow]; // get tile for top right
                tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow]; // get tile for bottom right
                if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) { // if either have collision
                    entity.collisionsOn = true; // character is colliding!
                }
                break;
        }
    }

    /**
     * Check if an object is being collided with
     * @param entity the object that may be collided with
     * @return return the index of the object being collided with
     */
    public int checkObject(Hero entity){
        int index = 999; // placeholder index -> if 999 is returned, no object is collided with

        for( int i = 0; i<gp.obj.length; i++){ // iterate through list of objects
            if(gp.obj[i] != null){ // if there is a valid object in the list

                // Get entity solid area position
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                // Get the object Area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                switch(entity.direction) { // do different things depending on direction of entity
                    case "up":
                        entity.solidArea.y -= entity.speed; // account for speed
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){ // if you intersect the object,
                            if(gp.obj[i].solidArea.intersects(entity.solidArea)){ // and the object intersects you
                                entity.collisionsOn = true; // you're colliding with the object
                            }
                            index = i; // return the object's index
                        }
                        break;
                    case "down": // rinse and repeat for other directions
                        entity.solidArea.y += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].solidArea.intersects(entity.solidArea)){
                                entity.collisionsOn = true;
                            }
                            index = i;
                        }
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].solidArea.intersects(entity.solidArea)){
                                entity.collisionsOn = true;
                            }
                            index = i;
                        }
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        if(entity.solidArea.intersects(gp.obj[i].solidArea)){
                            if(gp.obj[i].solidArea.intersects(entity.solidArea)){
                                entity.collisionsOn = true;
                            }
                            index = i;
                        }
                        break;
                }
                entity.solidArea.x = entity.solidAreaDefaultX; // sets the solid area to the defaults for a character
                entity.solidArea.y = entity.solidAreaDefaultY;
                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX; // sets the solid area to the defaults for an object
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }
        return index;
    }
}


