// entity/Enemy.java
package entity;

import util.GameConstants;

import java.awt.*;

public class Enemy {

    private int x, y;
    private int hp;

    public Enemy(int x, int y) {
        this.x  = x;
        this.y  = y;
        this.hp = 1;
    }

    public void update() {
        y += GameConstants.ENEMY_SPEED;
        // TODO: 이동 패턴 다양화 (Week 2)
    }

    public boolean isOutOfBounds() {
        return y > GameConstants.SCREEN_HEIGHT;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void hit() {
        hp--;
    }

    public void draw(Graphics2D g) {
        int cx   = x + GameConstants.ENEMY_WIDTH / 2;
        int[] px = { cx, cx - 14, cx + 14 };
        int[] py = { y + GameConstants.ENEMY_HEIGHT, y, y };
        g.setColor(new Color(248, 113, 113));
        g.fillPolygon(px, py, 3);
        // TODO: 스프라이트 이미지로 교체 (Week 2)
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    }
}