package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Gestionnaire d'evenements (touche clavier)
 *
 */
public class KeyHandler implements KeyListener{

	public boolean upPressed, downPressed, leftPressed, rightPressed, spacePressed, nextElementPressed, nextElementClicked;
    public boolean wPressed; //test win
	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_Z) {
			upPressed = true;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = true;
		}
		if (code == KeyEvent.VK_Q) {
			leftPressed = true;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = true;
		}
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = true;
        }
		if (code == KeyEvent.VK_E) {
			nextElementPressed = true;
		}
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_W) { //test win
            wPressed = true;
        }
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();

		if (code == KeyEvent.VK_Z) {
			upPressed = false;
		}
		if (code == KeyEvent.VK_S) {
			downPressed = false;
		}
		if (code == KeyEvent.VK_Q) {
			leftPressed = false;
		}
		if (code == KeyEvent.VK_D) {
			rightPressed = false;
		}
        if (code == KeyEvent.VK_SPACE) {
            spacePressed = false;
        }
		if (code == KeyEvent.VK_E && nextElementPressed) {
			nextElementPressed = false;
			nextElementClicked = true;
		}
        if (e.getKeyCode() == java.awt.event.KeyEvent.VK_W) {//test win
            wPressed = false;
        }

	}
}