package com.pilot.entity;

import com.pilot.util.GameConstants;

import java.awt.*;

public class Bullet {

    private int       x, y;
    private final int dx, dy;
    private final boolean playerBullet;
    private final float damage;

    private static final Color PLAYER_COLOR = new Color(251, 191, 36);
    private static final Color ENEMY_COLOR = new Color(255, 100, 100);
    private final Rectangle bounds = new Rectangle(0, 0, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);

    public Bullet(int x, int y, int dx, int dy, boolean playerBullet) {
        this(x, y, dx, dy, playerBullet, 1.0f);
    }
    
    public Bullet(int x, int y, int dx, int dy, boolean playerBullet, float damage) {
        this.x  = x;
        this.y  = y;
        this.dx = dx;
        this.dy = dy;
        this.playerBullet = playerBullet;
        this.damage = damage;
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
        g.setColor(playerBullet ? PLAYER_COLOR : ENEMY_COLOR);
        g.fillRoundRect(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT, 3, 3);
    }

    public Rectangle getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }
    
    public boolean isPlayerBullet() { return playerBullet; }
    public float getDamage() { return damage; }
}