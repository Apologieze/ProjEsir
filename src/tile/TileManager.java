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
 * Gestionnaire des tiles du jeu avec défilement
 */
public class TileManager {
	GamePanel m_gp;
	Tile[] m_tile;
	int m_mapTileNum[][];
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
		this.loadMap("/maps/map_Tile Layer 1.csv");

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
	 * Lecture du fichier csv contenant la map de taille variable
	 */
	public void loadMap(String filePath) {
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

			m_mapMaxCol = lines.get(0).length;
			m_mapMaxRow = lines.size();
			m_mapTileNum = new int[m_mapMaxCol][m_mapMaxRow];

			for (int row = 0; row < m_mapMaxRow; row++) {
				String[] numbers = lines.get(row);
				for (int col = 0; col < m_mapMaxCol; col++) {
					m_mapTileNum[col][row] = Integer.parseInt(numbers[col]);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * Affichage de la carte visible à l'écran
	 * @param g2
	 */
	public void draw(Graphics2D g2) {
		for (int row = 0; row < m_mapMaxRow; row++) {
			for (int col = 0; col < m_mapMaxCol; col++) {

				int tileNum = m_mapTileNum[col][row];

				int worldX = col * m_gp.TILE_SIZE;
				int worldY = row * m_gp.TILE_SIZE;

				int screenY = worldY - (int) m_cameraY;

				if (screenY + m_gp.TILE_SIZE > 0 && screenY < m_gp.SCREEN_HEIGHT) {
					if (m_tile[tileNum] != null && m_tile[tileNum].m_image != null) {
						g2.drawImage(m_tile[tileNum].m_image, worldX, screenY, m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
					}
				}
			}
		}
	}
}