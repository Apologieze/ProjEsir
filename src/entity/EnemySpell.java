package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;

public class EnemySpell extends AnimatedEntity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    private Player player; // Reference to the player for collision
    public final int type;

    private final float HITBOX_SCALE = 0.4f;

    public EnemySpell(GamePanel gp, Player player, List<BufferedImage> preloadedFrames, int animationSpeed, int type) {
        super(preloadedFrames, animationSpeed);
        this.gp = gp;
        this.player = player;
        this.type = type;
        this.speed = 4;
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE);
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX;
        this.y = startY;
        this.dx = dirX;
        this.dy = dirY;
        this.active = true;
        this.currentFrameIndex = 0;
    }

    @Override
    public void update() {
        if (!active) return;

        updateAnimation();
        move(dx, dy, speed);

        // 1. Check if the spell went off-screen
        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
            return; // Stop executing if deactivated
        }

        // 2. Check collision with the player
        checkCollision();
    }

    /**
     * Highly optimized circular collision check (no square roots)
     */
    private void checkCollision() {
        // Calculate distance between centers
        float deltaX = this.getCenterX() - player.getCenterX();
        float deltaY = this.getCenterY() - player.getCenterY();

        // Calculate squared distance
        float distSq = (deltaX * deltaX) + (deltaY * deltaY);

        // Calculate radii based on the entity width and the hitbox scale
        float myRadius = (this.width / 2.0f) * HITBOX_SCALE;
        float playerRadius = (player.getWidth() / 2.0f) * HITBOX_SCALE;

        // Calculate combined radius squared
        float combinedRadius = myRadius + playerRadius;
        float combinedRadiusSq = combinedRadius * combinedRadius;

        // If the squared distance is less than or equal to the squared combined radius, we have a hit!
        if (distSq <= combinedRadiusSq) {
            player.takeDamage(1);
            this.deactivate(); // The bullet disappears upon hitting the player
            System.out.println("Player hit! Remaining life: " + player.getLife());
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}