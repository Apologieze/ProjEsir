package manager;

/**
 * Gestionnaire statique possédant des dimensions globales pour le jeu.
 */
public class SizeManager {

    public static final int ORIGINAL_TILE_SIZE = 16;
    public static final int SCALE = 3;
    public static final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 48x48 pixels
    public static final int MAX_SCREEN_COL = 16;
    public static final int MAX_SCREEN_ROW = 12;

    public static final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
    public static final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;
}