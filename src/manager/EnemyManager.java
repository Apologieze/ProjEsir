package manager;

import entity.Enemy;
import entity.EnemyBoss;
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

    private int normalEnemiesSpawned = 0;
    private boolean isBossActive = false;

     //(0=Forêt, 1=Eau, 2=feu, selon tes types)
    private BufferedImage[] bossImages = new BufferedImage[3];

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

        try {
            bossImages[0] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/enemy/boss/16.png"));
            bossImages[1] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/enemy/boss/05.png"));
            bossImages[2] = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/enemy/boss/01.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        for (int i = enemies.size() - 1; i >= 0; i--) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.getLife() <= 0) {
                if (e instanceof EnemyBoss) {
                    System.out.println("LE BOSS EST MORT ! NIVEAU TERMINÉ !");
                }
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

        // Si le joueur est niveau 1, il ne peut pas avoir "1 de moins"
        if (playerLvl == 1) {
            if (rand < 0.25) {
                return playerLvl + 1; // 25% de chance d'avoir le niveau supérieur
            } else {
                return playerLvl;     // 75% de chance d'avoir le niveau 1
            }
        }

        // Joueur niveau 2 ou plus
        if (rand < 0.25) {
            return playerLvl + 1;     // 25% de chance d'avoir (Niveau + 1)
        } else if (rand < 0.40) {     // 0.25 + 0.15 = 0.40
            return playerLvl - 1;     // 15% de chance d'avoir (Niveau - 1)
        } else {
            return playerLvl;         // 60% de chance d'avoir le niveau actuel (50% de base + 10% restants)
        }
    }

    /**
     * Génère un ennemi aléatoire en fonction du niveau en cours
     */
    public void spawnRandomEnemy(int levelNum) {
        if (isBossActive) return;

        // On incrémente notre compteur
        normalEnemiesSpawned++;

        // Si on a atteint les 30 ennemis, on lance le boss au lieu d'un ennemi normal
        if (normalEnemiesSpawned >= 5) {
            spawnBoss(levelNum);
            return; // On sort de la méthode pour ne pas faire spawner de mob normal
        }

        Enemy newEnemy = null;
        int levelDrop = this.generateLevelDrop();

        if (levelNum == 1) {
            newEnemy = new PlantEnemy(player, gp, spellManager, plantFrames, levelDrop);
        }
        else if (levelNum == 2) {
            // TODO: Remplacer par WaterEnemy une fois la classe créée
            // newEnemy = new WaterEnemy(player, gp, spellManager, waterFrames, levelDrop);
            newEnemy = new PlantEnemy(player, gp, spellManager, plantFrames, levelDrop);
        }
        else {
            // Sécurité par défaut
            newEnemy = new PlantEnemy(player, gp, spellManager, plantFrames, levelDrop);
        }
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

        // 3. Application des paramètres et ajout au jeu
        newEnemy.setPosition(startX, startY);
        newEnemy.setEntryDirection(entryDx, entryDy);

        enemies.add(newEnemy);
    }

    /**
     * Fait apparaître le Boss du niveau
     */
    private void spawnBoss(int levelNum) {
        isBossActive = true; // Bloque le spawn des ennemis classiques

        int bossType = levelNum - 1; // Si level 1 = type 0
        int levelDrop = this.generateLevelDrop();

        // Sécurité : si l'image du boss n'est pas chargée, on utilise la première
        BufferedImage bossImg = bossImages[bossType];
        if (bossImg == null) bossImg = bossImages[0];

        EnemyBoss boss = new EnemyBoss(player, gp, spellManager, bossImg, bossType, levelDrop);

        // On l'ajoute à la liste des ennemis
        enemies.add(boss);

        // Optionnel : Jouer un son d'alerte ou changer la musique
        System.out.println("⚠️ ATTENTION : LE BOSS APPARAÎT ! ⚠️");
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