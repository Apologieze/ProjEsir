package entity;

import java.awt.image.BufferedImage;
import java.util.List;
import main.GamePanel;
import manager.SpellManager;

/**
 * Entité ennemie de base
 */
public abstract class Enemy extends AnimatedEntity {
    protected Player player;
    protected GamePanel gp;
    protected SpellManager spellManager;

    public int type;
    public int fireRate;
    protected int fireCooldown = 0;

    protected boolean isOnScreen = false;
    protected float entryDx, entryDy;

    // Remplacement du folderPath par la liste préchargée
    public Enemy(Player player, GamePanel gp, SpellManager spellManager, List<BufferedImage> preloadedFrames) {
        super(preloadedFrames, 10); // Appel de ton constructeur optimisé !
        this.player = player;
        this.gp = gp;
        this.spellManager = spellManager;
        this.setSize(gp.TILE_SIZE, gp.TILE_SIZE);
    }

    public abstract void shoot();
    public abstract void moveOnScreen();

    public void update() {
        updateAnimation();

        if (!isOnScreen) {
            move(entryDx, entryDy, speed);
            if (x >= 0 && x <= gp.SCREEN_WIDTH - width && y >= 0 && y <= gp.SCREEN_HEIGHT - height) {
                isOnScreen = true;
            }
        } else {
            moveOnScreen();

            if (x < 0) x = 0;
            if (x > gp.SCREEN_WIDTH - width) x = gp.SCREEN_WIDTH - width;
            if (y < 0) y = 0;
            if (y > gp.SCREEN_HEIGHT - height) y = gp.SCREEN_HEIGHT - height;
        }

        if (isOnScreen) {
            fireCooldown++;
            if (fireCooldown >= fireRate) {
                shoot();
                fireCooldown = 0;
            }
        }
    }

    public void setEntryDirection(float dx, float dy) {
        this.entryDx = dx;
        this.entryDy = dy;
    }
}