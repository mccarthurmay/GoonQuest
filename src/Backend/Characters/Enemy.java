package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;
import Display.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Enemy extends CharacterManager{

    GamePanel gp;
    int actionLockCounter = 0;
    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems){
        super(stats, weapon, name);
        this.ownedItems = ownedItems;
    }

    public Enemy(GamePanel gp){
        super(gp);
        this.gp = gp;
        direction = "down";
        speed = 3;
        getImage();
    }

    public void getImage() {
        try {
            down1 = ImageIO.read(new File("src/Backend/Images/sprites/glargo_red.png"));
        } catch(IOException e) {
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






}



