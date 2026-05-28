package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import UI.Text;
import main.KeyHandler;
import manager.ImageAssetManager;

/**
 * Définition du comportement d'un joueur
 *
 */
public class Player extends AnimatedEntity {
	public final int SIZE = 16 * 3;
	KeyHandler keyH;
	private int xp;
	private int level;
	private int nextLevelXp;
	private Text lvlUpText;
	private int element; // 0=fire, 1=grass, 2=water
	private int unlokedElement;
	private int frameCounterDamage;

	// Tableau contenant les animations préchargées pour les 3 éléments
	private List<BufferedImage>[] animations = new List[3];

	/**
	 * Constructeur de Player
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(KeyHandler a_keyH) {
		// On charge le feu par défaut via le constructeur parent
		super(ImageAssetManager.loadImagesFromFolder("/bee/fire"), 10);
		this.keyH = a_keyH;

		// Préchargement des 3 éléments en mémoire
		animations[0] = this.frames;
		animations[1] = ImageAssetManager.loadImagesFromFolder("/bee/grass");
		animations[2] = ImageAssetManager.loadImagesFromFolder("/bee/water");

		this.setDefaultValues();

		this.xp = 0;
		this.nextLevelXp = 100;
		this.level = 1;
		this.element = 0;
		this.unlokedElement = 1;
		this.setSize(SIZE, SIZE); // On définit la taille pour l'entité
	}

	/**
	 * Initialisation des données membres avec des valeurs par défaut
	 */
	protected void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		life = 5;
	}

	/**
	 * Mise à jour des données du joueur
	 */
	public void update() {
		if (frameCounterDamage > 0){
			frameCounterDamage--;
		}
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
		x = Math.max(0, Math.min(x, manager.SizeManager.SCREEN_WIDTH - SIZE));
		y = Math.max(0, Math.min(y, manager.SizeManager.SCREEN_HEIGHT - SIZE));

		tryNextElement();
	}

	/**
	 * Increment unlockedElement
	 */
	public void incUnlockedElement() {
		if (this.unlokedElement <= 3) {
			this.unlokedElement++;
		}
		else {
			System.out.println("tu as débloqué plus d'élément qu'il y en as ??");
		}
	}	
	
	/**
	 * Essaie de passer à l'élément suivant
	 */
	public void tryNextElement(){
		if (keyH.nextElementClicked) {
			setElement((getElement() + 1) % this.unlokedElement);
			keyH.nextElementClicked = false;
		}
	}
	
	/**
	 * Change l'élément actuel et met à jour l'animation associée
	 */
	public void setElement(int newElement) {
		this.element = newElement;
		// On remplace la liste d'images utilisée par AnimatedEntity
		this.frames = animations[newElement];
		this.currentFrameIndex = 0;
	}

	/**
	 * Affichage de l'image du joueur dans la fenêtre du jeu
	 * @param a_g2 Graphics2D
	 */
	public void draw(Graphics2D a_g2) {
		if (frameCounterDamage >= 0 && frameCounterDamage % 2 == 0)
		{

			// On dessine l'image
			//a_g2.drawImage(getCurrentFrame(), (int)x, (int)y, SIZE, SIZE, null);
			super.draw(a_g2);

			// On dessine le texte de level up s'il existe
			if (lvlUpText != null) {
				lvlUpText.draw(a_g2);

				if (!lvlUpText.isAlive()) {
					lvlUpText = null;
				}
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
			lvlUpText = new Text((int)x, (int)y - 10, "+1 ATK");
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

	public int getElement() {
		return this.element;
	}

	public void takeDamage(int damage){
		if (frameCounterDamage <= 0){
			life -= damage;
			frameCounterDamage = 60;
			manager.SoundAssetManager.playSE("Slap.wav");
		}
	}
}