package manager;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
            image = ImageIO.read(Objects.requireNonNull(ImageAssetManager.class.getResourceAsStream(path)));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Erreur : Impossible de charger l'image au chemin -> " + path);
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Charge toutes les images d'un dossier (Compatible création de JAR)
     * Tes images dans le dossier doivent être nommées de façon séquentielle : 0.png, 1.png, 2.png, etc.
     */
    public static List<BufferedImage> loadImagesFromFolder(String folderPath) {
        List<BufferedImage> images = new ArrayList<>();
        int i = 0; // Si tes images commencent à 1.png, mets 1 ici

        while (true) {
            // On construit le chemin. Ex: "/enemy/grass/0.png"
            String imagePath = folderPath + "/" + i + ".png";

            // getResourceAsStream cherche dans le classpath (le src ou le res dans l'IDE, ou à la racine du JAR)
            InputStream is = ImageAssetManager.class.getResourceAsStream(imagePath);

            // Si is est null, c'est que le fichier n'existe pas. On a donc atteint la fin de l'animation !
            if (is == null) {
                break;
            }

            try {
                images.add(javax.imageio.ImageIO.read(is));
            } catch (Exception e) {
                System.err.println("Erreur de lecture de l'image : " + imagePath);
                e.printStackTrace();
                break;
            }
            i++;
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