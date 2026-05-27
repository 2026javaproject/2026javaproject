package com.pilot.entity;

import com.pilot.util.GameConstants;

import java.awt.*;

public class Bullet {

    private int       x, y;
    private final int dx, dy;
    private final boolean playerBullet;

    public Bullet(int x, int y, int dx, int dy, boolean playerBullet) {
        this.x  = x;
        this.y  = y;
        this.dx = dx;
        this.dy = dy;
        this.playerBullet = playerBullet;
    }

    public void update() {
        x += dx;
        y += dy;
    }

    public boolean isOutOfBounds() {
        return y + GameConstants.BULLET_HEIGHT < 0
                || y > GameConstants.SCREEN_HEIGHT
                || x < 0
                || x > GameConstants.SCREEN_WIDTH;
    }

    public void draw(Graphics2D g) {
        g.setColor(playerBullet ? new Color(251, 191, 36) : new Color(255, 100, 100));
        g.fillRoundRect(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT, 3, 3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
    }
    
    public boolean isPlayerBullet() { return playerBullet; }
}