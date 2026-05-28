package UI;
import entity.Position;
import entity.Player;
import main.GamePanel;

import java.awt.Color;
import java.awt.Graphics2D;

public class XpBar extends Position implements HUD {
    private Player m_player;
    private GamePanel m_gp;

    private int m_largeur;
    private int m_hauteur;

    public XpBar(GamePanel gp, Player player, int x, int y) {
        this.setPosition(x, y); // Position de la barre sur l'écran
        this.m_player = player;
        this.m_hauteur = gp.TILE_SIZE;
        this.m_largeur = gp.TILE_SIZE * 5;
        this.m_gp = gp;
    }

    @Override
    public void draw(Graphics2D a_g2) {
        m_player.setXp(m_player.getXp() + 1);
        int currentXp = m_player.getXp();
        int maxXp = m_player.getNextLevelXp();

        double xpProgress = (double) currentXp / maxXp;

        int currentBarWidth = (int) (this.m_largeur * xpProgress);

        // Le fond de la barre
        a_g2.setColor(new Color(255, 255, 255));
        a_g2.fillRect(this.getX(), this.getY(), this.m_largeur, this.m_hauteur);

        // La barre
        a_g2.setColor(new Color(0, 255, 0));
        a_g2.fillRect(this.getX(), this.getY(), currentBarWidth, this.m_hauteur);

        //  Le contour
        a_g2.setColor(new Color(0, 0, 0));
        a_g2.drawRect(this.getX(), this.getY(), this.m_largeur, this.m_hauteur);
    }

}
