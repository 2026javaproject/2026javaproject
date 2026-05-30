package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Boss enemy with two phases:
 * Phase 1 (HP > 50%): 3-bullet spread, 70 frames interval
 * Phase 2 (HP ≤ 50%): 5-bullet spread + player-tracking shot, 40 frames interval
 */
public class BossEnemy extends Enemy {
    private static final int PHASE1_BULLET_COUNT = 3;
    private static final int PHASE2_BULLET_COUNT = 5;
    private static final int PHASE1_INTERVAL = 70;
    private static final int PHASE2_INTERVAL = 40;
    private static final int PHASE1_SPEED = 2;
    private static final int PHASE2_SPEED = 3;
    private static final double SPREAD_ANGLE = Math.PI / 2.5;
    private static final int BULLET_SPREAD_SPEED = 5;
    private static final int TRACKING_BULLET_SPEED = 6;
    private static final int HORIZONTAL_MARGIN = 10;
    private static final int VERTICAL_MARGIN = 12;

    private final int maxHp;
    private int phase;
    private int shootTimer;
    private int moveDir;
    private boolean inPosition;
    private final List<Bullet> bossBullets = new ArrayList<>();
    
    private final Rectangle bounds = new Rectangle(0, 0, 
            GameConstants.BOSS_WIDTH - 24, GameConstants.BOSS_HEIGHT - VERTICAL_MARGIN);

    public BossEnemy() {
        this(30);
    }
    
    public BossEnemy(int hp) {
        super(GameConstants.SCREEN_WIDTH / 2 - GameConstants.BOSS_WIDTH / 2,
                GameConstants.BOSS_INITIAL_Y, hp, PHASE1_SPEED, GameConstants.SCORE_BOSS);
        this.maxHp = hp;
        this.phase = 1;
        this.shootTimer = 0;
        this.moveDir = 1;
        this.inPosition = false;
    }

    @Override
    public void move() {
        if (!inPosition) {
            y += 2;
            if (y >= GameConstants.BOSS_POSITIONING_Y) inPosition = true;
            return;
        }
        x += speed * moveDir;
        if (x > GameConstants.SCREEN_WIDTH - GameConstants.BOSS_WIDTH - HORIZONTAL_MARGIN) {
            moveDir = -1;
        }
        if (x < HORIZONTAL_MARGIN) {
            moveDir = 1;
        }
    }

    @Override
    public void update(Player player, List<Bullet> bullets) {
        move();
        updatePhase();

        if (inPosition) {
            shootTimer++;
            int interval = (phase == 1) ? PHASE1_INTERVAL : PHASE2_INTERVAL;
            if (shootTimer >= interval) {
                fireBullets(player);
                shootTimer = 0;
            }
        }
    }

    private void updatePhase() {
        if (hp <= maxHp / 2 && phase == 1) {
            phase = 2;
            speed = PHASE2_SPEED;
        }
    }

    private void fireBullets(Player player) {
        int cx = x + GameConstants.BOSS_WIDTH / 2;
        int cy = y + GameConstants.BOSS_HEIGHT;
        int bulletCount = (phase == 1) ? PHASE1_BULLET_COUNT : PHASE2_BULLET_COUNT;

        fireSpreadBullets(cx, cy, bulletCount);
        
        if (phase == 2 && player != null) {
            fireTrackingBullet(cx, cy, player);
        }
    }

    private void fireSpreadBullets(int cx, int cy, int count) {
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI / 2) - SPREAD_ANGLE / 2 + SPREAD_ANGLE * i / Math.max(count - 1, 1);
            int sx = (int)(Math.cos(angle) * BULLET_SPREAD_SPEED);
            int sy = Math.max((int)(Math.sin(angle) * BULLET_SPREAD_SPEED), 3);
            bossBullets.add(new Bullet(cx - 5, cy, sx, sy, false));
        }
    }

    private void fireTrackingBullet(int cx, int cy, Player player) {
        int dx = player.getX() + GameConstants.PLAYER_WIDTH / 2 - cx;
        int dy = player.getY() + GameConstants.PLAYER_HEIGHT / 2 - cy;
        double dist = Math.sqrt((double)dx * dx + (double)dy * dy);
        
        if (dist > 0) {
            int sx = (int)(dx / dist * TRACKING_BULLET_SPEED);
            int sy = (int)(dy / dist * TRACKING_BULLET_SPEED);
            bossBullets.add(new Bullet(cx - 5, cy, sx, Math.max(sy, 2), false));
        }
    }

    @Override
    public Rectangle getBounds() {
        bounds.x = x + VERTICAL_MARGIN;
        bounds.y = y + VERTICAL_MARGIN;
        return bounds;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(phase == 1 ? Color.RED : Color.ORANGE);
        g.fillRect(x, y, GameConstants.BOSS_WIDTH, GameConstants.BOSS_HEIGHT);

        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y - 10, GameConstants.BOSS_WIDTH, 6);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int)(GameConstants.BOSS_WIDTH * ((double)hp / maxHp)), 6);
    }

    public int getPhase() { return phase; }
    public double getHpRatio() { return (double) hp / maxHp; }
    public List<Bullet> getBullets() { return bossBullets; }
}