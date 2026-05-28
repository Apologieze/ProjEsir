package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class PlayerSpell extends Entity implements ISpell {
    private boolean active = false;
    private BufferedImage image;
    private float dx, dy;
    private GamePanel gp;
    public final int type; // 0, 1 ou 2 pour les 3 types

    public PlayerSpell(GamePanel gp, BufferedImage preloadedImage, int type) {
        this.gp = gp;
        this.image = preloadedImage;
        this.type = type;
        this.speed = 6;
    }

    @Override
    public void spawn(float startX, float startY, float dirX, float dirY) {
        this.x = startX;
        this.y = startY;
        this.dx = dirX;
        this.dy = dirY;
        this.active = true;
    }

    @Override
    public void update() {
        if (!active) return;
        move(dx, dy, speed);

        // Logique de désactivation (sortie d'écran)
        if (x < 0 || x > gp.SCREEN_WIDTH || y < 0 || y > gp.SCREEN_HEIGHT) {
            deactivate();
        }
    }

    @Override
    public void draw(Graphics2D g2) {
        if (!active) return;
        g2.drawImage(image, (int)x, (int)y, gp.TILE_SIZE, gp.TILE_SIZE, null);
    }

    @Override
    public boolean isActive() { return active; }

    @Override
    public void deactivate() { this.active = false; }
}