package Backend.Characters;
import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import Display.KeyHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static Backend.Characters.CharacterFactory.unfoundItems;
import static Backend.Characters.CharacterFactory.unfoundWeapons;
import static Backend.Characters.CharacterFactory.unfoundEnemies;
import static Backend.Characters.CharacterFactory.foundObjects;

public class Hero extends CharacterManager {
    /**
     * ----Game mechanics fields----
     * Few attributes for this class for:
     * Storing weapons
     * Storing Items
     * Names for reference
     * Stats for battling
     */
    private ArrayList<Weapon> ownedWeapons;
    private ArrayList<Item> ownedItems ;
    private String name;
    private Stats stats;

    /**
     * Display and movement fields
     */
    private KeyHandler keyH;
    public final int screenX;
    public final int screenY;



    /**
     * Create a character - now with Christian's game panel and keyhandler
     */
    public Hero(String name, ArrayList<Weapon> ownedWeapons,
                ArrayList<Item> ownedItems, Stats stats, GamePanel gp, KeyHandler keyH) {

        super(stats, ownedWeapons.get(0), name);

        // Game mechanics initialization
        this.name = name;
        this.ownedWeapons = ownedWeapons;
        this.ownedItems = ownedItems;
        this.stats = stats;

        /**
         * Display and movement initialization
         */
        this.gp = gp;
        System.out.println(this.gp);
        this.keyH = keyH;
        this.screenX = gp.screenWidth/2 - (gp.tileSize/2);
        this.screenY = gp.screenHeight/2 - (gp.tileSize/2);

        /**
         * Initialize collision area
         */
        solidArea = new Rectangle();
        solidArea.x = 10;
        solidArea.y = 10;
        solidArea.width = 36;
        solidArea.height = 36;

        setDefaultValues();
        getPlayerImage();
    }

    /**
     *  Use one of those special milks (items)
     * @param item signals which item we should remove from the hero's inventory
     */
    public void useItem(Item item) {
        ownedItems.remove(item);
        item.useItem(this);
    }

    /**
     * Display and movement methods
     */
    public void setDefaultValues() {
        worldY = gp.tileSize * 30;
        worldX = gp.tileSize * 30;
        speed = 4;
        direction = "down";
    }

    /**
     * Some getters
     * @return some information about attributes
     */
    public ArrayList<Weapon> getOwnedWeapons(){
        return ownedWeapons;
    }

    public ArrayList<Item> getOwnedItems() {return ownedItems;}

    public void addWeapon(Weapon weapon) {
        ownedWeapons.add(weapon);
    }
    public void addItem(Item item) {
        ownedItems.add(item);
    }

    /**
     * Updates information on the hero, such as coordinates,
     * item, and weapon bag length, sprites(for animation purposes)
     * sends signals about collision etc.
     */

    public void update() {

        if(keyH.rightPressed || keyH.leftPressed || keyH.upPressed || keyH.downPressed) {
            if(keyH.upPressed) {
                direction = "up";
            } else if(keyH.downPressed) {
                direction = "down";
            } else if(keyH.leftPressed) {
                direction = "left";
            } else if(keyH.rightPressed) {
                direction = "right";
            }
            if(keyH.shiftPressed){
                speed = 8;
            } else{
                speed = 4;
            }
            /**
             *  Check Tile Collision
             */
            collisionsOn = false;
            gp.collisionChecker.checkTile(this);
            int obj_index = gp.collisionChecker.checkObject(this);
            pickUpObject(obj_index);


            /**
             * fight enemy!
             */

            if (collisionsOn == false) {
                switch (direction) {
                    case "up":
                        this.worldY -= speed;
                        break;
                    case "down":
                        this.worldY += speed;
                        break;
                    case "left":
                        this.worldX -= speed;
                        break;
                    case "right":
                        this.worldX += speed;
                        break;
                }
            }


            /**
             * Sets a counter to update the Hero's image at the right intervals.
             */

            spriteCounter++;
            if(spriteCounter > 10) {
                spriteNum = (spriteNum % 4) + 1;
                spriteCounter = 0;
            }

        }
    }

