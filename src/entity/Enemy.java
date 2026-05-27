package com.pilot.entity;

import java.awt.*;
import java.util.List;

public abstract class Enemy {
    protected int x, y;
    protected int hp;
    protected int speed;
    protected int scoreValue;

    public Enemy(int x, int y, int hp, int speed, int scoreValue) {
        this.x = x;
        this.y = y;
        this.hp = hp;
        this.speed = speed;
        this.scoreValue = scoreValue;
    }

    public abstract void move();
    public abstract void update(Player player, List<Bullet> bullets);
    public abstract void draw(Graphics2D g);
    public abstract Rectangle getBounds();

    public void takeDamage(int damage) {
        hp -= damage;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public boolean isOffScreen() {
        return y > 800;
    }

    public int getScoreValue() { return scoreValue; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHp() { return hp; }
}