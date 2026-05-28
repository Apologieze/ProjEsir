package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;
import manager.EnemyManager;

public class PlayerSpell extends Entity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    private EnemyManager enemyManager;
    public final int type;

    public PlayerSpell(GamePanel gp, BufferedImage preloadedImage, int type, EnemyManager enemyManager) {
        this.gp = gp;
        this.m_idleImage = preloadedImage; // Utilise la variable de la classe mère Entity
        this.type = type;
        this.speed = 6;
        this.enemyManager = enemyManager;
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE); // Initialise la taille
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX;
        this.y = startY;
        this.active = true;

        Enemy target = enemyManager.getClosestEnemy(this.x, this.y);

        if (target != null) {
            // Calcule la distance entre le sort et l'ennemi
            float distX = target.getCenterX() - this.getCenterX();
            float distY = target.getCenterY() - this.getCenterY();
            float distance = (float) Math.sqrt(distX * distX + distY * distY);

            // Normalise le vecteur de direction (pour que le sort aille à vitesse constante)
            if (distance != 0) {
                this.dx = distX / distance;
                this.dy = distY / distance;
            }
        } else {
            // S'il n'y a aucun ennemi à l'écran, tire tout droit selon les inputs de base
            this.dx = dirX;
            this.dy = dirY;
        }
    }

    @Override
    public void update() {
        if (!active) return;

        move(dx, dy, speed);

        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}