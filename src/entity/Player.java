// entity/Player.java
package entity;

import util.GameConstants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private int     x, y;
    private int     hp;
    private boolean left, right, up, down;
    private final List<Bullet> bullets = new ArrayList<>();

    private boolean invincible      = false;
    private int     invincibleTimer  = 0;
    private static final int INVINCIBLE_DURATION = 90;

    public Player(int x, int y) {
        this.x  = x - GameConstants.PLAYER_WIDTH / 2;
        this.y  = y;
        this.hp = GameConstants.PLAYER_HP;
    }

    public void update() {
        if (left)  x -= GameConstants.PLAYER_SPEED;
        if (right) x += GameConstants.PLAYER_SPEED;
        if (up)    y -= GameConstants.PLAYER_SPEED;
        if (down)  y += GameConstants.PLAYER_SPEED;

        x = Math.max(0, Math.min(x, GameConstants.SCREEN_WIDTH  - GameConstants.PLAYER_WIDTH));
        y = Math.max(0, Math.min(y, GameConstants.SCREEN_HEIGHT - GameConstants.PLAYER_HEIGHT));

        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) invincible = false;
        }
    }

    public void shoot() {
        int bx = x + GameConstants.PLAYER_WIDTH / 2 - GameConstants.BULLET_WIDTH / 2;
        bullets.add(new Bullet(bx, y, 0, -GameConstants.BULLET_SPEED));
        // TODO: 다중 발사 아이템 효과 연동 (Week 2)
    }

    public void hit() {
        if (invincible) return;
        hp--;
        invincible      = true;
        invincibleTimer = INVINCIBLE_DURATION;
    }

    public void draw(Graphics2D g) {
        if (invincible && (invincibleTimer / 5) % 2 == 0) return;

        int cx   = x + GameConstants.PLAYER_WIDTH / 2;
        int[] px = { cx, cx - 16, cx + 16 };
        int[] py = { y,  y + GameConstants.PLAYER_HEIGHT, y + GameConstants.PLAYER_HEIGHT };
        g.setColor(new Color(125, 211, 252));
        g.fillPolygon(px, py, 3);
        // TODO: 스프라이트 이미지로 교체 (Week 2)
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
    }

    public List<Bullet> getBullets() { return bullets; }
    public int          getHp()      { return hp; }

    public void setLeft(boolean v)  { left  = v; }
    public void setRight(boolean v) { right = v; }
    public void setUp(boolean v)    { up    = v; }
    public void setDown(boolean v)  { down  = v; }
}