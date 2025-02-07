package Backend.Characters;
import Backend.Items.*;
import Backend.Weapons.*;
import Display.GamePanel;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy extends CharacterManager{

    GamePanel gp;
    public Enemy(Stats stats, Weapon weapon, String name, ArrayList<Item> ownedItems){
        super(stats, weapon, name);
        this.ownedItems = ownedItems;
    }

    public Enemy(GamePanel gp){
        super(gp);
        direction = "down";
        speed = 3;
        getImage();
    }

    public void getImage() {
        try {
            down1 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            down2 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            down3 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            down4 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));

            right1 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            right2 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            right3 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            right4 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));

            up1 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            up2 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            up3 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            up4 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));

            left1 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            left2 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            left3 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
            left4 = ImageIO.read(new File("src/Backend/Images/sprites/SH_Lham_Dearg.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }




}



