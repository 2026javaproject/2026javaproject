// main/GameMain.java
package com.pilot.main;

import com.pilot.screen.GameOverPanel;
import com.pilot.screen.GamePanel;
import com.pilot.screen.MainMenuPanel;
import com.pilot.util.GameConstants;

import javax.swing.*;

public class GameMain extends JFrame {

    private MainMenuPanel mainMenuPanel;
    private GamePanel     gamePanel;
    private GameOverPanel gameOverPanel;

    public GameMain() {
        setTitle("PILOT");
        setSize(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        showMainMenu();
        setVisible(true);
    }

    public void showMainMenu() {
        mainMenuPanel = new MainMenuPanel(this);
        setContentPane(mainMenuPanel);
        revalidate();
    }

    public void startGame() {
        gamePanel = new GamePanel(this);
        setContentPane(gamePanel);
        revalidate();
        gamePanel.startGameLoop();
    }

    public void showGameOver(int score) {
        gameOverPanel = new GameOverPanel(this, score);
        setContentPane(gameOverPanel);
        revalidate();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMain::new);
    }
}