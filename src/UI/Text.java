package UI;

import entity.Position;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * Un texte d'UI qui flotte vers le haut et disparaît avec un fondu
 */
public class Text extends Position implements HUD {

    private String m_text;
    private int m_timer;
    private int m_maxTime;
    private int m_yOffset;
    private boolean m_alive;
    private int taille;

    public Text(int x, int y, String a_text, int taille, int maxTime) {
        setPosition(x, y);
        this.m_text = a_text;
        this.m_maxTime = maxTime;
        this.m_timer = m_maxTime;
        this.m_yOffset = 0;
        this.m_alive = true;
        this.taille = taille;

    }

    @Override
    public void draw(Graphics2D a_g2) {
        if (!m_alive) return;

        // 1. Mise à jour de l'animation
        m_timer--;
        m_yOffset -= 1; // Monte de 1 pixel par frame

        if (m_timer <= 0) {
            m_alive = false;
            return;
        }

        // Calcul du fondu
        int alpha = (int) ((double) m_timer / m_maxTime * 255);
        if (alpha < 0) alpha = 0;

        // Dessin du texte
        a_g2.setFont(new Font("Impact", Font.PLAIN, this.taille));
        a_g2.setColor(new Color(0, 0, 0, alpha));

        // Dessin à la position initiale + le décalage vers le haut
        a_g2.drawString(m_text, this.getX(), this.getY() + m_yOffset);
    }

    public boolean isAlive() {
        return m_alive;
    }
}