package main;

import manager.SoundAssetManager;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Classe principale du jeu
 */
public class Main {

    private static JFrame window;
    private static final int SCREEN_WIDTH = 768;
    private static final int SCREEN_HEIGHT = 576;

    public static void main(String[] args) {
        SoundAssetManager.initMusic();

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Sting 'Em Up");

        // Affichage initial du menu principal
        showMainMenu();

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }

    private static void showMainMenu() {
        window.getContentPane().removeAll();

        MenuPanel menuPanel = new MenuPanel(SCREEN_WIDTH, SCREEN_HEIGHT, () -> startGame());

        window.add(menuPanel);
        refreshWindow();
    }

    private static void startGame() {
        window.getContentPane().removeAll();

        // Capture des deux paramètres (score, isWin) injectés par le GamePanel
        GamePanel gamePanel = new GamePanel((score, isWin) -> {
            SwingUtilities.invokeLater(() -> {
                showEndScreen(score, isWin);
            });
        });

        window.add(gamePanel);
        refreshWindow();

        gamePanel.startGameThread();
        gamePanel.requestFocusInWindow();
    }

    /**
     * Affiche l'écran de fin de partie approprié
     */
    private static void showEndScreen(int finalScore, boolean isWin) {
        window.getContentPane().removeAll();

        // Instanciation du panel généralisé
        GameEndPanel endPanel = new GameEndPanel(SCREEN_WIDTH, SCREEN_HEIGHT, finalScore, isWin, () -> startGame());

        window.add(endPanel);
        refreshWindow();
    }

    /**
     * Actualise strictement le contenu de la fenêtre pour éviter les artefacts visuels
     */
    private static void refreshWindow() {
        window.revalidate();
        window.repaint();
    }
}