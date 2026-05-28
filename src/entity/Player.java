package entity;

import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;


/**
 * Dfintition du comportement d'un joueur
 *
 */
public class Player extends AnimatedEntity{
	public final int SIZE = 16 * 3;
	KeyHandler keyH;
	private int xp;
	private int level;
	private int nextLevelXp;

	/**
	 * Constructeur de Player
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(KeyHandler a_keyH) {
		super("/bee/fire", 10);
		this.keyH = a_keyH;
		this.setDefaultValues();

		this.xp = 0;
		this.nextLevelXp = 100;
		this.level = 1;
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
	 * @param a_g2 Graphics2D
	 */
	public void draw(Graphics2D a_g2) {
		a_g2.drawImage(getCurrentFrame(), (int)x, (int)y, SIZE, SIZE, null);
	}

	public int getXp() {
		return this.xp;
	}

	public void setXp(int xp) {
		if (xp >= this.getNextLevelXp()) {
			this.level++;
			this.xp = (xp)%this.getNextLevelXp();
			manager.SoundAssetManager.playSE("levelup.wav");
		}
		else {
			this.xp = xp;
		}
	}

	public int getNextLevelXp() {
		return this.nextLevelXp;
	}

	public int getLevel() {
		return this.level;
	}
}