package Display;

import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;
    BattlePanel battlePanel;

    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }

    public void checkBattle() {
        int enemyIndex = gp.collisionChecker.checkEnemy(gp.hero, gp.enemy);

        if(enemyIndex != 999) {
            gp.stopGameThread(); // Battle thread frozen if gamethread on
            // Create new battle panel
            BattlePanel battlePanel = new BattlePanel(gp);

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
    public void testBattleTransition() {
        BattlePanel battlePanel = new BattlePanel(gp);

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