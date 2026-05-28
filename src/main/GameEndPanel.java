package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Panel de fin de partie universel (Victoire ou Défaite).
 */
public class GameEndPanel extends JPanel {

    private Image backgroundImage;
    private ImageIcon replayIcon;
    private JButton replayButton;

    private int score;
    private boolean isWin;
    private Runnable replayCallback;

    public GameEndPanel(int width, int height, int score, boolean isWin, Runnable replayCallback) {
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(null);
        this.score = score;
        this.isWin = isWin;
        this.replayCallback = replayCallback;

        loadResources();
        setupReplayButton(width, height);
    }

    private void loadResources() {
        try {
            replayIcon = new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/menu/replay_btn.png")));

            // Chargement conditionnel de l'image de fond
            if (isWin) {
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/victory_bg.png"));
            } else {
                backgroundImage = ImageIO.read(getClass().getResourceAsStream("/menu/gameover_bg.png"));
            }
        } catch (Exception e) {
            System.err.println("Ressources visuelles de fin de jeu introuvables. Utilisation des placeholders.");
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

        int buttonWidth = 200;
        int buttonHeight = 60;
        replayButton.setBounds((screenWidth - buttonWidth) / 2, (screenHeight / 2) + 50, buttonWidth, buttonHeight);

        replayButton.addActionListener(e -> {
            if (replayCallback != null) {
                replayCallback.run();
            }
        });
        this.add(replayButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. Affichage du fond d'écran
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Texte de repli (Placeholder) adaptatif
            g.setColor(isWin ? Color.GREEN : Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 60));
            String title = isWin ? "VICTOIRE !" : "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            g.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, getHeight() / 2 - 50);
        }

        // 2. Affichage du score (commun aux deux états)
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        String scoreText = "Score final : " + score;
        FontMetrics fmScore = g.getFontMetrics();
        g.drawString(scoreText, (getWidth() - fmScore.stringWidth(scoreText)) / 2, getHeight() / 2);
    }
}