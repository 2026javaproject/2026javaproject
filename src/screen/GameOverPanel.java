package com.pilot.screen;

import com.pilot.main.GameMain;
import com.pilot.util.GameConstants;
import util.HighScoreFileIO;

import javax.swing.*;
import java.awt.*;

public class GameOverPanel extends JPanel {

    private final GameMain gameMain;
    private final int score;
    private int bestScore;
    private boolean newRecord;
    private final boolean clearMode;

    public GameOverPanel(GameMain gameMain, int score) {
        this(gameMain, score, false);
    }

    public GameOverPanel(GameMain gameMain, int score, boolean clearMode) {
        this.gameMain = gameMain;
        this.score = score;
        this.clearMode = clearMode;
        updateHighScore(score);

        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());

        initUI();
    }

    private void initUI() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(8, 0, 8, 0);

        String titleText = clearMode ? "STAGE CLEAR" : "GAME OVER";
        JLabel title = new JLabel(titleText);
        title.setFont(new Font("Dialog", Font.BOLD, 36));
        title.setForeground(Color.WHITE);
        gbc.gridy = 0;
        add(title, gbc);

        JLabel scoreLabel = new JLabel("SCORE: " + score);
        scoreLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(scoreLabel, gbc);

        JLabel bestLabel = new JLabel("BEST: " + bestScore);
        bestLabel.setFont(new Font("Dialog", Font.PLAIN, 18));
        bestLabel.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 2;
        add(bestLabel, gbc);

        if (newRecord) {
            JLabel recordLabel = new JLabel("NEW RECORD!");
            recordLabel.setFont(new Font("Dialog", Font.BOLD, 18));
            recordLabel.setForeground(new Color(255, 215, 0));
            gbc.gridy = 3;
            add(recordLabel, gbc);
        }

        JButton restartButton = new JButton("RESTART");
        restartButton.addActionListener(e -> gameMain.startGame());
        gbc.gridy = newRecord ? 4 : 3;
        add(restartButton, gbc);

        JButton menuButton = new JButton("MAIN MENU");
        menuButton.addActionListener(e -> gameMain.showMainMenu());
        gbc.gridy = newRecord ? 5 : 4;
        add(menuButton, gbc);

        JButton exitButton = new JButton("EXIT");
        exitButton.addActionListener(e -> {
            gameMain.dispose();
            System.exit(0);
        });
        gbc.gridy = newRecord ? 6 : 5;
        add(exitButton, gbc);
    }

    private void updateHighScore(int newScore) {
        int currentBest = HighScoreFileIO.loadHighScore();
        newRecord = newScore > currentBest;
        bestScore = HighScoreFileIO.saveIfHighScore(newScore);
    }
}
