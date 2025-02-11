package Backend.ObjectsRendering;

import Display.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Superobjects {

    public BufferedImage image;
    public String name;
    public boolean collision  = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;


    public void draw (Graphics g2, GamePanel gp) {
        int screenX = worldX - gp.hero.worldX + gp.hero.screenX;
        int screenY = worldY - gp.hero.worldY + gp.hero.screenY;

        if(worldX + gp.tileSize > gp.hero.worldX - gp.hero.screenX && worldX - gp.tileSize < gp.hero.worldX + gp.hero.screenX && worldY + gp.tileSize > gp.hero.worldY - gp.hero.screenY && worldY - gp.tileSize < gp.hero.worldY + gp.hero.screenY){
            //System.out.println(tileNum);
            g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        }
    }

}
