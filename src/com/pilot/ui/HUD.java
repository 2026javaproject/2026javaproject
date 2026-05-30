package com.pilot.ui;

import com.pilot.util.GameConstants;

import java.awt.*;

public class HUD {

    private static final Font HUD_FONT = new Font("Dialog", Font.BOLD, 16);
    private static final Font WARNING_FONT = new Font("Dialog", Font.BOLD, 36);

    private int score;
    private int hp;
    private int stage;
    private boolean shieldActive;
    private int kills;

    private boolean warningActive;
    private int warningTimer;
    private int warningBlinkTimer;
    private boolean warningVisible;

    public void updateStats(int score, int hp, int stage, boolean shieldActive, int kills) {
        this.score = score;
        this.hp = hp;
        this.stage = stage;
        this.shieldActive = shieldActive;
        this.kills = kills;
    }

    public void startWarning(int durationFrames) {
        warningActive = true;
        warningTimer = durationFrames;
        warningBlinkTimer = 0;
        warningVisible = true;
    }

    public void updateWarning() {
        if (!warningActive) {
            return;
        }
        warningTimer--;
        warningBlinkTimer++;
        if (warningBlinkTimer >= 12) {
            warningBlinkTimer = 0;
            warningVisible = !warningVisible;
        }
        if (warningTimer <= 0) {
            warningActive = false;
            warningVisible = false;
        }
    }

    public boolean isWarningActive() {
        return warningActive;
    }

    public void draw(Graphics2D g2) {
        g2.setFont(HUD_FONT);
        g2.setColor(Color.WHITE);
        g2.drawString("SCORE: " + score, 12, 22);
        g2.drawString("KILL: " + kills, 12, 72);
        drawHearts(g2, 12, 32, hp);
        g2.setColor(Color.WHITE);
        g2.drawString("STAGE: " + stage, 12, 62);
        drawShieldIcon(g2, 140, 44, shieldActive);

        if (warningActive && warningVisible) {
            g2.setFont(WARNING_FONT);
            g2.setColor(Color.RED);
            String text = "WARNING, WARNING";
            FontMetrics fm = g2.getFontMetrics();
            int x = (GameConstants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
            int y = GameConstants.SCREEN_HEIGHT / 2;
            g2.drawString(text, x, y);
        }
    }

    private void drawHearts(Graphics2D g2, int startX, int baseY, int count) {
        int size = 8 ;
        int gap = 6;
        for (int i = 0; i < count; i++) {
            int x = startX + i * (size + gap);
            int y = baseY - size;
            g2.setColor(new Color(255, 90, 90));
            g2.fillOval(x, y, size, size);
            g2.fillOval(x + size, y, size, size);
            int[] px = { x - 1, x + size * 2 + 1, x + size };
            int[] py = { y + size - 2, y + size - 2, y + size * 2 + 2 };
            g2.fillPolygon(px, py, 3);
            
        }
    }

    private void drawShieldIcon(Graphics2D g2, int x, int y, boolean active) {
        int r = 8;
        g2.setColor(active ? new Color(80, 200, 255) : new Color(120, 120, 120));
        
        if (active) {
            g2.setColor(new Color(80, 200, 255, 120));
            g2.fillOval(x, y - r, r * 2, r * 2);
        }
    }
}