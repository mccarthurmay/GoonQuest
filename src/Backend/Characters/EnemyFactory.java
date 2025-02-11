package Backend.Characters;

import Backend.Weapons.*;
import Backend.Items.*;
import Display.GamePanel;
import java.util.ArrayList;

public class EnemyFactory {

    /**
     * Create default enemy for display testing, similar to createDisplayTestHero
     */
    public static Enemy createDefaultEnemy(GamePanel gp) {
        // Create basic stats for default enemy
        Stats defaultStats = new Stats(15.0, 8.0, 500.0, 0.1, 0.75);
        Weapon defaultWeapon = new Weapon("Claws", 10, "Basic weapon", 1, 2, "Sharp claws", "src/Backend/Images/sprites/glargo_star.png");
        ArrayList<Item> defaultItems = new ArrayList<>();

        Enemy enemy = new Enemy(defaultStats, defaultWeapon, "Default Enemy", defaultItems, "");
        enemy.gp = gp;
        enemy.direction = "down";
        enemy.speed = 3;
        enemy.getImage();

        return enemy;
    }

}