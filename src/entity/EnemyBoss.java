package entity;

import main.GamePanel;
import ui.BossBar;
import manager.SpellManager;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class EnemyBoss extends Enemy {

    private int numProjectiles; // Le nombre de tirs dans le cercle
    private BossBar bossBar;
    private int alternate = 0; // pour pas qu'il ne tire tjr au mm endroit

    public EnemyBoss(Player player, GamePanel gp, SpellManager spellManager, BufferedImage bossImage, int type, int levelDrop) {
        super(player, gp, spellManager, createSingleFrameList(bossImage), levelDrop);

        this.type = type;
        this.fireRate = 90; // Tire toutes les 1,5 secondes
        this.speed = 0;     // Le boss reste immobile au centre
        this.life = 30*gp.levelNum;    // Beaucoup de PV pour un boss

        // Taille spécifique du boss (64x96)
        this.setSize(64*2, 96*2);

        this.x = (gp.SCREEN_WIDTH / 2.0f) - (this.width / 2.0f);
        this.y = (gp.SCREEN_HEIGHT / 2.0f) - (this.height / 2.0f);

        this.isOnScreen = true;

        String bossName = "";
        switch (this.type) {
            case 0:
                numProjectiles = 16;
                bossName = "Boss de la Forêt";
                break;
            case 1:
                bossName = "Boss de l'Eau";
                numProjectiles = 18;
                break;
            case 2:
                numProjectiles = 20;
                bossName = "Boss de Feu";
                break;
            default:
                bossName = "Boss";
                break;
        }

        // On passe le nom dynamique à la barre de vie
        this.bossBar = new BossBar(gp, this, bossName, this.life);
    }



    /**
     * Astuce pour rendre compatible une image fixe avec le système AnimatedEntity
     */
    private static List<BufferedImage> createSingleFrameList(BufferedImage img) {
        List<BufferedImage> list = new ArrayList<>();
        list.add(img);
        return list;
    }

    @Override
    public void moveOnScreen() {
        // Le boss reste fixe. Tu pourrais ajouter un petit mouvement de lévitation ici plus tard !
    }

    @Override
    public void shoot() {
        // Tir en forme de cercle
        for (int i = 0; i < numProjectiles; i++) {
            // Calcule l'angle pour répartir uniformément les tirs sur 360
            double angle = (2 * Math.PI / numProjectiles) * i + alternate * Math.PI / numProjectiles;
            float dirX = (float) Math.cos(angle);
            float dirY = (float) Math.sin(angle);

            // Fait spawner le sort depuis le centre du boss
            spellManager.spawnEnemySpell(getCenterX(), getCenterY(), this.type, dirX, dirY);
        }

        // Son pour tir du boss
        manager.SoundAssetManager.playSE("bossShoot.wav");
        alternate=++alternate%2;
    }

    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        if (life>0 && bossBar != null){
            bossBar.update();
        }
    }

    public BossBar getBossBar(){
        return bossBar;
    }
}