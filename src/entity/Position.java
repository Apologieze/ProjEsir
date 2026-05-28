package entity;

/**
 * Represents a 2D coordinate on the game map and handles its movements.
 */
public class Position {

    /**
     * The X and Y coordinates of the entity on the map.
     */
    protected float x, y;				//position sur la map

    /**
     * Instantly relocates the entity to the specified coordinates.
     *
     * @param x The new X coordinate on the map
     * @param y The new Y coordinate on the map
     */
    public void setPosition(float x,float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the current horizontal coordinate of the entity.
     *
     * @return The current X position
     */
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }


    /**
     * Updates the position according to a direction (dx, dy) and a speed.
     * @param dx, the x coord direction
     * @param dy, the y coord direction
     * @param speed, how much big is the update
     */
    public void move(float dx, float dy, float speed){

        if (dx == 0.0f && dy == 0.0f) {
            return;
        }
        if (dx != 0.0f && dy != 0.0f) {
            float diagonalSpeed = speed * 0.70710678f; // pas le meme calcule pour la diag
            this.x += dx * diagonalSpeed;
            this.y += dy * diagonalSpeed;
        }
        else {
            this.x += dx * speed;
            this.y += dy * speed;
        }
    }

}
