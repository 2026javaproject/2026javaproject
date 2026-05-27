package com.pilot.entity;

import com.pilot.util.GameConstants;
import com.pilot.entity.Bullet;
import com.pilot.entity.Enemy;
import com.pilot.entity.Player;

import java.awt.*;
import java.util.List;

/**
 * 페이즈 1 (HP > 50%): 3발 부채꼴, 70프레임마다
 * 페이즈 2 (HP ≤ 50%): 5발 부채꼴, 40프레임마다 + 플레이어 조준 발사
 */
public class BossEnemy extends Enemy {

    private static final int BOSS_W = 90;
    private static final int BOSS_H = 70;

    private final int maxHp;
    private int phase;
    private int shootTimer;
    private int moveDir;
    private boolean inPosition;

    public BossEnemy() {
        super(GameConstants.SCREEN_WIDTH / 2 - 45, -BOSS_H - 10,
                30, 2, GameConstants.SCORE_BOSS);
        this.maxHp = 30;
        this.phase = 1;
        this.shootTimer = 0;
        this.moveDir = 1;
        this.inPosition = false;
    }

    @Override
    public void move() {
        if (!inPosition) {
            y += 2;
            if (y >= 60) inPosition = true;
            return;
        }
        x += speed * moveDir;
        if (x > GameConstants.SCREEN_WIDTH - BOSS_W - 10) moveDir = -1;
        if (x < 10) moveDir = 1;
    }

    @Override
    public void update(Player player, List<Bullet> bullets) {
        move();

        // 페이즈 전환
        if (hp <= maxHp / 2 && phase == 1) {
            phase = 2;
            speed = 3;
        }

        if (inPosition) {
            shootTimer++;
            int interval = (phase == 1) ? 70 : 40;
            if (shootTimer >= interval) {
                fireBullets(bullets, player);
                shootTimer = 0;
            }
        }
    }

    private void fireBullets(List<Bullet> bullets, Player player) {
        int cx = x + BOSS_W / 2;
        int cy = y + BOSS_H;
        int count = (phase == 1) ? 3 : 5;

        double spread = Math.PI / 2.5;
        for (int i = 0; i < count; i++) {
            double angle = (Math.PI / 2)
                    - spread / 2
                    + spread * i / Math.max(count - 1, 1);
            int sx = (int)(Math.cos(angle) * 5);
            int sy = Math.max((int)(Math.sin(angle) * 5), 3);
            bullets.add(new Bullet(cx - 5, cy, sx, sy, false));
        }

        // 페이즈 2: 플레이어 조준 발사
        if (phase == 2 && player != null) {
            int dx = player.getX() + 20 - cx;
            int dy = player.getY() + 25 - cy;
            double dist = Math.sqrt((double)dx * dx + (double)dy * dy);
            if (dist > 0) {
                int sx2 = (int)(dx / dist * 6);
                int sy2 = (int)(dy / dist * 6);
                bullets.add(new Bullet(cx - 5, cy, sx2, Math.max(sy2, 2), false));
            }
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 12, y + 12, BOSS_W - 24, BOSS_H - 12);
    }

    @Override
    public void draw(Graphics2D g) {
        // TODO: 스프라이트 또는 도형 렌더링
        g.setColor(phase == 1 ? Color.RED : Color.ORANGE);
        g.fillRect(x, y, BOSS_W, BOSS_H);

        // 체력바
        g.setColor(Color.DARK_GRAY);
        g.fillRect(x, y - 10, BOSS_W, 6);
        g.setColor(Color.GREEN);
        g.fillRect(x, y - 10, (int)(BOSS_W * ((double)hp / maxHp)), 6);
    }

    public int getPhase() { return phase; }
    public double getHpRatio() { return (double) hp / maxHp; }
}