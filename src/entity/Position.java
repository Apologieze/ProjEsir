package entity;

public class Position {
    protected int x, y;				//position sur la map

    public void setPosition(int x,int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(int x) {
        return this.x;
    }

    public int getY(int y) {
        return this.y;
    }

}
