package com.pilot.entity;

import java.awt.*;

public class EnemyTypeA extends Enemy {
    
    public EnemyTypeA(int x, int y) {
        super(x, y, 1, 2, 10);
    }

    @Override
    public void move() {
        y += speed;
    }

    @Override
    public void update(Player player, java.util.List<Bullet> bullets) {
        move();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 36, 36);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 36, 36);
    }
}
