package manager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Gestionnaire de sons avec initialisation explicite
 */
public class SoundAssetManager {

    private static final Map<String, Clip> sounds = new HashMap<>();

    /**
     * Initialise le système audio et précharge tous les bruitages en mémoire
     */
    public static void initMusic() {
        try {
            Clip dummy = AudioSystem.getClip();
            dummy.open();
            dummy.close();
        } catch (Exception ignored) {}

        // Liste de tous les fichiers à charger
        String[] bruitages = {"levelup.wav", "BuckBumble.wav", "musicBoss.wav", "Sting_em_up.wav", "bossSpawn.wav","explosion.wav"};
        for (String name : bruitages) {
            loadSound(name);
        }
    }

    /**
     * Méthode interne de chargement brut
     */
    private static void loadSound(String fileName) {
        try {
            URL soundURL = SoundAssetManager.class.getResource("/sound/" + fileName);
            if (soundURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);
                sounds.put(fileName, clip);
            } else {
                System.err.println("Fichier introuvable : /sound/" + fileName);
            }
        } catch (Exception e) {
            System.err.println("Impossible de charger : " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Joue un effet sonore court
     */
    public static void playSE(String fileName) {
        Clip clip = sounds.get(fileName);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    /**
     * Joue une musique en boucle continue
     */
    public static void playMusicLoop(String fileName) {
        Clip clip = sounds.get(fileName);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
}