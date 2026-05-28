package entity;

import java.util.List;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class EnemySpell extends AnimatedEntity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;
    public final int type;

    public EnemySpell(GamePanel gp, List<BufferedImage> preloadedFrames, int animationSpeed, int type) {
        super(preloadedFrames, animationSpeed); // Utilisation directe de ton nouveau constructeur !
        this.gp = gp;
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

        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    // draw() SUPPRIMÉ (Géré par AnimatedEntity)

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}