package Display;

import Backend.Characters.Enemy;

import javax.swing.*;

public class Battle {
    GamePanel gp;
    JFrame window;
    BattlePanel battlePanel;

    /**
     * Constructor for battle class
     * @param gp gamepanel to be drawn on
     * @param window the window that will be drawn inside of (we will be making a new gamepanel for battles)
     */
    public Battle(GamePanel gp, JFrame window) {
        this.gp = gp;
        this.window = window;
    }

    /**
     * Function to handle the transition between the overworld gamepanel and the battle gamepanel
     * @param enemy the enemy that will be fought in the battle (just one v one battles here)
     */
    public void toBattleTransition(Enemy enemy) {
        gp.stopGameThread(); // stop the overworld logic
        gp.stopMusic(); // stop the overworld music

        // Clean up old battle panel if it exists
        if (battlePanel != null) {
            battlePanel.cleanupResources();
            battlePanel = null;
        }

        window.remove(gp); // remove the current game panel
        gp.playMusic(2); // play the new music

        battlePanel = new BattlePanel(gp, enemy, window); // create a new battle panel object with our enemy, window, and game panel

        // Switch to battle panel
        window.add(battlePanel);
        battlePanel.requestFocusInWindow();
        window.revalidate();
        window.repaint();

    }

    /**
     * update the battle
     */
    public void update(){
        if (battlePanel != null) { // assuming the battle panel exists,
            battlePanel.update(); // update the battle panel
        }

    }

}
