package entity;

import java.awt.*;
import main.GamePanel;
import java.awt.Graphics2D;



public class GrassEnemy extends Enemy{

    public final int SIZE = 16 * 3;
    public GrassEnemy(Player player){
        super(player, "/enemy/grass");

    }


    public void shoot(){
        //todo
    }

    public void draw(Graphics2D a_g2){
        a_g2.drawImage(getCurrentFrame(), (int)x, (int)y, SIZE, SIZE, null);
    }

    public void update(){
        updateAnimation();
    }
    
}
