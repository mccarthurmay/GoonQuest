package Display;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean battleTestKey; // since enemies don't work, just made a set battle key
    public boolean weaponLeft, weaponRight;
    public boolean spacePressed;



    public void keyTyped(KeyEvent e) {}


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

        // Menu navigation (Arrow Keys)
        if (keyCode == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }

        // Battle test key
        if (keyCode == KeyEvent.VK_B) {
            battleTestKey = true;
        }

        // Action controls
        if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
    }

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

        // Menu navigation (Arrow Keys)
        if (keyCode == KeyEvent.VK_UP) {
            upPressed = false;
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            downPressed = false;
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (keyCode == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }

        // Battle test key
        if (keyCode == KeyEvent.VK_B) {
            battleTestKey = false;
        }

        // Action controls
        if (keyCode == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
    }
}