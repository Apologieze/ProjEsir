package entity;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Entité de base du jeu
 *
 */
public abstract class Entity {
	public int m_x, m_y;				//position sur la map
	public int m_speed;					//Déplacement de l'entité
	public BufferedImage m_idleImage;	//Une image de l'entité

    /**
     * R�cup�ration de l'image du personnage
     */
    public void getEntityImage(String name) {
        //gestion des expections
        try {
            m_idleImage = ImageIO.read(getClass().getResource(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}