package entity;

import java.awt.Graphics2D;

import main.GamePanel;
import main.KeyHandler;

/**
 * Dfintition du comportement d'un joueur
 *
 */
public class Player extends AnimatedEntity{
	GamePanel gp;
	KeyHandler keyH;
	private int xp;
	private int level;
	private int nextLevelXp;

	/**
	 * Constructeur de Player
	 * @param a_gp GamePanel, pannel principal du jeu
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(GamePanel a_gp, KeyHandler a_keyH) {
		super("/bee/fire", 10);
		this.gp = a_gp;
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

		x += xAxis * speed;
		y += yAxis * speed;
	}

	/**
	 * Affichage du l'image du joueur dans la fentre du jeu
	 * @param a_g2 Graphics2D
	 */
	public void draw(Graphics2D a_g2) {
		a_g2.drawImage(getCurrentFrame(), x, y, gp.TILE_SIZE, gp.TILE_SIZE, null);
	}

	public int getXp() {
		return this.xp;
	}

	public void setXp(int xp) {
		if (xp >= this.getNextLevelXp()) {
			this.level++;
			this.xp = (xp)%this.getNextLevelXp();
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