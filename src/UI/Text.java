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
    private int m_maxTime; // 2 secondes à 60 FPS
    private int m_yOffset;
    private boolean m_alive;

    public Text(int x, int y, String a_text) {
        setPosition(x, y);
        this.m_text = a_text;
        this.m_maxTime = 60;
        this.m_timer = m_maxTime;
        this.m_yOffset = 0;
        this.m_alive = true;

    }

    @Override
    public void draw(Graphics2D a_g2) {
        if (!m_alive) return;

        // 1. Mise à jour de l'animation (Logique)
        m_timer--;
        m_yOffset -= 1; // Monte de 1 pixel par frame

        if (m_timer <= 0) {
            m_alive = false;
            return;
        }

        // 2. Calcul du fondu (Alpha entre 0 et 255)
        int alpha = (int) ((double) m_timer / m_maxTime * 255);
        if (alpha < 0) alpha = 0;

        // 3. Dessin du texte
        a_g2.setFont(new Font("Impact", Font.PLAIN, 20));
        a_g2.setColor(new Color(0, 0, 0));

        // Dessin à la position initiale + le décalage vers le haut
        a_g2.drawString(m_text, this.getX(), this.getY() + m_yOffset);
    }

    public boolean isAlive() {
        return m_alive;
    }
}