package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;

public class EnemyTypeC extends Enemy {
    private static final int HP = 3;
    private static final int SPEED = 1;
    private static final int SCORE = 30;
    private static final Color COLOR = Color.GREEN;
    private int targetX;
    
    public EnemyTypeC(int x, int y, Player player) {
        super(x, y, HP, SPEED, SCORE);
        this.targetX = x + GameConstants.ENEMY_WIDTH / 2;
    }

    @Override
    public void move() {
        int enemyCenterX = x + GameConstants.ENEMY_WIDTH / 2;
        if (targetX < enemyCenterX) {
            x -= speed;
        } else if (targetX > enemyCenterX) {
            x += speed;
        }
        y += speed;
        x = Math.max(0, Math.min(x, GameConstants.SCREEN_WIDTH - GameConstants.ENEMY_WIDTH));
    }

    @Override
    public void update(Player player, java.util.List<Bullet> bullets) {
        targetX = player.getX() + GameConstants.PLAYER_WIDTH / 2;
        move();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(COLOR);
        g.fillRect(x, y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    }
}
