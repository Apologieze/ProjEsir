package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;
import manager.EnemyManager;

public class PlayerSpell extends Entity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    private EnemyManager enemyManager; // ADDED BACK
    private Player player;

    public int type;
    public int damage = 1; // Base damage of the spell

    public PlayerSpell(GamePanel gp, BufferedImage preloadedImage, int type, EnemyManager enemyManager, Player player) {
        this.gp = gp;
        this.m_idleImage = preloadedImage;
        this.type = type;
        this.enemyManager = enemyManager;
        this.speed = 10;
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE);
        this.player = player;
    }

    public void configure(int newType, BufferedImage newImage) {
        this.type = newType;
        this.m_idleImage = newImage;
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX - (this.width / 2.0f);
        this.y = startY - (this.height / 2.0f);
        this.active = true;
        this.dx = dirX;
        this.dy = dirY;
    }

    @Override
    public void update() {
        if (!active) return;
        move(dx, dy, speed);

        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
            return;
        }

        float spellRadius = this.width / 4.0f;
        Enemy hitEnemy = enemyManager.checkCollision(this.getCenterX(), this.getCenterY(), spellRadius);

        if (hitEnemy != null) {
            hitEnemy.takeDamage(this.damage * player.getLevel());
            this.deactivate();
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}