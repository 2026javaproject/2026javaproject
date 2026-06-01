package com.pilot.entity;

import com.pilot.util.GameConstants;
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

    private int weaponLevel = 1;
    private int speedBuffTimer = 0;
    private static final int BASE_SPEED = 5;
    private boolean shieldActive = false;
    private int shieldTimer = 0;
    private int attackSpeedBuffTimer = 0;

    private static final Color PLAYER_COLOR = new Color(125, 211, 252);
    private static final Color SHIELD_COLOR = new Color(0, 255, 0, 100);
    private final Rectangle bounds = new Rectangle(0, 0, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);

    public Player(int x, int y) {
        this.x  = x - GameConstants.PLAYER_WIDTH / 2;
        this.y  = y;
        this.hp = GameConstants.PLAYER_HP;
    }

    public void update() {
        int currentSpeed = speedBuffTimer > 0 ? BASE_SPEED + 2 : BASE_SPEED;
        if (left)  x -= currentSpeed;
        if (right) x += currentSpeed;
        if (up)    y -= currentSpeed;
        if (down)  y += currentSpeed;

        x = Math.max(0, Math.min(x, GameConstants.SCREEN_WIDTH  - GameConstants.PLAYER_WIDTH));
        y = Math.max(0, Math.min(y, GameConstants.SCREEN_HEIGHT - GameConstants.PLAYER_HEIGHT));

        if (invincible) {
            invincibleTimer--;
            if (invincibleTimer <= 0) invincible = false;
        }

        if (speedBuffTimer > 0) {
            speedBuffTimer--;
        }

        if (shieldTimer > 0) {
            shieldTimer--;
            if (shieldTimer <= 0) {
                shieldActive = false;
            }
        }

        if (attackSpeedBuffTimer > 0) {
            attackSpeedBuffTimer--;
        }
    }

    public void shoot() {
        int bx = x + GameConstants.PLAYER_WIDTH / 2 - GameConstants.BULLET_WIDTH / 2;
        
        // weaponLevel에 따라 총알 개수 증가 (1~4발)
        // Level 1: 1발 (중앙)
        // Level 2: 2발 (중앙 + 오른쪽)
        // Level 3: 3발 (중앙 + 양옆)
        // Level 4: 4발 (중앙 + 양옆 2개 + 오른쪽)
        
        bullets.add(new Bullet(bx, y, 0, -GameConstants.BULLET_SPEED, true)); // 중앙
        
        if (weaponLevel >= 2) {
            bullets.add(new Bullet(bx + 15, y, 2, -GameConstants.BULLET_SPEED, true)); // 우측
        }
        if (weaponLevel >= 3) {
            bullets.add(new Bullet(bx - 15, y, -2, -GameConstants.BULLET_SPEED, true)); // 좌측
        }
        if (weaponLevel >= 4) {
            bullets.add(new Bullet(bx + 30, y, 4, -GameConstants.BULLET_SPEED, true)); // 우측 2
        }
    }

    public void hit() {
        if (invincible || shieldActive) {
            return;
        }
        hp--;
        invincible      = true;
        invincibleTimer = INVINCIBLE_DURATION;
    }

    public void draw(Graphics2D g) {
        if (invincible && !shieldActive && (invincibleTimer / 5) % 2 == 0) return;

        int cx   = x + GameConstants.PLAYER_WIDTH / 2;
        if (shieldActive) {
            g.setColor(SHIELD_COLOR);
            g.fillOval(cx - 22, y - 10, 44, 54);
            g.setColor(new Color(80, 200, 255));
            g.drawOval(cx - 22, y - 10, 44, 54);
        }
        int[] px = { cx, cx - 16, cx + 16 };
        int[] py = { y,  y + GameConstants.PLAYER_HEIGHT, y + GameConstants.PLAYER_HEIGHT };
        g.setColor(PLAYER_COLOR);
        g.fillPolygon(px, py, 3);
    }

    public Rectangle getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }

    public void addSpeedBuff(int frames) {
        speedBuffTimer = frames;
    }

    public void upgradeWeapon(int level) {
        // 최대 4단계까지 증가
        weaponLevel = Math.min(Math.max(weaponLevel, level), 4);
    }

    public void addShield(int frames) {
        shieldActive = true;
        shieldTimer = Math.max(shieldTimer, frames);
    }

    public void addAttackSpeedBuff(int frames) {
        attackSpeedBuffTimer = frames;
    }

    public boolean hasAttackSpeedBuff() {
        return attackSpeedBuffTimer > 0;
    }

    public List<Bullet> getBullets() { return bullets; }
    public int          getHp()      { return hp; }
    public int          getX()       { return x; }
    public int          getY()       { return y; }
    public boolean      isAlive()    { return hp > 0; }
    public int          getWeaponLevel() { return weaponLevel; }
    public boolean      isShieldActive() { return shieldActive; }

    public void setLeft(boolean v)  { left  = v; }
    public void setRight(boolean v) { right = v; }
    public void setUp(boolean v)    { up    = v; }
    public void setDown(boolean v)  { down  = v; }
}