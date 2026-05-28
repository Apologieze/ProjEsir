package entity;

import java.awt.*;
import java.awt.Graphics2D;
/**
 * Represents all enemy of the game
 */
public abstract class Enemy extends AnimatedEntity{
    private Player player;

    public Enemy(Player player, String folderPath){
        super(folderPath, 10);
        this.player = player;
        x = 200;
        y = 200;
    }

    public abstract void shoot();

    public abstract void draw(Graphics2D a_g2);

    public abstract void update();



}
