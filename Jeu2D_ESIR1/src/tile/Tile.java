package tile;

import java.awt.image.BufferedImage;

/**
 * 
 * Element graphique de la carte
 */
public class Tile {
	public BufferedImage m_image;		//image
	public boolean m_collision;			//début de gestion de collision entre éléments
	
	Tile(){
		m_collision = false;
	}
}
