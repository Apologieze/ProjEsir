package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

/**
 * Dfintition du comportement d'un joueur
 *
 */
public class Player extends Entity{

	GamePanel m_gp;
	KeyHandler m_keyH;

	/**
	 * Constructeur de Player
	 * @param a_gp GamePanel, pannel principal du jeu
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(GamePanel a_gp, KeyHandler a_keyH) {
		this.m_gp = a_gp;
		this.m_keyH = a_keyH;
		this.setDefaultValues();
		this.getEntityImage("/player/superhero.png");
	}

	/**
	 * Initialisation des donnes membres avec des valeurs par dfaut
	 */
	protected void setDefaultValues() {
		m_x = 100;
		m_y = 100;
		m_speed = 4;
	}

	/**
	 * Mise  jour des donnes du joueur
	 */
	public void update() {
		int xAxis = 0;
		int yAxis = 0;

		if (m_keyH.upPressed) {
			yAxis -= 1;
		}
		if (m_keyH.downPressed) {
			yAxis += 1;
		}
		if (m_keyH.leftPressed) {
			xAxis -= 1;
		}
		if (m_keyH.rightPressed) {
			xAxis += 1;
		}

		m_x += xAxis * m_speed;
		m_y += yAxis * m_speed;
	}

	/**
	 * Affichage du l'image du joueur dans la fentre du jeu
	 * @param a_g2 Graphics2D
	 */
	public void draw(Graphics2D a_g2) {
		BufferedImage l_image = m_idleImage;
		a_g2.drawImage(l_image, m_x, m_y, m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
	}
}