package entity;

import java.awt.image.BufferedImage;
import main.GamePanel;

public class PlayerSpell extends Entity implements ISpell {
    private boolean active = false;
    private float dx, dy;
    private GamePanel gp;

    public int type;

    public PlayerSpell(GamePanel gp, BufferedImage preloadedImage, int type) {
        this.gp = gp;
        this.m_idleImage = preloadedImage;
        this.type = type;
        this.speed = 10; // You might want to increase this so player bullets feel fast!
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE);
    }

    public void configure(int newType, BufferedImage newImage) {
        this.type = newType;
        this.m_idleImage = newImage;
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        // Center the spell perfectly
        this.x = startX - (this.width / 2.0f);
        this.y = startY - (this.height / 2.0f);
        this.active = true;

        // Simply set the direction to exactly what was asked (0, -1 for straight up)
        this.dx = dirX;
        this.dy = dirY;
    }

    @Override
    public void update() {
        if (!active) return;
        move(dx, dy, speed);

        // Deactivate if it goes off-screen
        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}