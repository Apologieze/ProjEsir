package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;

public class EnemySpell extends AnimatedEntity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    private Player player;

    // REMOVED 'final' so the pool can re-use and reassign the type
    public int type;

    private final float HITBOX_SCALE = 0.4f;

    public EnemySpell(GamePanel gp, Player player, List<BufferedImage> preloadedFrames, int animationSpeed, int type) {
        super(preloadedFrames, animationSpeed);
        this.gp = gp;
        this.player = player;
        this.type = type;
        this.speed = 4;
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE);
    }

    /**
     * Reconfigures the spell's properties before it is fired from the pool.
     */
    public void configure(int newType, List<BufferedImage> newFrames, int newAnimSpeed, int newMoveSpeed) {
        this.type = newType;
        this.frames = newFrames;
        this.animationSpeed = newAnimSpeed;
        this.speed = newMoveSpeed;
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX - (this.width / 2.0f);
        this.y = startY - (this.height / 2.0f);

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

        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
            return;
        }

        checkCollision();
    }

    private void checkCollision() {
        float deltaX = this.getCenterX() - player.getCenterX();
        float deltaY = this.getCenterY() - player.getCenterY();
        float distSq = (deltaX * deltaX) + (deltaY * deltaY);

        float myRadius = (this.width / 2.0f) * HITBOX_SCALE;
        float playerRadius = (player.getWidth() / 2.0f) * HITBOX_SCALE;

        float combinedRadius = myRadius + playerRadius;
        float combinedRadiusSq = combinedRadius * combinedRadius;

        if (distSq <= combinedRadiusSq) {
            player.setLife(player.getLife() - 1);
            this.deactivate();
            System.out.println("Player hit! Remaining life: " + player.getLife());
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}