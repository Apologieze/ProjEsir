package tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import main.GamePanel;
import manager.ImageAssetManager;

/**
 * Gestionnaire des tiles du jeu avec défilement et double couche (Base + Détails)
 */
public class TileManager {
	GamePanel m_gp;
	Tile[] m_tile;

	// Tableaux pour les deux couches
	int m_mapTileNum[][];       // Couche de base (sol)
	int m_overlayTileNum[][];   // Couche de détails (arbres, buissons, etc.)

	int m_mapMaxCol;
	int m_mapMaxRow;

	public double m_cameraY;
	public double m_scrollSpeed = 1.5;

	/**
	 * Constructeur
	 * @param gp panel du jeu principal
	 */
	public TileManager(GamePanel gp) {
		this.m_gp = gp;

		this.getTileImage();

		m_mapTileNum = this.loadMap("/maps/forest/forest1.csv");
		m_overlayTileNum = this.loadMap("/maps/forest/forest2.csv"); // Change le nom selon ton fichier

		m_cameraY = (m_mapMaxRow * m_gp.TILE_SIZE) - m_gp.SCREEN_HEIGHT;
	}

	/**
	 * Chargement de toutes les tuiles du jeu depuis le spritesheet
	 */
	public void getTileImage() {
		List<BufferedImage> sprites = ImageAssetManager.loadSpritesheet("/tileset/punyworld_tileset.png", 16, 16);

		m_tile = new Tile[sprites.size()];

		for (int i = 0; i < sprites.size(); i++) {
			m_tile[i] = new Tile();
			m_tile[i].m_image = sprites.get(i);
		}
	}

	/**
	 * Lecture du fichier csv et renvoie le tableau 2D généré
	 */
	public int[][] loadMap(String filePath) {
		int[][] mapArray = null;
		try {
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			List<String[]> lines = new ArrayList<>();
			String line;

			while ((line = br.readLine()) != null) {
				String[] numbers = line.split(",");
				lines.add(numbers);
			}
			br.close();

			// On définit les dimensions (on suppose que toutes les couches ont la même taille)
			m_mapMaxCol = lines.get(0).length;
			m_mapMaxRow = lines.size();
			mapArray = new int[m_mapMaxCol][m_mapMaxRow];

			for (int row = 0; row < m_mapMaxRow; row++) {
				String[] numbers = lines.get(row);
				for (int col = 0; col < m_mapMaxCol; col++) {
					mapArray[col][row] = Integer.parseInt(numbers[col]);
				}
			}

		} catch (Exception e) {
			System.err.println("Erreur de chargement pour la map : " + filePath);
			e.printStackTrace();
		}
		return mapArray;
	}

	/**
	 * Mise à jour de la position de la caméra pour le défilement
	 */
	public void update() {
		m_cameraY -= m_scrollSpeed;

		// Si la caméra atteint le sommet de la map...
		if (m_cameraY <= 0) {
			m_cameraY = (m_mapMaxRow * m_gp.TILE_SIZE) - m_gp.SCREEN_HEIGHT;
		}
	}

	/**
	 * Affichage de la carte visible à l'écran (Couche 1 puis Couche 2)
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		for (int row = 0; row < m_mapMaxRow; row++) {
			for (int col = 0; col < m_mapMaxCol; col++) {

				// Calcul de la position à l'écran pour cette ligne
				int worldY = row * m_gp.TILE_SIZE;
				int screenY = worldY - (int) m_cameraY;

				// On vérifie d'abord si la ligne entière est visible à l'écran
				if (screenY + m_gp.TILE_SIZE > 0 && screenY < m_gp.SCREEN_HEIGHT) {

					int worldX = col * m_gp.TILE_SIZE;

					// --- 1. Dessin de la couche de base ---
					int tileNumBase = m_mapTileNum[col][row];
					// Sécurité optionnelle au cas où il y aurait aussi des trous dans la map de base
					if (tileNumBase != -1 && m_tile[tileNumBase] != null && m_tile[tileNumBase].m_image != null) {
						g2.drawImage(m_tile[tileNumBase].m_image, worldX, screenY, m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
					}

					// --- 2. Dessin de la couche de détails (Optimisé pour ignorer le vide) ---
					int tileNumOverlay = m_overlayTileNum[col][row];
					// OPTIMISATION : on zappe totalement le traitement si on rencontre -1
					if (tileNumOverlay != -1) {
						if (m_tile[tileNumOverlay] != null && m_tile[tileNumOverlay].m_image != null) {
							g2.drawImage(m_tile[tileNumOverlay].m_image, worldX, screenY, m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
						}
					}

				}
			}
		}
	}
}