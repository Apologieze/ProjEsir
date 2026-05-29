package main;

import UI.HUD;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.JPanel;

import UI.Heart;
import UI.XpBar;
import UI.Icon;
import entity.Player;
import manager.SoundAssetManager;
import tile.TileManager;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.function.BiConsumer;
import java.util.ArrayList;
import entity.HeartItem;

/**
 * Panel principal du jeu contenant la map principale
 *
 */
public class GamePanel extends JPanel implements Runnable{

	//Paramètres de l'écran
	final int ORIGINAL_TILE_SIZE = 16; 							// une tuile de taille 16x16
	final int SCALE = 3; 										// echelle utilisee pour agrandir l'affichage
	public final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; 	// 48x48
	public final int MAX_SCREEN_COL = 16;
	public final int MAX_SCREE_ROW = 12; 					 	// ces valeurs donnent une resolution 4:3
	public final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL; // 768 pixels
	public final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREE_ROW;	// 576 pixels

	public ArrayList<HeartItem> heartList = new ArrayList<>();
	private int heartSpawnTimer = 0;
	// FPS : taux de rafraichissement
	int m_FPS;

	// Creation des differentes instances (Player, KeyHandler, TileManager, GameThread ...)
	KeyHandler m_keyH;
	Thread m_gameThread;
	Player m_player;
	TileManager m_tileM;
	HUD m_heart;
	HUD m_xpBar;
	HUD m_icon;

	public manager.EnemyManager m_enemyM;
	public manager.SpellManager m_spellM;
	private int enemySpawnTimer = 0;

    // Le callback accepte le score (Integer) et l'état de victoire (Boolean)
    private BiConsumer<Integer, Boolean> onGameEnd;
    private boolean isGameEndTriggered = false;

	public int levelNum = 1; // 1 = Forêt, 2 = Eau,
	/**
	 * Constructeur
	 */
    public GamePanel(BiConsumer<Integer, Boolean> onGameEnd) {
        this.onGameEnd = onGameEnd;
		m_FPS = 60;
		m_keyH = new KeyHandler();
		m_player = new Player(m_keyH);
		m_tileM = new TileManager(this);
		m_heart = new Heart(this, m_player, 20, SCREEN_HEIGHT - 20 - TILE_SIZE);
		m_xpBar = new XpBar(this, m_player, SCREEN_WIDTH - 20 - 5 * TILE_SIZE, SCREEN_HEIGHT - 20 - TILE_SIZE);
		m_icon = new Icon(this, m_player, SCREEN_WIDTH - 20 - 5 * TILE_SIZE, SCREEN_HEIGHT - 20 - TILE_SIZE);

		m_enemyM = new manager.EnemyManager(this, m_player);
		m_spellM = new manager.SpellManager(this, m_enemyM, m_player);
		m_enemyM.setSpellManager(m_spellM);
		m_player.setSpellManager(m_spellM);

		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(m_keyH);
		this.setFocusable(true);
		SoundAssetManager.playSE("Sting_em_up.wav");

	}

	/**
	 * Lancement du thread principal
	 */
	public void startGameThread() {
		m_gameThread = new Thread(this);
		m_gameThread.start();
	}

	public void run() {

		double drawInterval = 1000000000/m_FPS; // rafraichissement chaque 0.0166666 secondes
		double nextDrawTime = System.nanoTime() + drawInterval;

		while(m_gameThread != null) { //Tant que le thread du jeu est actif

			//Permet de mettre à jour les differentes variables du jeu
			this.update();

			//Dessine sur l'ecran le personnage et la map avec les nouvelles informations. la methode "paintComponent" doit obligatoirement être appelee avec "repaint()"
			this.repaint();

			//Calcule le temps de pause du thread
			try {
				double remainingTime = nextDrawTime - System.nanoTime();
				remainingTime = remainingTime/1000000;

				if(remainingTime < 0) {
					remainingTime = 0;
				}

				Thread.sleep((long)remainingTime);
				nextDrawTime += drawInterval;

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	/**
	 * Mise à jour des données des entités
	 */

    public void update() {
        // Cas de défaite : le joueur n'a plus de vie
        if (m_player.isDead()) {
            triggerEndGame(false);
            return;
        }

        // Cas de victoire
        // COMMANDE DE TEST : Déclenchement de la victoire via la touche W
        if (m_keyH.wPressed) {
            m_keyH.wPressed = false; // Consomme l'événement instantanément
            triggerEndGame(true);
            return;
        }

        m_tileM.update();
        m_player.update();
        m_spellM.update();
        m_enemyM.update();

		enemySpawnTimer++;
		if (enemySpawnTimer >= 90) {
			m_enemyM.spawnRandomEnemy(this.levelNum);
			//m_enemyM.spawnRandomPlant();
			enemySpawnTimer = 0;
		}

		for (int i = heartList.size() - 1; i >= 0; i--) {
			HeartItem heart = heartList.get(i);
			heart.update(); // Fait descendre le cœur et bouger sa hitbox

			// Si le cœur dépasse le bas de l'écran, on le supprime
			if (heart.getY() > SCREEN_HEIGHT) {
				heartList.remove(i);
			}
		}

		checkPlayerHeartCollisions();
	}

    /**
     * Centralise l'interruption du thread et la notification du résultat
     */
    private void triggerEndGame(boolean isVictory) {
        if (!isGameEndTriggered) {
            isGameEndTriggered = true;
            m_gameThread = null; // Arrêt de la boucle run()
            // Interruption de la musique de fond
            manager.SoundAssetManager.stopAllSounds();
            if (onGameEnd != null) {
                onGameEnd.accept(m_player.getTotalScore(), isVictory);
            }
        }
    }

	/**
	 * Affichage des éléments
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		m_tileM.draw(g2);
		m_heart.draw(g2);
		m_xpBar.draw(g2);
		m_icon.draw(g2);
		for (int i = 0; i < heartList.size(); i++) {
			heartList.get(i).draw(g2);
		}
		m_enemyM.draw(g2);
		m_spellM.draw(g2);
		m_player.draw(g2);
		g2.dispose();
	}

	private void checkPlayerHeartCollisions() {
		java.awt.Rectangle playerBounds = new java.awt.Rectangle(
				(int)m_player.getX(),
				(int)m_player.getY(),
				m_player.getWidth(),
				m_player.getHeight()
		);

		for (int i = heartList.size() - 1; i >= 0; i--) {
			HeartItem heart = heartList.get(i);

			if (playerBounds.intersects(heart.getHitbox())) {
				if (m_player.getLife() < 9) {
					manager.SoundAssetManager.playSE("soin.wav");
					m_player.setLife(m_player.getLife() + 1);


					if (m_player.getLife() > 8) {
						m_player.setLife(8);
					}
					heartList.remove(i);
				}
			}
		}
	}

	public void checkHeartDrop(int enemyX, int enemyY) {
		double rand = Math.random();

		// 5 % de chance de faire spawn un coeur e
		if (rand < 0.05) {
			heartList.add(new HeartItem(this, enemyX, enemyY));
		}
	}

	public void setLevelNum(int levelNum) {
		this.levelNum = levelNum;
	}

	public void nextLevelNum(){
		this.levelNum++;
	}
}