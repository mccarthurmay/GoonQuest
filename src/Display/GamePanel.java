package Display;
import Backend.Characters.Hero;

import javax.swing.JPanel;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    public  int originalTileSize = 16;
    public  int scale = 3;
    public int tileSize = originalTileSize * scale;
    final int maxScreenCol= 16;
    final int maxScreenRow= 12;
    final int screenWidth = maxScreenCol * tileSize;
    final int screenHeight = maxScreenRow * tileSize;

    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    Hero hero = new Hero(this, keyH);

    // set player's default position * DELETE LATER*
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

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


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        hero.draw(g2);
        g2.dispose();
    }



}
