package com.pilot.entity;

import java.awt.*;

public class EnemyTypeC extends Enemy {
    
    private Player targetPlayer;
    
    public EnemyTypeC(int x, int y, Player player) {
        super(x, y, 3, 1, 30);
        this.targetPlayer = player;
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
        g.setColor(Color.GREEN);
        g.fillRect(x, y, 36, 36);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 36, 36);
    }
}
