package Display;
import Backend.Characters.*;
import Backend.WorldBuilding.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    public  int originalTileSize = 20;
    public  int scale = 3;
    public int tileSize = originalTileSize * scale;
    public final int maxScreenCol= 16;
    public final int maxScreenRow= 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;

    // World Settings
    public final int maxWorldCol = 36;
    public final int maxWorldRow = 36;
    public final int worldWidth  = tileSize*maxWorldCol;
    public final int WorldHeight = tileSize * maxWorldRow;


    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public Hero hero = HeroFactory.createDefaultHero(this, keyH);

    // set player's default position * DELETE LATER*
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;



    // Composite controls how pixels overlap existing pixels
    // Src_over draws pixels on top of existing, taking in account of transparency
    // DstOut makes existing pixels more transparent where drawn (cut holes)
    private AlphaComposite fogComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.99f);
    private int visibilityRadius = 100;


    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);


    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    /**
     * In this loop, we will constantly be updating the charcater position and
     * draw the sprites on the screen
     */
    public void run(){

        int FPS = 60;
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;


        while(gameThread != null) {
//            System.out.println("FPS: " + FPS);
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }
    }

    public void update() {
        hero.update();
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Create buffer to draw shit in memory before in world, prevents flickering
        BufferedImage buffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bufferG = buffer.createGraphics();

        // Draw out normal world
        tileM.draw(bufferG);
        hero.draw(bufferG);

        // Create fog layer
        BufferedImage fogLayer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D fogG = fogLayer.createGraphics();

        // Fill fog layer with black
        fogG.setColor(new Color(0,0,0));
        fogG.fillRect(0, 0, screenWidth, screenHeight);

        // Create gradient for smooth visibility circle
        RadialGradientPaint gradient = new RadialGradientPaint(
                hero.screenX + tileSize/2,
                hero.screenY + tileSize/2,
                visibilityRadius,
                new float[]{0.0f, 0.5f, 1.0f}, // Percent of circle
                new Color[]{
                        new Color(0,0,0, 255), // Center is fully visible
                        new Color(0,0,0, 150), // Middle mostly visible
                        new Color(0,0,0, 50) // Edge is darkest
                }
        );

        // Apply gradient to create visibility circle
        fogG.setComposite(AlphaComposite.DstOut);
        fogG.setPaint(gradient);
        fogG.fillOval(
                hero.screenX + tileSize/2 - visibilityRadius,
                hero.screenY + tileSize/2 - visibilityRadius,
                visibilityRadius *2,
                visibilityRadius *2
        );


        // Actually draw the shit out
        g2.drawImage(buffer, 0, 0, null);
        g2.setComposite(fogComposite);
        g2.drawImage(fogLayer, 0, 0, null);

        // Clear memory
        bufferG.dispose();
        fogG.dispose();
        g2.dispose();
    }

    public void setVisibilityRadius(int radius){
        this.visibilityRadius = radius;
    }

    public void setFogDensity(float density){
        this.fogComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, density);
    }

}
