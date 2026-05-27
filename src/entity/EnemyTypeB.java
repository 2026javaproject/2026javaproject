package com.pilot.entity;

import java.awt.*;

public class EnemyTypeB extends Enemy {
    
    public EnemyTypeB(int x, int y) {
        super(x, y, 2, 2, 20);
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 36, 36);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 36, 36);
    }
}
