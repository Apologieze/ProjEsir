package entity;

import java.awt.*;
import javax.imageio.ImageIO;
import java.util.Objects;
import main.GamePanel;

public class HeartItem extends Entity {

    private Rectangle m_hitbox;
    private GamePanel m_gp;

    public HeartItem(GamePanel a_gp, int a_x, int a_y) {
        this.m_gp = a_gp; // On enregistre la référence du GamePanel
        this.x = a_x;
        this.y = a_y;
        this.speed = 2; // Vitesse à laquelle le cœur descend
        this.setSize(a_gp.TILE_SIZE, a_gp.TILE_SIZE);
        this.m_hitbox = new Rectangle(a_x, a_y, this.width, this.height);

        try {
            this.m_idleImage = ImageIO.read(Objects.requireNonNull(
                    getClass().getResourceAsStream("/ui/heart.png")
            ));
        } catch (Exception e) {
            System.err.println("Erreur chargement image coeur");
        }
    }

    public Rectangle getHitbox() {
        return m_hitbox;
    }

    public void update() {
        this.y += this.speed;
        this.m_hitbox.x = (int)this.x;
        this.m_hitbox.y = (int)this.y;
    }

    @Override
    public void draw(Graphics2D a_g2) {
        if (m_idleImage != null) {

            int screenX = (int)this.x;
            int screenY = (int)this.y;

            a_g2.drawImage(m_idleImage, screenX, screenY, width, height, null);
        }
    }
}