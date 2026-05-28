package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.GamePanel;
import main.KeyHandler;
import manager.ImageAssetManager;

/**
 * Dfintition du comportement d'un joueur
 *
 */
public class Player extends Entity{

	GamePanel gp;
	KeyHandler keyH;
	private BufferedImage idleImage;

	/**
	 * Constructeur de Player
	 * @param a_gp GamePanel, pannel principal du jeu
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(GamePanel a_gp, KeyHandler a_keyH) {
		this.gp = a_gp;
		this.keyH = a_keyH;
		this.setDefaultValues();
		this.idleImage = ImageAssetManager.loadImage("/player/superhero.png");
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
		BufferedImage l_image = idleImage;
		a_g2.drawImage(l_image, x, y, gp.TILE_SIZE, gp.TILE_SIZE, null);
	}
}