package entity;

import manager.ImageAssetManager;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Entité disposant d'une animation à partir d'un dossier d'images
 */
public abstract class AnimatedEntity extends Entity {

    protected List<BufferedImage> frames;
    protected int animationSpeed;
    protected int currentFrameIndex = 0;
    protected int frameCounter = 0;

    /**
     * Constructeur pour une entité animée
     * @param folderPath Chemin vers le dossier contenant les frames de l'animation
     * @param animationSpeed Nombre de cycles de mise à jour avant de changer d'image (plus c'est élevé, plus c'est lent)
     */
    public AnimatedEntity(String folderPath, int animationSpeed) {
        this.animationSpeed = animationSpeed;
        this.frames = ImageAssetManager.loadImagesFromFolder(folderPath);
    }

    public AnimatedEntity(List<BufferedImage> preloadedFrames, int animationSpeed) {
        this.animationSpeed = animationSpeed;
        this.frames = preloadedFrames;
    }

    /**
     * Met à jour l'animation (à appeler à chaque tick dans la méthode update de l'entité enfant)
     */
    public void updateAnimation() {
        // Sécurité au cas où le dossier est vide ou introuvable
        if (frames == null || frames.isEmpty()) {
            return;
        }

        frameCounter++;

        // Si le compteur atteint la vitesse définie, on passe à l'image suivante
        if (frameCounter >= animationSpeed) {
            currentFrameIndex++;

            // On boucle pour revenir à la première image si on atteint la fin de la liste
            if (currentFrameIndex >= frames.size()) {
                currentFrameIndex = 0;
            }

            frameCounter = 0; // Réinitialisation du compteur
        }
    }

    /**
     * Récupère l'image actuelle de l'animation pour l'affichage
     * @return L'image à dessiner
     */
    public BufferedImage getCurrentFrame() {
        if (frames == null || frames.isEmpty()) {
            return null;
        }
        return frames.get(currentFrameIndex);
    }
}