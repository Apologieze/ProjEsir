package ui;

import entity.Player;
import entity.Position;
import main.GamePanel;
import manager.ImageAssetManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Icon extends Position implements HUD {

    private GamePanel m_gp;
    private Player m_player;
    private BufferedImage m_image_fire;
    private BufferedImage m_image_water;
    private BufferedImage m_image_grass;

    public Icon(GamePanel gp, Player player, int x, int y) {
        this.setPosition(x, y); // Met les positions du premier coeur
        this.m_gp = gp;
        this.m_player = player;

        this.m_image_fire = ImageAssetManager.loadImage("/ui/fire_icon.png");
        this.m_image_water = ImageAssetManager.loadImage("/ui/water_icon.png");
        this.m_image_grass = ImageAssetManager.loadImage("/ui/grass_icon.png");
    }

    @Override
    public void draw(Graphics2D a_g2) {
        int element = m_player.getElement();
        if(element==0) {
            a_g2.drawImage(m_image_fire, (int)this.getX(), (int)this.getY(), m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
        }
        else if(element==1) {
            a_g2.drawImage(m_image_grass, (int)this.getX(), (int)this.getY(), m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
        }
        else if(element==2) {
            a_g2.drawImage(m_image_water, (int)this.getX(), (int)this.getY(), m_gp.TILE_SIZE, m_gp.TILE_SIZE, null);
        }
    }
}