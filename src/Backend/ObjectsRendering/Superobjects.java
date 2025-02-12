package Backend.ObjectsRendering;

import Display.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Superobjects {

    /**
     * some fields that are necessary for graphics,
     * rendering images, and connecting to other class objects
     */

    public BufferedImage image;
    public String name;
    public String spritePath;
    public boolean collision  = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0,0,48,48);
    public int solidAreaDefaultX = 0;
    public int solidAreaDefaultY = 0;

    /**
     * Constructor
     * @param worldX coordinates
     * @param worldY coordinates
     */
    public Superobjects(int worldX, int worldY){
        this.worldX = worldX;
        this.worldY = worldY;
    }

    /**
     * image rendering
     * @param g2 Basic drawing object
     * @param gp a game panel that we draw on
     */
    public void draw(Graphics g2, GamePanel gp) {

        int screenX = worldX - gp.hero.worldX + gp.hero.screenX;
        int screenY = worldY - gp.hero.worldY + gp.hero.screenY;

        /**
         * Scale the image by 5 times
         */
        int scaledWidth = image.getWidth() * 3;
        int scaledHeight = image.getHeight() * 3;

        if (worldX + scaledWidth > gp.hero.worldX - gp.hero.screenX &&
                worldX - scaledWidth < gp.hero.worldX + gp.hero.screenX &&
                worldY + scaledHeight > gp.hero.worldY - gp.hero.screenY &&
                worldY - scaledHeight < gp.hero.worldY + gp.hero.screenY) {

            /**
             *  Draw the image at 5 times its original size
             */
            g2.drawImage(image, screenX, screenY, scaledWidth, scaledHeight, null);
        }
    }



}
