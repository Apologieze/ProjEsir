package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import UI.Text;
import main.KeyHandler;
import manager.ImageAssetManager;
import manager.SpellManager;

/**
 * Définition du comportement d'un joueur
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
	private int damage;


	private SpellManager spellManager;
	private int fireRate = 15; // Le delai entre chaque tire
	private int fireCooldown = 0;

	// Tableau contenant les animations préchargées pour les 3 éléments
	private List<BufferedImage>[] animations = new List[3];

	/**
	 * Constructeur de Player
	 * @param a_keyH KeyHandler, gestionnaire des touches
	 */
	public Player(KeyHandler a_keyH) {
		super(ImageAssetManager.loadImagesFromFolder("/bee/fire"), 10);
		this.keyH = a_keyH;

		animations[0] = this.frames;
		animations[1] = ImageAssetManager.loadImagesFromFolder("/bee/grass");
		animations[2] = ImageAssetManager.loadImagesFromFolder("/bee/water");

		this.setDefaultValues();

		this.xp = 0;
		this.nextLevelXp = 50;
		this.level = 1;
		this.element = 0;
		this.unlokedElement = 3;
		this.setSize(SIZE, SIZE);
		damage = 1;
	}

	/**
	 * Permet d'injecter le SpellManager après la création du joueur
	 */
	public void setSpellManager(SpellManager spellManager) {
		this.spellManager = spellManager;
	}

	protected void setDefaultValues() {
		x = 100;
		y = 100;
		speed = 4;
		life = 5;
	}

	public void update() {
		// Damage immunity timer
		if (frameCounterDamage > 0){
			frameCounterDamage--;
		}

		// Shooting cooldown timer
		if (fireCooldown > 0) {
			fireCooldown--;
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

		if (keyH.spacePressed && fireCooldown <= 0 && spellManager != null) {
			shoot();
		}
	}

	/**
	 * Gère la création du sort du joueur
	 */
	private void shoot() {
		spellManager.spawnPlayerSpell(getCenterX(), getCenterY(), this.element, 0, -1);

		// Reinitialise le cooldown
		fireCooldown = fireRate;
	}

	public void incUnlockedElement() {
		if (this.unlokedElement <= 3) {
			this.unlokedElement++;
		}
		else {
			System.out.println(" erreur, tu as débloqué plus d'élément qu'il y en as");
		}
	}

	public void tryNextElement(){
		if (keyH.nextElementClicked) {
			setElement((getElement() + 1) % this.unlokedElement);
			keyH.nextElementClicked = false;
		}
	}

	public void setElement(int newElement) {
		this.element = newElement;
		this.frames = animations[newElement];
		this.currentFrameIndex = 0;
	}

	public void draw(Graphics2D a_g2) {
		// Clignotement lors des dégats
		if (frameCounterDamage >= 0 && frameCounterDamage % 2 == 0) {
			super.draw(a_g2);

			if (lvlUpText != null) {
				lvlUpText.draw(a_g2);

				if (!lvlUpText.isAlive()) {
					lvlUpText = null;
				}
			}
		}
	}

	public int getXp() { return this.xp; }

	public void addXp(int xp){setXp(this.xp+xp);}

	public void setXp(int xp) {
		if (xp >= this.getNextLevelXp()) {
			this.level++;
			addDamage(1); //damage++
			this.xp = (xp) % this.getNextLevelXp();
			manager.SoundAssetManager.playSE("levelup.wav");
			this.nextLevelXp += this.getNextLevelXp() / 2;
			lvlUpText = new Text((int)x, (int)y - 10, "+1 ATK", 20, 60);
		}
		else {
			this.xp = xp;
		}
	}

	public int getNextLevelXp() { return this.nextLevelXp; }
	public int getLevel() { return this.level; }
	public int getElement() { return this.element; }

	public void takeDamage(int damage){
		if (frameCounterDamage <= 0){
			life -= damage;
			frameCounterDamage = 60;
			manager.SoundAssetManager.playSE("Slap.wav");
		}
	}

	public int getTotalScore(){
		return (level-1)*(getNextLevelXp())+xp;
	}

	// Si le joueur est mort
	public boolean isDead(){
		return life <= 0;
	}

	public void setDamage(int damage){
		this.damage = damage;
	}

	// le joueur prend des degats
	public void addDamage(int damage){
		this.damage += damage;
	}
}