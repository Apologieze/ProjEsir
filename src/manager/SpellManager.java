package manager;

import entity.EnemySpell;
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

    // Pools d'objets
    private List<PlayerSpell> playerSpellPool;
    private List<EnemySpell> enemySpellPool;
    private final int MAX_PLAYER_SPELLS = 50;
    private final int MAX_ENEMY_SPELLS = 100;

    // Caches statiques pour les assets (chargés une seule fois au démarrage)
    private static BufferedImage[] playerSpellImages = new BufferedImage[3];
    private static List<List<BufferedImage>> enemySpellAnimations = new ArrayList<>(3);

    public SpellManager(GamePanel gp) {
        this.gp = gp;
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
                playerSpellImages[0] = ImageIO.read(getClass().getResourceAsStream("/spells/player_type1.png"));
                playerSpellImages[1] = ImageIO.read(getClass().getResourceAsStream("/spells/player_type2.png"));
                playerSpellImages[2] = ImageIO.read(getClass().getResourceAsStream("/spells/player_type3.png"));

                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/spells/enemy_type1"));
                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/spells/enemy_type2"));
                enemySpellAnimations.add(ImageAssetManager.loadImagesFromFolder("/spells/enemy_type3"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Pré-allocation des instances de sorts désactivés.
     */
    private void initializePools() {
        // Pré-allocation des sorts joueur (ex: répartition égale des types ou type par défaut)
        for (int i = 0; i < MAX_PLAYER_SPELLS; i++) {
            playerSpellPool.add(new PlayerSpell(gp, playerSpellImages[0], 0)); // Type 0 par défaut
        }

        // Pré-allocation des sorts ennemis
        for (int i = 0; i < MAX_ENEMY_SPELLS; i++) {
            enemySpellPool.add(new EnemySpell(gp, enemySpellAnimations.get(0), 10, 0));
        }
    }

    /**
     * Recherche un sort inactif dans le pool et l'active.
     */
    public void spawnPlayerSpell(float x, float y, int type, float dirX, float dirY) {
        for (PlayerSpell spell : playerSpellPool) {
            if (!spell.isActive() && spell.type == type) {
                spell.spawn(x, y, dirX, dirY);
                return; // Sort trouvé et activé, fin de l'opération
            }
        }
        // Optionnel : Gérer le cas où le pool est vide (agrandir le pool ou ignorer le tir)
    }

    public void spawnEnemySpell(float x, float y, int type, float dirX, float dirY) {
        for (EnemySpell spell : enemySpellPool) {
            if (!spell.isActive() && spell.type == type) {
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