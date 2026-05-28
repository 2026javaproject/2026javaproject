package com.pilot.manager;

import com.pilot.entity.*;
import java.awt.Rectangle;
import java.util.List;

public class CollisionDetector {
    
    public static void checkBulletEnemyCollision(List<Bullet> bullets, List<Enemy> enemies) {
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isPlayerBullet()) continue;
            
            for (int j = enemies.size() - 1; j >= 0; j--) {
                Enemy enemy = enemies.get(j);
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    applyBulletDamage(bullet, enemy);
                    bullets.remove(i);
                    break;
                }
            }
        }
    }
    
    public static void checkBulletBossCollision(List<Bullet> bullets, BossEnemy boss) {
        if (boss == null) return;
        
        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bullets.get(i);
            if (!bullet.isPlayerBullet()) continue;
            
            if (bullet.getBounds().intersects(boss.getBounds())) {
                applyBulletDamage(bullet, boss);
                bullets.remove(i);
                break;
            }
        }
    }
    
    public static void checkPlayerEnemyCollision(Player player, List<Enemy> enemies) {
        Rectangle playerBounds = player.getBounds();
        for (Enemy enemy : enemies) {
            if (playerBounds.intersects(enemy.getBounds())) {
                player.hit();
                break;
            }
        }
    }
    
    public static void checkPlayerBossBulletCollision(Player player, List<Bullet> bossBullets) {
        if (bossBullets == null || bossBullets.isEmpty()) return;
        
        Rectangle playerBounds = player.getBounds();
        for (int i = bossBullets.size() - 1; i >= 0; i--) {
            Bullet bullet = bossBullets.get(i);
            if (!bullet.isPlayerBullet() && playerBounds.intersects(bullet.getBounds())) {
                player.hit();
                bossBullets.remove(i);
                break;
            }
        }
    }
    
    public static void checkPlayerItemCollision(Player player, List<PowerUpItem> items) {
        Rectangle playerBounds = player.getBounds();
        for (int i = items.size() - 1; i >= 0; i--) {
            PowerUpItem item = items.get(i);
            if (playerBounds.intersects(item.getBounds())) {
                item.applyEffect(player);
                items.remove(i);
            }
        }
    }

    private static void applyBulletDamage(Bullet bullet, Enemy enemy) {
        int damage = (int) Math.ceil(bullet.getDamage());
        enemy.takeDamage(damage);
    }
}
