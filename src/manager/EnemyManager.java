package manager;

import entity.Enemy;
import entity.PlantEnemy;
import entity.Player;
import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EnemyManager {
    private List<Enemy> enemies;
    private GamePanel gp;
    private Player player;
    private SpellManager spellManager;

    // Cache des animations
    private List<BufferedImage> plantFrames;

    public EnemyManager(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        this.enemies = new ArrayList<>();

        // Chargement des images UNE SEULE FOIS au lancement
        loadAssets();
    }

    // Le SpellManager est créé après l'EnemyManager, on l'injecte donc après via un setter
    public void setSpellManager(SpellManager spellManager) {
        this.spellManager = spellManager;
    }

    private void loadAssets() {
        plantFrames = ImageAssetManager.loadImagesFromFolder("/enemy/grass");
    }

    public void update() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.getLife() <= 0) {
                enemies.remove(i);
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Enemy e : enemies) {
            e.draw(g2);
        }
    }

    /**
     * Génère une Plante avec les images préchargées
     */
    public void spawnRandomPlant() {
        //System.out.println("AJOUT D'UNE PLANTE");
        // On donne la référence des images déjà en mémoire
        PlantEnemy plant = new PlantEnemy(player, gp, spellManager, plantFrames);

        int spawnZone = (int) (Math.random() * 3);
        float startX = 0, startY = 0;
        float entryDx = 0, entryDy = 0;

        switch (spawnZone) {
            case 0: // HAUT
                startX = (float) (Math.random() * (gp.SCREEN_WIDTH - gp.TILE_SIZE));
                startY = -gp.TILE_SIZE;
                entryDx = 0; entryDy = 1;
                break;
            case 1: // GAUCHE
                startX = -gp.TILE_SIZE;
                startY = (float) (Math.random() * (gp.SCREEN_HEIGHT / 2));
                entryDx = 1; entryDy = 0;
                break;
            case 2: // DROITE
                startX = gp.SCREEN_WIDTH;
                startY = (float) (Math.random() * (gp.SCREEN_HEIGHT / 2));
                entryDx = -1; entryDy = 0;
                break;
        }

        plant.setPosition(startX, startY);
        plant.setEntryDirection(entryDx, entryDy);

        enemies.add(plant);
    }

    public Enemy getClosestEnemy(float x, float y) {
        Enemy closest = null;
        float minDistanceSq = Float.MAX_VALUE;

        for (Enemy e : enemies) {
            float dx = e.getCenterX() - x;
            float dy = e.getCenterY() - y;
            float distSq = dx * dx + dy * dy;

            if (distSq < minDistanceSq) {
                minDistanceSq = distSq;
                closest = e;
            }
        }
        return closest;
    }
}