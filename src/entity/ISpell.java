package entity;

import java.awt.Graphics2D;

public interface ISpell {
    void update();
    void draw(Graphics2D g2);
    boolean isActive();
    void deactivate();
    void spawn(float startX, float startY, float dirX, float dirY);
}