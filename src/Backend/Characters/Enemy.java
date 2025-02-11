package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;
import Display.GamePanel;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends CharacterManager{

    GamePanel gp;
    String path;
    int actionLockCounter = 0;
    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems, String path){
        super(stats, weapon, name);
        this.ownedItems = ownedItems;
        this.path = path;
    }

    public Enemy(GamePanel gp, String path){
        super(gp);
        this.gp = gp;
        this.path = path;
        direction = "down";
        speed = 3;
        getImage();
    }
    public void update() {
        collisionsOn = true;
        //gp.collisionChecker.checkTile(this);
    }

    public void getImage() {
        try {
            down1 = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setAction() {

        actionLockCounter++;
        if(actionLockCounter == 10){
            Random random = new Random();
            int i = random.nextInt(100);
            if (i == 1){
                direction = "right";
            }
            else if (i == 2){
                direction = "up";
            }
            else if (i == 3){
                direction = "down";
            }
            else if (i  == 4){
                direction = "left";
            }
            else{
                direction = "none";
            }
            actionLockCounter = 0;
        }


    }


    public Weapon getWeapon() {
        return weapon;
    }
}



