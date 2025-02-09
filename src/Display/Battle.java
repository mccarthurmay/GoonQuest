package Display;

import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;

    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }

    public void checkBattle() {
        int enemyIndex = gp.collisionChecker.checkEnemy(gp.hero, gp.enemy);

        if(enemyIndex != 999) {
            // Create new battle panel
            BattlePanel battlePanel = new BattlePanel(gp);

            // Remove game panel and add battle panel
            window.remove(gp);
            window.add(battlePanel);

            // Need this to refresh and show new panel
            window.revalidate();
            window.repaint();
        }



    }

    // REMOVE THIS LATER
    public void testBattleTransition() {
        BattlePanel battlePanel = new BattlePanel(gp);
        window.remove(gp);
        window.add(battlePanel);
        window.revalidate();
        window.repaint();
    }
}