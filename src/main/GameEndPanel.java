package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Panel de fin de partie universel gérant des images de titre et de boutons spécifiques selon l'état.
 */
public class GameEndPanel extends JPanel {

    private Image backgroundImage;
    private Image titleImage;
    private ImageIcon replayIcon;
    private JButton replayButton;

    private int score;
    private boolean isWin;
    private Runnable replayCallback;

    private int animationState = 0;

    public GameEndPanel(int width, int height, int score, boolean isWin, Runnable replayCallback) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.score = score;
        this.isWin = isWin;
        this.replayCallback = replayCallback;

        loadResources();
        setupReplayButton(width, height);
        startSequenceTimer();
    }

    /**
     * Chargement conditionnel strict des ressources selon l'état de victoire.
     */
    private void loadResources() {
        try {
            if (isWin) {
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/fondWin.png"));
                titleImage = ImageIO.read(getClass().getResourceAsStream("/menu/victoryWin.png"));
                replayIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/menu/rejouerWin.png")));
            } else {
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/fondDied.png"));
                titleImage = ImageIO.read(getClass().getResourceAsStream("/menu/youDied.png"));
                replayIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/menu/rejouerDied.png")));
            }
        } catch (Exception e) {
            System.err.println("Ressources visuelles manquantes. Utilisation du rendu textuel de repli.");
        }
    }

    private void setupReplayButton(int screenWidth, int screenHeight) {
        replayButton = new JButton("REJOUER");

        if (replayIcon != null && replayIcon.getImage() != null) {
            replayButton.setIcon(replayIcon);
            replayButton.setText("");
            replayButton.setContentAreaFilled(false);
            replayButton.setBorderPainted(false);
            replayButton.setFocusPainted(false);
        } else {
            replayButton.setBackground(Color.WHITE);
            replayButton.setFont(new Font("Arial", Font.BOLD, 20));
        }

        // Dimensions par défaut, ajustables selon la taille de vos images de bouton
        int buttonWidth = replayIcon != null ? replayIcon.getIconWidth() : 200;
        int buttonHeight = replayIcon != null ? replayIcon.getIconHeight() : 60;

        replayButton.setBounds((screenWidth - buttonWidth) / 2, (screenHeight / 2) + 90, buttonWidth, buttonHeight);
        replayButton.setVisible(false);

        replayButton.addActionListener(e -> {
            if (replayCallback != null) {
                replayCallback.run();
            }
        });
        this.add(replayButton);
    }

    private void startSequenceTimer() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                animationState++;

                if (animationState == 1 || animationState == 2) {
                    repaint();
                } else if (animationState == 3) {
                    replayButton.setVisible(true);
                    repaint();
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        timer.setInitialDelay(800);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ÉTAPE 0 : Fond d'écran
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // ÉTAPE 1 : Affichage du titre (Image ou texte de repli)
        if (animationState >= 1) {
            if (titleImage != null) {
                int x = (getWidth() - titleImage.getWidth(null)) / 2;
                // Position Y = 100. À ajuster en fonction des dimensions de votre image
                g.drawImage(titleImage, x, 0, null);
            } else {
                g.setColor(isWin ? Color.GREEN : Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 60));
                String title = isWin ? "VICTOIRE !" : "GAME OVER";
                FontMetrics fm = g.getFontMetrics();
                g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 60);
            }
        }

        // ÉTAPE 2 : Affichage du score
        if (animationState >= 2) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String scoreText = "Score final : " + score;
            FontMetrics fmScore = g.getFontMetrics();
            g.drawString(scoreText, (getWidth() - fmScore.stringWidth(scoreText)) / 2, getHeight() / 2 + 20);
        }
    }
}