    /**
     * Checks for collision with items displaced on the map,
     * and adds some of those items, or sometimes fight such in the
     * case if enemy, then later destroys those items.
     * @param i, it is an index that signals when hero touched one of these
     *           objects displaced on the map.
     */
    public void pickUpObject(int i){
        if(i != 999 && gp.obj != null && gp.obj[i] != null){
            String objectName = gp.obj[i].name;
            String objectPath = gp.obj[i].spritePath;

            System.out.println(objectPath);
            switch(objectName) {

                /**
                 * Sets some cases for when we interact with different objects.
                 * for example, when we interact with a weapon object, we iterate throygh the
                 * list of weapons displaced around the world, and delete them from that list, destroy the
                 * items, and add a weapon object that used to be linked to that sprite on the open map.
                 *
                 * On the case of Enemy, we initiate a fight.
                 */
                case ("Weapon"):
                    gp.playSE(3);
                    for (int j = 0; j < unfoundWeapons.size(); j++) {
                        if (unfoundWeapons.get(j).getSpritePath().contains(objectPath)) {
                            addWeapon(unfoundWeapons.get(j));
                            unfoundWeapons.remove(j);
                            break;
                        }
                    }
                    // Track the object as found before removing it
                    foundObjects.add(gp.obj[i]);
                    gp.obj[i] = null;
                    break;

                case ("Item"):

                    gp.playSE(4);

                    for (int j = 0; j < unfoundItems.size(); j++) {
                        System.out.println(unfoundItems.get(j).getSpritePath());
                        System.out.println(objectPath);
                        if (unfoundItems.get(j).getSpritePath().contains(objectPath)) {
                            addItem(unfoundItems.get(j));
                            unfoundItems.remove(j);
                            break;
                        }
                    }
                    // Track the object as found before removing it
                    foundObjects.add(gp.obj[i]);
                    gp.obj[i] = null;
                    break;
                case ("Enemy"):
                    for (int k = 0; k < unfoundEnemies.size(); k++){
                        System.out.println(unfoundEnemies.get(k).getSpritePath());
                        System.out.println(objectPath);
                        if (unfoundEnemies.get(k).getSpritePath().contains(objectPath)) {
                            gp.battle.toBattleTransition(unfoundEnemies.get(k));
                            unfoundEnemies.remove(k);
                        }
                    }
                    // Track the object as found before removing it
                    foundObjects.add(gp.obj[i]);
                    gp.obj[i] = null;
                    break;

            }
        }
    }

    /**
     * Draws the Hero on gamePanel
     * @param g2 Is the panel on which we draw on.
     */
    public void drawHero(Graphics g2) {
        BufferedImage img = null;

        /**
         * The follwoing switch, allows to switch images whenever our sprite is
         * waling in a specific direction.
         */
        switch(direction) {
            case "up":
                if(spriteNum == 1){
                    img = up1;
                    break;
                }
                if(spriteNum == 2){
                    img = up2;
                    break;
                }
                if(spriteNum == 3){
                    img = up3;
                    break;
                }
                if(spriteNum == 4){
                    img = up4;
                    break;
                }
            case "down":
                if(spriteNum == 1){
                    img = down1;
                    break;
                }
                if(spriteNum == 2){
                    img = down2;
                    break;
                }
                if(spriteNum == 3){
                    img = down3;
                    break;
                }
                if(spriteNum == 4) {
                    img = down4;
                    break;
                }

            case "left":
                if(spriteNum == 1){
                    img = left1;
                    break;
                }
                if(spriteNum == 2){
                    img = left2;
                    break;
                }
                if(spriteNum == 3){
                    img = left3;
                    break;
                }
                if(spriteNum == 4){
                    img = left4;
                    break;
                }

            case "right":
                if(spriteNum == 1){
                    img = right1;
                    break;
                }
                if(spriteNum == 2){
                    img = right2;
                    break;
                }
                if(spriteNum == 3){
                    img = right3;
                    break;
                }
                if(spriteNum == 4){
                    img = right4;
                    break;
                }

        }
        /**
         * Draws the hero with an specific image in a spefic coordinate on the map.
         */
        g2.drawImage(img, screenX, screenY, gp.tileSize, gp.tileSize, null);
    }

    @Override
    public String toString() {
        return "Hero{" +
                "ownedWeapons=" + ownedWeapons +
                ", ownedItems=" + ownedItems +
                ", name='" + name + '\'' +
                ", stats=" + stats +
                '}';
    }
}