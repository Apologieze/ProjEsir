package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;
import manager.SpellManager;

public class PlantEnemy extends Enemy {

    private float moveDirX = 1.0f;

    public PlantEnemy(Player player, GamePanel gp, SpellManager spellManager, List<BufferedImage> preloadedFrames, int level) {
        super(player, gp, spellManager, preloadedFrames, level);

        this.type = 0;
        this.fireRate = 120;
        this.speed = 2;
        this.life = 3 * super.level;
    }

    @Override
    public void moveOnScreen() {
        move(moveDirX, 0, speed);

        if (x <= 0) {
            moveDirX = 1.0f;
        } else if (x >= gp.SCREEN_WIDTH - width) {
            moveDirX = -1.0f;
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