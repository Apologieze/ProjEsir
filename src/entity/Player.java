package entity;

import java.awt.Graphics2D;
import java.lang.classfile.instruction.ReturnInstruction;

import UI.Text;
import main.GamePanel;
import main.KeyHandler;


/**
 * Dfintition du comportement d'un joueur
 *
 */
public class Player extends AnimatedEntity {
	public final int SIZE = 16 * 3;
	KeyHandler keyH;
	private int xp;
	private int level;
	private int nextLevelXp;
	private Text lvlUpText;
	private int element; //0=fire, 1=grass, 2=water

	/**
	 * Constructeur de Player
	 *
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(KeyHandler a_keyH) {
		super("/bee/fire", 10);
		this.keyH = a_keyH;
		this.setDefaultValues();

		this.xp = 0;
		this.nextLevelXp = 100;
		this.level = 1;
		this.element = 0;
	}

	/**
	 * Initialisation des donnes membres avec des valeurs par dfaut
	 */
	protected void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		life = 5;
	}

	/**
	 * Mise  jour des donnes du joueur
	 */
	public void update() {
		updateAnimation();
		int xAxis = 0;
		int yAxis = 0;

		if (keyH.upPressed) {
			yAxis -= 1;
		}
		if (keyH.downPressed) {
			yAxis += 1;
		}
		if (keyH.leftPressed) {
			xAxis -= 1;
		}
		if (keyH.rightPressed) {
			xAxis += 1;
		}
		move(xAxis, yAxis, speed);
	}

	/**
	 * Affichage du l'image du joueur dans la fentre du jeu
	 *
	 * @param a_g2 Graphics2D
	 */
	public void draw(Graphics2D a_g2) {
		a_g2.drawImage(getCurrentFrame(), (int) x, (int) y, SIZE, SIZE, null);

		if (lvlUpText != null) {
			lvlUpText.draw(a_g2);


			if (!lvlUpText.isAlive()) {
				lvlUpText = null;
			}
		}
	}

	public int getXp() {
		return this.xp;
	}

	public void setXp(int xp) {
		if (xp >= this.getNextLevelXp()) {
			this.level++;
			this.xp = (xp) % this.getNextLevelXp();
			manager.SoundAssetManager.playSE("levelup.wav");
			this.nextLevelXp += this.getNextLevelXp() / 2;
			lvlUpText = new Text((int) x, (int) y - 10, "+5 ATK");
		} else {
			this.xp = xp;
		}
	}

	public int getNextLevelXp() {
		return this.nextLevelXp;
	}

	public int getLevel() {
		return this.level;
	}

	public int getElement() {return this.element;}
}