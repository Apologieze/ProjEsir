package UI;

import main.GamePanel;
import entity.EnemyBoss;
import UI.Text;

import java.awt.*;
import java.awt.FontMetrics;

public class BossBar implements HUD{

    private GamePanel m_gp;
    private EnemyBoss boss;
    private String name;
    int x;
    int y = 20;
    int maxLife;


    public BossBar (GamePanel gp, EnemyBoss boss, String name, int maxLife) {
        this.m_gp = gp;
        this.boss = boss;
        this.name = name;
        this.x = m_gp.SCREEN_WIDTH/2;
        this.maxLife = maxLife;
    }

    @Override
    public void draw(Graphics2D a_g2) {
        //texte
        Font fontUI = new Font("Impact", Font.BOLD, 25);
        a_g2.setFont(fontUI);
        a_g2.setColor(new Color(255, 255, 255));
        FontMetrics metrics = a_g2.getFontMetrics(fontUI);
        a_g2.drawString(name, x, y);

        //rectangle
        a_g2.setColor(new Color(0,0,0));
        //contour
        a_g2.fillRect(x, 50, m_gp.SCREEN_WIDTH-40, 22);
        a_g2.setColor(new Color(255, 0, 0));
        //interieur
        a_g2.fillRect(x, 50, (m_gp.SCREEN_WIDTH-40) * (boss.getLife()/maxLife), 22);

    }
}
