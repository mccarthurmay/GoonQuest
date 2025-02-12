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
        gp.stopGameThread();
        window.remove(gp);
        gp.stopMusic();
        gp.playMusic(2);
        BattlePanel battlePanel = new BattlePanel(gp, enemy, window);
        window.add(battlePanel);
        battlePanel.requestFocusInWindow();
        window.revalidate();
        window.repaint();
    }

    public void update(){
        if(battlePanel != null){
            battlePanel.update();
        }

    }

    }
