package UI;

import main.GamePanel;
import entity.EnemyBoss;
import java.awt.*;

public class BossBar implements HUD {

    private GamePanel m_gp;
    private EnemyBoss boss;
    private String name;
    private int x = 20;
    private int barWidth;
    private int currentBarWidth;
    private int maxLife;

    public BossBar(GamePanel gp, EnemyBoss boss, String name, int maxLife) {
        this.m_gp = gp;
        this.boss = boss;
        this.name = name;
        this.maxLife = maxLife;
        this.barWidth = m_gp.SCREEN_WIDTH - 40;
        this.currentBarWidth = this.barWidth;
    }
    //que si le boss prends des dégâts
    public void update() {
        if (boss.getLife()>0) { // Vérification que le boss est en vie
            float hpPercentage = (float) boss.getLife() / maxLife;
            if (hpPercentage < 0) {hpPercentage = 0;}
            this.currentBarWidth = (int) (this.barWidth * hpPercentage);
        }
    }

    @Override
    public void draw(Graphics2D a_g2) {
        Font fontUI = new Font("Impact", Font.BOLD, 29);
        a_g2.setFont(fontUI);
        a_g2.setColor(Color.WHITE);

        FontMetrics metrics = a_g2.getFontMetrics(fontUI);
        int textX = m_gp.SCREEN_WIDTH / 2 - metrics.stringWidth(name) / 2;
        a_g2.drawString(name, textX, 30);

        // Fond noir
        a_g2.setColor(Color.BLACK);
        a_g2.fillRect(x, 40, barWidth, 22);

        // Intérieur rouge (utilise la variable calculée dans update())
        a_g2.setColor(Color.RED);
        a_g2.fillRect(x, 40, currentBarWidth, 22);
    }
}