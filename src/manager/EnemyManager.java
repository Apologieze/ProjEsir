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

    public int generateLevelDrop() {
        int playerLvl = player.getLevel();
        double rand = Math.random(); // Génère un nombre entre 0.0 et 1.0

        // Cas particulier : Si le joueur est niveau 1, il ne peut pas avoir "1 de moins"
        if (playerLvl == 1) {
            // On redistribue les 15% du niveau inférieur au niveau actuel (50% + 15% + 10% restants = 75%)
            if (rand < 0.25) {
                return playerLvl + 1; // 25% de chance d'avoir le niveau supérieur
            } else {
                return playerLvl;     // 75% de chance d'avoir le niveau 1
            }
        }

        // Cas général (Joueur niveau 2 ou plus)
        if (rand < 0.25) {
            return playerLvl + 1;     // 25% de chance d'avoir (Niveau + 1)
        } else if (rand < 0.40) {     // 0.25 + 0.15 = 0.40
            return playerLvl - 1;     // 15% de chance d'avoir (Niveau - 1)
        } else {
            return playerLvl;         // 60% de chance d'avoir le niveau actuel (50% de base + 10% restants)
        }
    }

    /**
     * Génère une Plante avec les images préchargées
     */
    public void spawnRandomPlant() {
        //System.out.println("AJOUT D'UNE PLANTE");
        // On donne la référence des images déjà en mémoire
        PlantEnemy plant = new PlantEnemy(player, gp, spellManager, plantFrames, this.generateLevelDrop());

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

    /**
     * Vérification de collision optimisée pour les sorts du joueur.
     * Renvoie l'Enemy touché, ou null si rien n'est touché.
     */
    public Enemy checkCollision(float spellCx, float spellCy, float spellRadius) {
        for (Enemy e : enemies) {
            // On ignore simplement les ennemis déjà morts
            if (e.getLife() <= 0) continue;

            float dx = e.getCenterX() - spellCx;
            float dy = e.getCenterY() - spellCy;
            float distSq = (dx * dx) + (dy * dy);

            // On donne à l'ennemi une hitbox légèrement réduite pour que ça soit juste (ex: 60%)
            float enemyRadius = (e.getWidth() / 2.0f) * 0.6f;
            float combinedRadius = spellRadius + enemyRadius;

            // Comparaison des distances au carré
            if (distSq <= (combinedRadius * combinedRadius)) {
                return e; // Collision
            }
        }
        return null;
    }
}