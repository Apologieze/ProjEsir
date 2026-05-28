package manager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Gestionnaire de sons pour charger et jouer les ressources audio du jeu
 */
public class SoundAssetManager {

    // Notre dictionnaire de sons stocké de manière statique
    private static final Map<String, Clip> sounds = new HashMap<>();

    /**
     * Charge un fichier audio en mémoire cache (RAM)
     * @param fileName Le nom du fichier dans le dossier /sound/ (ex: "tir.wav")
     */
    public static void loadSound(String fileName) {
        try {
            URL soundURL = SoundAssetManager.class.getResource("/sound/" + fileName);
            if (soundURL != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL);
                Clip clip = AudioSystem.getClip();
                clip.open(ais);

                // Enregistrement dans le dictionnaire static
                sounds.put(fileName, clip);
            } else {
                System.err.println("Erreur : Fichier /sound/" + fileName + " introuvable.");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du préchargement du son -> " + fileName);
            e.printStackTrace();
        }
    }

    /**
     * Joue un effet sonore court (gère la réinitialisation si déjà en cours de lecture)
     * @param fileName Le nom du fichier audio (ex: "tir.wav")
     */
    public static void playSE(String fileName) {
        Clip clip = sounds.get(fileName);

        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop(); // Stoppe le son s'il jouait déjà (tir rapide)
            }
            clip.setFramePosition(0); // Revient au début
            clip.start(); // Joue
        } else {
            System.err.println("Erreur : Le son '" + fileName + "' n'a pas été préchargé avec loadSound().");
        }
    }

    /**
     * Joue une musique en boucle continue
     * @param fileName Le nom de la musique (ex: "music_fond.wav")
     */
    public static void playMusicLoop(String fileName) {
        Clip clip = sounds.get(fileName);

        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Erreur : La musique '" + fileName + "' n'a pas été préchargée.");
        }
    }

    /**
     * Arrête un son ou une musique en cours
     * @param fileName Le nom du fichier à couper
     */
    public static void stop(String fileName) {
        Clip clip = sounds.get(fileName);
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}