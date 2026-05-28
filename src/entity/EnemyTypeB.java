package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;

public class EnemyTypeB extends Enemy {
    private static final int HP = 2;
    private static final int SPEED = 2;
    private static final int SCORE = 20;
    private static final Color COLOR = Color.BLUE;
    private static final int AMPLITUDE = 40;
    private static final double WAVE_SPEED = 0.15;
    
    private final int baseX;
    private double phase;
    
    public EnemyTypeB(int x, int y) {
        super(x, y, HP, SPEED, SCORE);
        this.baseX = x;
        this.phase = 0.0;
    }

    @Override
    public void move() {
        y += speed;
        phase += WAVE_SPEED;
        int waveX = baseX + (int) Math.round(AMPLITUDE * Math.sin(phase));
        x = Math.max(0, Math.min(waveX, GameConstants.SCREEN_WIDTH - GameConstants.ENEMY_WIDTH));
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
        return new Rectangle(x, y, GameConstants.ENEMY_WIDTH, GameConstants.ENEMY_HEIGHT);
    }
}
