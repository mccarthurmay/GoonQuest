package Display;

import Backend.Characters.Enemy;

import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;
    BattlePanel battlePanel;
    Enemy currentEnemy; // tracks current enemy
    boolean battleState = false;

    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }


    // REMOVE THIS LATER
    public void toBattleTransition(Enemy enemy) {
        BattlePanel battlePanel = new BattlePanel(gp, enemy, window);
        gp.stopGameThread(); // BattleThread was frozen if both threads on

        window.remove(gp);
        window.add(battlePanel);
        window.revalidate();
        window.repaint();
        battlePanel.requestFocusInWindow(); // Need this to make keyboard work
    }

    public void update(){
            battlePanel.update();

    }

    }
