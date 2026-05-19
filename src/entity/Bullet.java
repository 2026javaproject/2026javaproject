// entity/Bullet.java
package entity;

import util.GameConstants;

import java.awt.*;

public class Bullet {

    private int       x, y;
    private final int dx, dy;

    public Bullet(int x, int y, int dx, int dy) {
        this.x  = x;
        this.y  = y;
        this.dx = dx;
        this.dy = dy;
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
        g.setColor(new Color(251, 191, 36));
        g.fillRoundRect(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT, 3, 3);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.BULLET_WIDTH, GameConstants.BULLET_HEIGHT);
    }
}