package Display;
import Backend.Characters.*;
import Backend.Items.Item;
import Backend.ObjectsRendering.Superobjects;
import Backend.SaveLoad;
import Backend.Weapons.Weapon;
import Backend.WorldBuilding.TileManager;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable {
    // Screen settings
    public int originalTileSize = 20;
    public int scale = 3;
    public int tileSize = originalTileSize * scale;
    public final int maxScreenCol= 16;
    public final int maxScreenRow= 12;
    public final int screenWidth = maxScreenCol * tileSize;
    public final int screenHeight = maxScreenRow * tileSize;

    // World Settings
    public final int maxWorldCol = 199;
    public final int maxWorldRow = 199;
    public final int worldWidth  = tileSize*maxWorldCol;
    public final int WorldHeight = tileSize * maxWorldRow;


    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound sound = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    Thread gameThread;


    public Hero hero = loadHero(this, keyH);
    public Superobjects obj[] = new Superobjects[10];

    public Enemy foe = EnemyFactory.createDefaultEnemy(this);

    Stats gHoly = new Stats(15.0, 8.0, 1, 0.1, 0.75); // angelic, high HP
    Stats gRed = new Stats(20.0, 3.0, 300, 0.5, 0.3); // he sees red, low hit chance but it'll hurt when it connects!
    Stats gVoid = new Stats(100.0, 200.0, 10.0, 0.0, 1); // super easy to kill, but oh boy will he CORRUPT you
    Stats gKawaii = new Stats(5.0, 20.0, 600.0, 0.5, 0.8); // kawaii!
    Stats gStar = new Stats(10.0, 50.0, 300.0, 0.7, 0.99); // skilled sharpshooter. rarely misses.
    Stats gSad = new Stats(15.0, 3.0, 200.0, 0.5, 0.3); // he's doing his best, alright?
    Weapon lameStick = new Weapon ("Lame stick", 1.0, "None", 0.2, 0.3, "Ehhhh.. it's alright.", "");
    ArrayList<Item> blankItems = new ArrayList<>();
    public Enemy holyEnemy = new Enemy(gHoly, lameStick, "Glargo Holy", blankItems, this, "src/Backend/Images/sprites/glargo_holy.png", 30, 31);
    public Enemy redEnemy = new Enemy(gRed, lameStick, "Glargo Red", blankItems, this, "src/Backend/Images/sprites/glargo_red.png", 32, 30);
    public Enemy voidEnemy = new Enemy(gVoid, lameStick, "Glargo Void", blankItems, this, "src/Backend/Images/sprites/glargo_void.png", 33, 30);
    public Enemy kawaiiEnemy = new Enemy(gKawaii, lameStick, "Glargo Kawaii", blankItems, this, "src/Backend/Images/sprites/glargo_kawaii.png", 34, 30);
    public Enemy starEnemy = new Enemy(gStar, lameStick, "Glargo Star", blankItems, this, "src/Backend/Images/sprites/glargo_star.png", 35, 30);
    public Enemy sadEnemy = new Enemy(gSad, lameStick, "Glargo Sad", blankItems, this, "src/Backend/Images/sprites/glargo_sad.png", 36, 30);

    //public Enemy[] enemy = {holyEnemy, redEnemy, voidEnemy, kawaiiEnemy, sadEnemy, starEnemy}; // can have up to 10 enemies

    public Enemy[] enemy = {holyEnemy};





    // set player's default position * DELETE LATER*
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;



    // Composite controls how pixels overlap existing pixels
    // Src_over draws pixels on top of existing, taking in account of transparency
    // DstOut makes existing pixels more transparent where drawn (cut holes)\
    // CHANGE ALPHA TO CHANGE HOW TRANSPARENT THE FOG IS
    private AlphaComposite fogComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    private int visibilityRadius = 100;

    // Add a buffer for entire game -- no buffer causes flickering
    private BufferedImage gameBuffer;

    // Add fields for fog regions
    private ArrayList<Rectangle> foggyRegions;
    private boolean isInFoggyRegion = false;

    // Battle
    public Battle battle;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);

        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        // Initialize game buffer
        gameBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);


        // define foggy regions with coordinates
        foggyRegions = new ArrayList<>();
        // EXAMPLEEEEEE
        addFoggyRegion(18,18,5,5);
    }
    private boolean checkFoggyRegion() {
        Point playerWorldPos = new Point(hero.worldX, hero.worldY);

        for (Rectangle region : foggyRegions) {
            if (region.contains(playerWorldPos)) {
                return true;
            }
        }
        return false;
    }

    public void setupGame() { // add things to the game world, play music, etc
        //aSetter.setEnemy();

        aSetter.setObject();
        playMusic(0 );
    }

    public void startGameThread() {
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
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

    public void stopGameThread() {
        gameThread = null;
        try {
            // Give time for thread to properly terminate
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        hero.update();

//        if(hero.worldX < 60 && hero.worldY < 80){
//            stopMusic();
//            playMusic(1);
//        }else{
//            stopMusic();
//            playMusic(0);
//        }


        for (int i = 0; i < enemy.length; i++){
            if (enemy[i] != null){
                enemy[i].update();
            }
        }

        isInFoggyRegion = checkFoggyRegion();

        // REMOVE THIS LATER
        if(keyH.battleTestKey) {
            battle.testBattleTransition();
        }
        if(keyH.saveFile){
            System.out.println("Saving...");
            SaveLoad.save(hero);
            System.out.println("Saved at out.sav!");
        }

        battle.checkBattle();

        if (battle != null){
            battle.update();
        }

    }

    public Hero loadHero(GamePanel gp, KeyHandler keyH) {
        Hero hero;
        Scanner input = new Scanner(System.in);
        System.out.println("Would you like to load a save? (y/n): ");
        String response = input.nextLine();
        if (response.equals("y") || response.equals("Y")) {
            System.out.println("Input your save file directory (ex: src/saves/mySav.sav)");
            response = input.nextLine();
            hero = SaveLoad.load(response, gp, keyH);

        }else {
            hero = HeroFactory.createDefaultHero(this, keyH);
        }

        return hero;
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        Graphics2D bufferG = gameBuffer.createGraphics();

        bufferG.setColor(getBackground());
        bufferG.fillRect(0, 0, screenWidth, screenHeight);

        // Draw out normal world


        for (int i = 0; i < enemy.length; i++){ // draws all enemies
            if (enemy[i] != null){
                enemy[i].draw(bufferG);
            }
        }

        // Tile
        tileM.draw(bufferG);

        // Player
        hero.drawHero(bufferG);

        // Object
        for(int i = 0; i<obj.length; i++ ){
            if (obj[i] != null){
                obj[i].draw(bufferG, this);
            }
        }

        // Create fog layer
        if (isInFoggyRegion) {
            BufferedImage fogLayer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D fogG = fogLayer.createGraphics();

            // Fill fog layer with black
            fogG.setColor(new Color(0, 0, 0));
            fogG.fillRect(0, 0, screenWidth, screenHeight);

            // Create gradient for smooth visibility circle
            RadialGradientPaint gradient = new RadialGradientPaint(
                    hero.screenX + tileSize / 2,
                    hero.screenY + tileSize / 2,
                    visibilityRadius,
                    new float[]{0.0f, 0.5f, 1.0f}, // Percent of circle
                    new Color[]{
                            new Color(0, 0, 0, 255), // Center is fully visible
                            new Color(0, 0, 0, 150), // Middle mostly visible
                            new Color(0, 0, 0, 50) // Edge is darkest
                    }
            );

            // Apply gradient to create visibility circle
            fogG.setComposite(AlphaComposite.DstOut);
            fogG.setPaint(gradient);
            fogG.fillOval(
                    hero.screenX + tileSize / 2 - visibilityRadius,
                    hero.screenY + tileSize / 2 - visibilityRadius,
                    visibilityRadius * 2,
                    visibilityRadius * 2
            );


            // Apply fog to the game buffer
            bufferG.setComposite(fogComposite);
            bufferG.drawImage(fogLayer, 0, 0, null);

            // Dispose fog first
            fogG.dispose();
        }

        // Draw the final composed buffer to the screen
        g2.drawImage(gameBuffer, 0, 0, null);

        // Clear memory
        bufferG.dispose();
        g2.dispose();
    }

    public void setVisibilityRadius(int radius){
        this.visibilityRadius = radius;
    }

    public void setFogDensity(float density){
        this.fogComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, density);
    }

    public void addFoggyRegion(int tileX, int tileY, int width, int height) {
        foggyRegions.add(new Rectangle(
                tileX * tileSize,
                tileY * tileSize,
                width * tileSize,
                height * tileSize
        ));
    }
    public void removeFoggyRegion(int index) {
        if (index >= 0 && index < foggyRegions.size()) {
            foggyRegions.remove(index);
        }
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();

    }

    public void stopMusic() {
        sound.stop();
    }

    public void playSE(int i){
        sound.setFile(i);
        sound.play();

    }

}
