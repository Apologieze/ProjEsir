package entity;

/**
 * Entité de base du jeu
 *
 */
public abstract class Entity extends Position {
	protected int speed;					//Déplacement de l'entité
	protected int life;					// Point de vie de l'entité

	public int getLife() {
		return life;
	}

	public void setLife(int newLife) {
		life = newLife;
	}
}