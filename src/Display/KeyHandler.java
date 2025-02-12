package Display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    // Initialize variables
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean upArrow, downArrow, leftArrow, rightArrow;
    public boolean shiftPressed;
    public boolean spacePressed;
    public boolean saveFile; // save the game state
    public boolean enterPressed;
    public boolean qPressed;


    public void keyTyped(KeyEvent e) {}

    /**
     * Check if a key is in the "pressed" position
     * @param e the event to be processed
     */
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Movement controls (WASD)
        if (keyCode == KeyEvent.VK_W) {
            upPressed = true;
        }
        if (keyCode == KeyEvent.VK_S) {
            downPressed = true;
        }
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = true;
        }
        // Sprint
        if (keyCode == KeyEvent.VK_SHIFT){
            shiftPressed = true;
        }

        // Menu navigation (Arrow Keys)
        if (keyCode == KeyEvent.VK_UP) {
            upArrow = true;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downArrow = true;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            leftArrow = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightArrow = true;
        }

        // Save state
        if (keyCode == KeyEvent.VK_P) {
            saveFile = true;
        }

        // Action controls
        if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }

        if (keyCode == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }
        if (keyCode == KeyEvent.VK_Q) {
            qPressed = true;
        }
    }

    /**
     * Check if the key has been let go
     * @param e the event to be processed
     */
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        // Movement controls (WASD)
        if (keyCode == KeyEvent.VK_W) {
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_D) {
            rightPressed = false;
        }
        // Sprint
        if (keyCode == KeyEvent.VK_SHIFT){
            shiftPressed = false;
        }

        // Menu navigation (Arrow Keys)
        if (keyCode == KeyEvent.VK_UP) {
            upArrow = false;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downArrow = false;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            leftArrow = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightArrow = false;
        }

        // Save state
        if (keyCode == KeyEvent.VK_P) {
            saveFile = false;
        }

        // Action controls
        if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }

        // Action to confirm use of items in battlePanel
        if (keyCode == KeyEvent.VK_ENTER) {
            enterPressed = false;
        }

        // Used mostly for items layout purposes and get some players
        // coordinates
        if (keyCode == KeyEvent.VK_Q) {
            qPressed = false;
        }
    }
}