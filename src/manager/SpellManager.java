package manager;

import entity.EnemySpell;
import entity.Player;
import entity.PlayerSpell;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpellManager {

    private GamePanel gp;

    private EnemyManager enemyManager;
    private Player player;

    // Pools d'objets
    private List<PlayerSpell> playerSpellPool;
    private List<EnemySpell> enemySpellPool;
    private final int MAX_PLAYER_SPELLS = 50;
    private final int MAX_ENEMY_SPELLS = 100;

    // Caches statiques pour les assets (chargés une seule fois au démarrage)
    private static BufferedImage[] playerSpellImages = new BufferedImage[3];
    private static List<List<BufferedImage>> enemySpellAnimations = new ArrayList<>(3);

    private final int[] enemySpellMoveSpeeds = {6, 6, 3}; // Type 1 is fast, Type 2 is slow
    private final int[] enemySpellAnimSpeeds = {3, 5, 12}; // Type 1 animates twice as fast

    public SpellManager(GamePanel gp, EnemyManager enemyManager, Player player) {
        this.gp = gp;
        this.enemyManager = enemyManager;
        this.player = player;
        this.playerSpellPool = new ArrayList<>(MAX_PLAYER_SPELLS);
        this.enemySpellPool = new ArrayList<>(MAX_ENEMY_SPELLS);

        loadAssets();
        initializePools();
    }

    /**
     * Chargement en mémoire des assets.
     * Cette méthode garantit que le disque n'est lu qu'à l'initialisation.
     */
    private void loadAssets() {
        try {
            // Remplacer par vos chemins réels
            if (playerSpellImages[0] == null) {
                playerSpellImages[0] = ImageIO.read(getClass().getResourceAsStream("/bullet/bee/grass.png"));
                playerSpellImages[1] = ImageIO.read(getClass().getResourceAsStream("/bullet/bee/fire.png"));
                playerSpellImages[2] = ImageIO.read(getClass().getResourceAsStream("/bullet/bee/water.png"));

                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/bullet/enemy/grass"));
                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/bullet/enemy/fire"));
                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/bullet/enemy/water"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pré-allocation des instances de sorts désactivés.
     */
    private void initializePools() {
        for (int i = 0; i < MAX_PLAYER_SPELLS; i++) {
            playerSpellPool.add(new PlayerSpell(gp, playerSpellImages[0], 0, enemyManager));
        }

        for (int i = 0; i < MAX_ENEMY_SPELLS; i++) {
            enemySpellPool.add(new EnemySpell(gp, player, enemySpellAnimations.get(0), 10, 0));
        }
    }

    /**
     * Recherche un sort inactif dans le pool, le reconfigure, et l'active.
     */
    public void spawnPlayerSpell(float x, float y, int type, float dirX, float dirY) {
        for (PlayerSpell spell : playerSpellPool) {
            if (!spell.isActive()) {
                // Reconfigure the object with the correct image before firing
                spell.configure(type, playerSpellImages[type]);
                spell.spawn(x, y, dirX, dirY);
                return;
            }
        }
    }

    public void spawnEnemySpell(float x, float y, int type, float dirX, float dirY) {
        for (EnemySpell spell : enemySpellPool) {
            if (!spell.isActive()) {
                // Fetch the custom speeds based on the type requested
                int moveSpeed = enemySpellMoveSpeeds[type];
                int animSpeed = enemySpellAnimSpeeds[type];

                // Reconfigure the object before firing
                spell.configure(type, enemySpellAnimations.get(type), animSpeed, moveSpeed);
                spell.spawn(x, y, dirX, dirY);
                return;
            }
        }
    }

    /**
     * Met à jour uniquement les sorts actifs.
     */
    public void update() {
        for (PlayerSpell spell : playerSpellPool) {
            if (spell.isActive()) spell.update();
        }
        for (EnemySpell spell : enemySpellPool) {
            if (spell.isActive()) spell.update();
        }
    }

    /**
     * Dessine uniquement les sorts actifs.
     */
    public void draw(Graphics2D g2) {
        for (PlayerSpell spell : playerSpellPool) {
            if (spell.isActive()) spell.draw(g2);
        }
        for (EnemySpell spell : enemySpellPool) {
            if (spell.isActive()) spell.draw(g2);
        }
    }
}