package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;
import manager.SpellManager;

public class FireEnemy extends Enemy {

    public FireEnemy(Player player, GamePanel gp, SpellManager spellManager, List<BufferedImage> preloadedFrames, int level) {
        super(player, gp, spellManager, preloadedFrames, level);

        this.type = 2;
        this.fireRate = 75; // Tire plus vite
        this.speed = 1; // Se déplace lentement
        this.life = 5 * super.level;
    }

    @Override
    public void moveOnScreen() {
        //L'ennemi essaie de s'aligner horizontalement avec le joueur
        float distX = player.getCenterX() - this.getCenterX();

        if (Math.abs(distX) > 5) {
            float dirX = (distX > 0) ? 1.0f : -1.0f;
            // Ne se déplace que sur l'axe X
            move(dirX, 0, speed);
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