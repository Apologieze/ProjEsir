package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Entité de base du jeu
 */
public abstract class Entity extends Position {
	protected int speed;               // Déplacement de l'entité
	protected int life;                // Point de vie de l'entité

	// Dimensions de l'entité pour l'affichage et les collisions
	protected int width;
	protected int height;

	// Image fixe pour les entités non animées
	public BufferedImage m_idleImage;

	public int getLife() {
		return life;
	}

	public void setLife(int newLife) {
		life = newLife;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public float getCenterX() {
		return this.x + (this.width / 2.0f);
	}

	public float getCenterY() {
		return this.y + (this.height / 2.0f);
	}


	//Dessine automatiquement l'entité fixe sur l'écra en utilisant sa position et ses dimensions
	public void draw(Graphics2D a_g2) {
		// Si l'image n'a pas été initialisée
		if (m_idleImage != null) {
			a_g2.drawImage(m_idleImage, (int)x, (int)y, width, height, null);
		}
	}
}