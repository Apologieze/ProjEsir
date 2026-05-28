package manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Gestionnaire d'images pour charger les ressources du jeu
 */
public class ImageAssetManager  {

    /**
     * Charge une seule image depuis un chemin donné
     * @param path Le chemin de l'image (ex: "/player/superhero.png")
     * @return L'image chargée, ou null si introuvable
     */
    public static BufferedImage loadImage(String path) {
        BufferedImage image = null;
        try {
            // Utilisation de getResourceAsStream qui est plus sûr
            image = ImageIO.read(Objects.requireNonNull(ImageAssetManager.class.getResourceAsStream(path)));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erreur : Impossible de charger l'image au chemin -> " + path);
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Charge toutes les images (.png) présentes dans un dossier spécifique
     * @param folderPath Le chemin du dossier (ex: "/player/run_animation/")
     * @return Une liste contenant toutes les images du dossier
     */
    public static List<BufferedImage> loadImagesFromFolder(String folderPath) {
        List<BufferedImage> images = new ArrayList<>();

        try {
            URL resourceUrl = ImageAssetManager.class.getResource(folderPath);
            if (resourceUrl == null) {
                System.err.println("Erreur : Dossier introuvable -> " + folderPath);
                return images;
            }

            File folder = new File(resourceUrl.getFile());
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();

                if (files != null) {
                    // Trie les fichiers par ordre alphabétique pour garantir le bon ordre des animations
                    Arrays.sort(files);

                    for (File file : files) {
                        // Ne charge que les fichiers PNG
                        if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                            images.add(ImageIO.read(file));
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du dossier -> " + folderPath);
            e.printStackTrace();
        }

        return images;
    }

    /**
     * Découpe un spritesheet (feuille de sprites) en une liste d'images individuelles.
     * Le parcours se fait de gauche à droite, puis de haut en bas.
     * * @param path Le chemin de l'image source (ex: "/player/spritesheet.png")
     * @param tileWidth La largeur d'une sous-image (ex: 16)
     * @param tileHeight La hauteur d'une sous-image (ex: 16)
     * @return Une liste contenant toutes les sous-images découpées
     */
    public static List<BufferedImage> loadSpritesheet(String path, int tileWidth, int tileHeight) {
        List<BufferedImage> sprites = new ArrayList<>();

        // Utilise la méthode loadImage déjà existante dans ImageAsset
        BufferedImage spriteSheet = loadImage(path);

        if (spriteSheet == null) {
            System.err.println("Erreur : Impossible de découper le spritesheet, image source introuvable.");
            return sprites;
        }

        // Calcul du nombre de colonnes et de lignes
        int cols = spriteSheet.getWidth() / tileWidth;
        int rows = spriteSheet.getHeight() / tileHeight;

        // Découpage de haut en bas (y) et de gauche à droite (x)
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                // Extraction de la sous-image
                BufferedImage subImage = spriteSheet.getSubimage(
                        x * tileWidth,
                        y * tileHeight,
                        tileWidth,
                        tileHeight
                );
                sprites.add(subImage);
            }
        }

        return sprites;
    }
}