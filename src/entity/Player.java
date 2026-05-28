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
    private int attackSpeedBuffTimer = 0;

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
        
        if (attackSpeedBuffTimer > 0) {
            attackSpeedBuffTimer--;
        }
    }

    public void shoot() {
        int bx = x + GameConstants.PLAYER_WIDTH / 2 - GameConstants.BULLET_WIDTH / 2;
        bullets.add(new Bullet(bx, y, 0, -GameConstants.BULLET_SPEED, true));
        
        if (weaponLevel >= 2) {
            bullets.add(new Bullet(bx - 15, y, -2, -GameConstants.BULLET_SPEED, true));
            bullets.add(new Bullet(bx + 15, y, 2, -GameConstants.BULLET_SPEED, true));
        }
    }

    public void hit() {
        if (invincible || shieldActive) {
            if (shieldActive) {
                shieldActive = false;
            }
            return;
        }
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
        
        if (shieldActive) {
            g.setColor(new Color(0, 255, 0, 100));
            g.drawOval(cx - 25, y - 5, 50, 50);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
    }

    public void addSpeedBuff(int frames) {
        speedBuffTimer = frames;
    }
    
    public void upgradeWeapon(int level) {
        weaponLevel = Math.max(weaponLevel, level);
    }
    
    public void activateShield(int durability) {
        shieldActive = true;
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
    public boolean      isShieldActive() { return shieldActive; }

    public void setLeft(boolean v)  { left  = v; }
    public void setRight(boolean v) { right = v; }
    public void setUp(boolean v)    { up    = v; }
    public void setDown(boolean v)  { down  = v; }
}