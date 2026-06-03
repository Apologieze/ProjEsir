package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;
import manager.SpellManager;

public class WaterEnemy extends Enemy {

    private float moveDirX = 1.0f;
    private float waveAngle = 0; // Utilisé pour calculer la vague

    public WaterEnemy(Player player, GamePanel gp, SpellManager spellManager, List<BufferedImage> preloadedFrames, int level) {
        super(player, gp, spellManager, preloadedFrames, level);

        this.type = 1;
        this.fireRate = 100; // Tire légèrement plus vite que la plante
        this.speed = 2;
        this.life = 2 * super.level;
    }

    @Override
    public void moveOnScreen() {
        // Mouvement de vague (oscillation sur l'axe Y)
        waveAngle += 0.05f;
        float offsetY = (float) Math.sin(waveAngle) * 1.5f;

        // Déplacement combiné (Horizontal + Vertical ondulé)
        move(moveDirX, offsetY, speed);

        // Rebond sur les bords de l'écran
        if (x <= 0) {
            moveDirX = 1.0f;
            this.speed = (this.speed*2)%6; // sa vitesse change
        } else if (x >= gp.SCREEN_WIDTH - width) {
            moveDirX = -1.0f;
            this.speed = (this.speed*2)%6; // sa vitesse change
        }
    }

    @Override
    public void shoot() {
        float distX = player.getCenterX() - this.getCenterX();
        float distY = player.getCenterY() - this.getCenterY();
        float distance = (float) Math.sqrt(distX * distX + distY * distY);

        float dirX = 0;
        float dirY = 1;

        if (distance != 0) {
            dirX = distX / distance;
            dirY = distY / distance;
        }

        spellManager.spawnEnemySpell(getCenterX(), getCenterY(), this.type, dirX, dirY);
    }
}