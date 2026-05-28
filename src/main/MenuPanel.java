package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import manager.SoundAssetManager;

/**
 * Panel du menu principal gérant les animations d'apparition et de lancement
 */
public class MenuPanel extends JPanel {

    private Image backgroundImage;
    private Image titleImage;
    private ImageIcon playIcon;
    private JButton playButton;

    private int animationState = 0; // 0 = Fond, 1 = Fond + Titre, 2 = Fond + Titre + Bouton
    private Runnable startGameCallback;

    public MenuPanel(int width, int height, Runnable startGameCallback) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null); // Layout absolu requis pour positionner le bouton manuellement
        this.startGameCallback = startGameCallback;

        loadImages();
        setupPlayButton(width, height);
        startSequenceTimer();
    }

    /**
     * Chargement des ressources graphiques.
     * Pensez à ajuster les chemins "/res/..." selon l'architecture de votre projet.
     */
    private void loadImages() {
        try {
            // Remplacez ces chemins par les chemins réels de vos assets
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/menu0.png"));
            titleImage = ImageIO.read(getClass().getResourceAsStream("/menu/menu1.png"));
            playIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/menu/menu2.png")));
        } catch (Exception e) {
            System.err.println("Ressources visuelles du menu introuvables. Utilisation des placeholders.");
        }
    }

    /**
     * Configuration du bouton Jouer
     */
    private void setupPlayButton(int screenWidth, int screenHeight) {
        playButton = new JButton("PLAY");

        // Si l'image du bouton est chargée avec succès, on masque le texte et les bordures
        if (playIcon != null && playIcon.getImage() != null) {
            playButton.setIcon(playIcon);
            playButton.setText("");
            playButton.setContentAreaFilled(false);
            playButton.setBorderPainted(false);
            playButton.setFocusPainted(false);
        } else {
            // Style de repli si l'image manque
            playButton.setBackground(Color.WHITE);
            playButton.setFont(new Font("Arial", Font.BOLD, 20));
        }

        // Centrage du bouton dans la moitié inférieure de l'écran
        int buttonWidth = 546;
        int buttonHeight = 143;
        playButton.setBounds((screenWidth - buttonWidth) / 2, (screenHeight / 2) -50 , buttonWidth, buttonHeight);

        playButton.setVisible(false); // Invisible à l'initialisation

        // Action au clic
        playButton.addActionListener(e -> launchGame());
        this.add(playButton);
    }

    /**
     * Minuteur orchestrant l'apparition séquentielle des éléments
     */
    private void startSequenceTimer() {
        // Un délai d'une seconde (1000ms) entre chaque étape
        Timer timer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationState++;

                if (animationState == 1) {
                    // Apparition du titre
                    SoundAssetManager.playSE("explosion.wav");
                    repaint();
                } else if (animationState == 2) {
                    // Apparition du bouton
                    SoundAssetManager.playSE("explosion.wav");
                    playButton.setVisible(true);
                    repaint();

                    // Fin de la séquence d'apparition, on arrête le minuteur
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.setInitialDelay(1000); // Attend 1 seconde avant d'afficher le titre
        timer.start();
    }

    /**
     * Lance la musique et délègue la transition de panel à Main
     */
    private void launchGame() {
        SoundAssetManager.playMusicLoop("BuckBumble.wav");
        if (startGameCallback != null) {
            startGameCallback.run();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Affichage du fond
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // 2. Affichage du titre (si animationState >= 1)
        if (animationState >= 1) {
            if (titleImage != null) {
                int x = (getWidth() - titleImage.getWidth(null)) / 2;
                g.drawImage(titleImage, x, 0, null);
            } else {
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 60));
                FontMetrics fm = g.getFontMetrics();
                String title = "STING 'EM UP";
                g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, 150);
            }
        }
    }
}