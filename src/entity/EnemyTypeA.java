package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;

public class EnemyTypeA extends Enemy {
    private static final int HP = 1;
    private static final int SPEED = 2;
    private static final int SCORE = 10;
    private static final Color COLOR = Color.RED;
    
    private final Rectangle bounds = new Rectangle(0, 0, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    
    public EnemyTypeA(int x, int y) {
        super(x, y, HP, SPEED, SCORE);
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
        g.setColor(COLOR);
        g.fillRect(x, y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    }

    @Override
    public Rectangle getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }
}
