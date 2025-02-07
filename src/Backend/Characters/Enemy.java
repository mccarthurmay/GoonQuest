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

public class Enemy extends CharacterManager{

    GamePanel gp;
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
    public void draw(Graphics2D g2){
        BufferedImage image = down1;

        int screenX = worldX - gp.hero.worldX + gp.hero.screenX;
        int screenY = worldY - gp.hero.worldY + gp.hero.screenY;

        if(worldX + gp.tileSize > gp.hero.worldX - gp.hero.screenX && worldX - gp.tileSize < gp.hero.worldX + gp.hero.screenX && worldY + gp.tileSize > gp.hero.worldY - gp.hero.screenY && worldY - gp.tileSize < gp.hero.worldY + gp.hero.screenY){
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }





}



