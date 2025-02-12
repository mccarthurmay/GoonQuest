package Display;

import Backend.Characters.Enemy;

import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;
    BattlePanel battlePanel;

    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }


    // REMOVE THIS LATER
    public void toBattleTransition(Enemy enemy) {
        gp.stopGameThread();
        gp.stopMusic();

        // Clean up old battle panel if it exists
        if (battlePanel != null) {
            battlePanel.cleanupResources();
            battlePanel = null;
        }

        window.remove(gp);
        gp.playMusic(2);

        battlePanel = new BattlePanel(gp, enemy, window);

        // Switch to battle panel
        window.add(battlePanel);
        battlePanel.requestFocusInWindow();
        window.revalidate();
        window.repaint();
    }

    public void update(){
        if (battlePanel != null) {
            battlePanel.update();
        }

    }

    }
