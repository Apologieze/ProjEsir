package ui;

import entity.Position;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import entity.Player;
import main.GamePanel;
import manager.ImageAssetManager;

public class Heart extends Position implements HUD {

    private GamePanel m_gp;
    private Player m_player;
    private int m_space = 35; // Espace entre chaque coeur
    private BufferedImage m_image;

    public Heart(GamePanel gp, Player player, int x, int y) {
        this.setPosition(x, y); // Met les positions du premier coeur
        this.m_gp = gp;
        this.m_player = player;

        this.m_image = ImageAssetManager.loadImage("/ui/heart.png");
    }

    @Override
    public void draw(Graphics2D a_g2) {
        int currentLife = m_player.getLife();

        for (int i = 0; i < currentLife; i++) {
            int space_x = (int)this.getX() + (i * m_space);
            a_g2.drawImage(m_image, space_x, (int)this.getY(), m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
        }
    }
}