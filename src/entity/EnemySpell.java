package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;

public class EnemySpell extends AnimatedEntity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    public final int type; // 0, 1 ou 2

    public EnemySpell(GamePanel gp, List<BufferedImage> preloadedFrames, int animationSpeed, int type) {
        super(preloadedFrames, animationSpeed);
        this.gp = gp;
        this.type = type;
        this.speed = 4;
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX;
        this.y = startY;
        this.dx = dirX;
        this.dy = dirY;
        this.active = true;
        this.currentFrameIndex = 0; // Réinitialisation de l'animation
    }

    @Override
    public void update() {
        if (!active) return;
        updateAnimation();
        move(dx, dy, speed);

        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;
        g2.drawImage(getCurrentFrame(), (int)x, (int)y, gp.TILE_SIZE, gp.TILE_SIZE, null);
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}