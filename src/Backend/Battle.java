package Backend;
import Backend.Characters.Enemy;
import Display.GamePanel;

public class Battle {
    boolean running = true;
    Enemy foe;
    GamePanel display;
    public Battle(Enemy foe, GamePanel display){ // import music at some point
        this.foe = foe;
        this.display = display;
    }


    public void run(){
        String choice;
        while (running) {
            System.out.println("That's right, it's fightin' time!");
            display.run();


        }

    }

}
