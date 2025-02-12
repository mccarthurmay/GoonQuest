package Display;

import Backend.Characters.Enemy;
import Backend.Characters.EnemyFactory;
import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;
    BattlePanel battlePanel;
    Enemy currentEnemy; // tracks current enemy

    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }

    public void checkBattle() {
        int enemyIndex = gp.collisionChecker.checkEnemy(gp.hero, gp.enemy);

        if(enemyIndex != 999) {
            gp.stopGameThread(); // Battle thread frozen if gamethread on
            // Create new battle panel
            currentEnemy = gp.enemy[0];
            BattlePanel battlePanel = new BattlePanel(gp, currentEnemy, window);

            // Remove game panel and add battle panel
            window.remove(gp);
            window.add(battlePanel);

            battlePanel.requestFocusInWindow(); // Need this to make keyboard work

            // Need this to refresh and show new panel
            window.revalidate();
            window.repaint();
        }



    }

    // REMOVE THIS LATER
    public void toBattleTransition(Enemy enemy) {
        this.currentEnemy = EnemyFactory.createDefaultEnemy(gp);
        BattlePanel battlePanel = new BattlePanel(gp, enemy, window);

        gp.stopGameThread(); // BattleThread was frozen if both threads on

        window.remove(gp);
        window.add(battlePanel);
        window.revalidate();
        window.repaint();
        battlePanel.requestFocusInWindow(); // Need this to make keyboard work
    }

    public void update(){
        if(battlePanel != null) {
            battlePanel.update();
        }
    }
}