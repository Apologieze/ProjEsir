package main;

import manager.SoundAssetManager;
import javax.swing.JFrame;

/**
 * Classe principale du jeu
 */
public class Main {

    public static void main(String[] args) {

        SoundAssetManager.initMusic();

        // Fenêtre de lancement du jeu
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Sting 'Em Up");

        // Calcul des dimensions en se basant sur les constantes prévues dans GamePanel
        // (16 * 3 * 16 = 768px de large, 16 * 3 * 12 = 576px de haut)
        int screenWidth = 768;
        int screenHeight = 576;

        // Instanciation du MenuPanel avec un Callback (Lambda Runnable)
        MenuPanel menuPanel = new MenuPanel(screenWidth, screenHeight, () -> {

            // Logique de transition une fois "PLAY" cliqué
            window.getContentPane().removeAll();

            GamePanel gamePanel = new GamePanel();
            window.add(gamePanel);

            // Actualisation stricte du contenu de la fenêtre
            window.revalidate();
            window.repaint();

            // Lancement du jeu
            gamePanel.startGameThread();

            // CRITIQUE : Il faut redonner le focus au GamePanel pour que le KeyHandler fonctionne
            gamePanel.requestFocusInWindow();
        });

        window.add(menuPanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

    }
}