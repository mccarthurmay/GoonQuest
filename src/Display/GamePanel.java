package Display;
import Backend.Characters.*;
import Backend.ObjectsRendering.Superobjects;
import Backend.SaveLoad;
import Backend.WorldBuilding.TileManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

// Runnable allows us to implement Thread objects
public class GamePanel extends JPanel implements Runnable {


    // Screen settings
    public final int originalTileSize = 20; // 20x20 tiles
    public final int scale = 3;
    public final int tileSize = originalTileSize * scale; // 60x60 tiles
    public final int maxScreenCol= 16;
    public final int maxScreenRow= 12;
    public final int screenWidth = maxScreenCol * tileSize; // 960 pixels
    public final int screenHeight = maxScreenRow * tileSize; // 720 pixels

    // World Settings
    public final int maxWorldCol = 199;
    public final int maxWorldRow = 199;;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound sound = new Sound();
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);

    // Allows us to use the concept of time in the game, in order to update the
    // drawings constantly at a certain speed
    Thread gameThread;

    public Hero hero = loadHero(this, keyH);
    public Superobjects obj[] = new Superobjects[30];


    // Composite controls how pixels overlap existing pixels
    // Src_over draws pixels on top of existing, taking in account of transparency
    // DstOut makes existing pixels more transparent where drawn (cut holes)\
    private AlphaComposite fogComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f);
    private int visibilityRadius = 200;

    // Add a buffer for entire game -- no buffer causes flickering
    private BufferedImage gameBuffer;

    // Add fields for fog regions
    private ArrayList<Rectangle> foggyRegions;
    private boolean isInFoggyRegion = false;

    // Initialize a battle object
    public Battle battle;

    // Initialize tutorial bool
    public static boolean isFirstEnemy = true;

    public GamePanel() {

        // Sets the size of this class
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.WHITE);

        // If set to true, all the drawings from this component will
        // be done offscreen
        // Painting buffer
        this.setDoubleBuffered(true);

        // Adds keyListener so that this gamePanel can recognize inputs
        this.addKeyListener(keyH);

        // With this, gamePanel can be focused/ready to receive input
        this.setFocusable(true);

        // Initialize game buffer
        gameBuffer = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);

        // Define foggy regions with coordinates
        foggyRegions = new ArrayList<>();
        addFoggyRegion(1,140,60,54);
    }

    // Check if player is in fog
    private boolean checkFoggyRegion() {
        Point playerWorldPos = new Point(hero.worldX, hero.worldY);

        for (Rectangle region : foggyRegions) {
            if (region.contains(playerWorldPos)) {
                return true;
            }
        }
        return false;
    }

    // Add things to the game world, play music, etc.
    public void setupGame() {
        aSetter.setThings();
        playMusic(0 );
    }

    public void startGameThread() {
        /**
         * In order to instantiate a thread object, we need to pass a GamePanel object
         */
        if (gameThread == null) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }


    /**
     * In this loop, we will constantly be updating the character position and
     * draw the sprites on the scree. Also, this function is necessary to use when working with Thread objects.
     * In this method we create a Game loop.
     */
    public void run(){

        /**
         * These following variable help set time intervals that slow down
         * the number of times we update our program per second.
         * We restrict it to only update 60 times per second.
         */
        int FPS = 60;
        double drawInterval = (double) 1000000000 /FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        /**
         * As long as this gameThreadExists, it represents the process that
         * is written inside this bracket.
         */
        while(gameThread != null) {

            /**
             * Delta is essentially a timer that signals the computer
             * when to update and redraw the screen.
             * it slowly approaches one and once it reaches a value of one, it signals it. git
             */
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if(delta >= 1) {

                // Updates information as character position
                update();

                // Draw the screen with the updated information
                repaint();
                delta--;
            }

        }
    }

    /**
     * End gamethread for switching between battle and overworld gamepanels
     */
    public void stopGameThread() {
        gameThread = null;
        try {
            // Give time for thread to properly terminate
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * update the world conditions
     */
    public void update() {
        hero.update();
        isInFoggyRegion = checkFoggyRegion();

        if(keyH.qPressed){
            System.out.println(hero.worldX + " " + hero.worldY);
            keyH.qPressed = false;
        }
        if(keyH.saveFile){
            System.out.println("Saving...");
            SaveLoad.save(hero);
            System.out.println("Saved at out.sav!");
            keyH.saveFile = false;
        }

        if (battle != null){
            battle.update();
        }

    }

    /**
     * Load hero object from file
     * @param gp GamePanel that will be passed into the hero
     * @param keyH keyhandler that will be passed into the hero
     * @return the newly loaded hero!
     */
    public Hero loadHero(GamePanel gp, KeyHandler keyH) {
        Hero hero = CharacterFactory.createDefaultHero(this, keyH);;
        Scanner input = new Scanner(System.in); // create a scanner to read files from the system
        System.out.println("Would you like to load a save? (y/n): "); // prompt user for a save
        String response = input.nextLine();
        if (response.equals("y") || response.equals("Y")) {
            System.out.println("Input your save file directory (ex: out.sav)"); // out.sav is the default
            response = input.nextLine(); // take in the user response
            hero = SaveLoad.load(response, gp, keyH); // load the information from that save file

        }else { // if no file to load from, create default hero
            hero = CharacterFactory.createDefaultHero(this, keyH);
        }
        return hero;
    }

    /**
     * Build in method in java, standard method to draw on JPanel
     * @Graphics: A class that has the necessary functions to draw on the screen
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        /**
         * Graphics2D class extends the Graphic class to provide more sophisticated control
         * over geometry, coordinate transformations, color management, and text layout.
         */
        Graphics2D g2 = (Graphics2D) g;

        Graphics2D bufferG = gameBuffer.createGraphics();

        /**
         * This method typically returns the background color
         * of the component that contains bufferG. It is inherited from Component in AWT/Swing.
         */
        bufferG.setColor(getBackground());
        bufferG.fillRect(0, 0, screenWidth, screenHeight);

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

    /**
     * create a foggy region (currently only over the forest maze)
     * @param tileX X coordinate of top left corner of the fog box
     * @param tileY Y coordinate of top left corner
     * @param width width of the fog box
     * @param height height of the fog box
     */
    public void addFoggyRegion(int tileX, int tileY, int width, int height) {
        foggyRegions.add(new Rectangle(
                tileX * tileSize,
                tileY * tileSize,
                width * tileSize,
                height * tileSize
        ));
    }

    /**
     * method to play music file
     * @param i the index of the song to play (songs must be manually added in the Sound class)
     */
    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();

    }

    /**
     * stop playing music
     */
    public void stopMusic() {
        sound.stop();
    }

    /**
     * play a sound effect (without looping it)
     * @param i index of the sound effect to be played
     */
    public void playSE(int i){
        sound.stop();
        sound.setFile(i);
        sound.play();
        Timer timer = new Timer(2500, e -> playMusic(0));
        timer.setRepeats(false);
        timer.start();

    }

}
