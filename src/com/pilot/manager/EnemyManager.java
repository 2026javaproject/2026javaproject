package com.pilot.manager;

import com.pilot.entity.*;
import com.pilot.util.GameConstants;
import java.util.List;
import java.util.Random;

public class EnemyManager {
    private static final int DUAL_SPAWN_THRESHOLD = 2;
    private static final int ENEMY_TYPE_A_THRESHOLD_STAGE1 = 8;
    private static final int ENEMY_TYPE_B_START_STAGE1 = 10;
    private static final int ENEMY_TYPE_A_THRESHOLD_STAGE2 = 3;
    private static final int ENEMY_TYPE_B_THRESHOLD_STAGE2 = 6;
    private static final int TOTAL_ENEMY_TYPES = 10;
    private static final int DUAL_SPAWN_INTERVAL_MULTIPLIER = 3;

    private int frameCounter;
    private final Random random = new Random();
    private Player player;

    public EnemyManager(Player player) {
        this.player = player;
        this.frameCounter = 0;
    }

    public void setPlayer(Player player) { 
        this.player = player; 
    }
    
    public void reset() { 
        frameCounter = 0; 
    }

    public void update(List<Enemy> enemies, int stage) {
        frameCounter++;

        int interval = calculateSpawnInterval(stage);
        if (frameCounter % interval == 0) {
            spawnEnemy(enemies, stage);
        }

        if (stage >= DUAL_SPAWN_THRESHOLD && frameCounter % (interval * DUAL_SPAWN_INTERVAL_MULTIPLIER) == 0) {
            spawnEnemy(enemies, stage);
        }
    }

    private int calculateSpawnInterval(int stage) {
        return Math.max(GameConstants.ENEMY_SPAWN_BASE_INTERVAL - stage * 10, 
                        GameConstants.ENEMY_SPAWN_MIN_INTERVAL);
    }

    private void spawnEnemy(List<Enemy> enemies, int stage) {
        int x = random.nextInt(Math.max(1, GameConstants.SCREEN_WIDTH - GameConstants.ENEMY_WIDTH));
        int y = -GameConstants.ENEMY_HEIGHT;

        int roll = random.nextInt(TOTAL_ENEMY_TYPES);
        Enemy enemy = selectEnemyType(roll, x, y, stage);
        
        // 5스테이지 이상: HP 2배
        if (stage >= 5) {
            enemy.setHp(enemy.getHp() * 2);
        }
        
        enemies.add(enemy);
    }

    private Enemy selectEnemyType(int roll, int x, int y, int stage) {
        if (stage <= 2) {
            return roll < ENEMY_TYPE_A_THRESHOLD_STAGE1 ? 
                new EnemyTypeA(x, y) : new EnemyTypeB(x, y);
        } else if (stage < 5) {
            if (roll < 4) return new EnemyTypeA(x, y);
            else if (roll < 7) return new EnemyTypeB(x, y);
            else return new EnemyTypeC(x, y, player);
        } else {
            if (roll < 3) return new EnemyTypeA(x, y);
            else if (roll < 6) return new EnemyTypeB(x, y);
            else return new EnemyTypeC(x, y, player);
        }
    }
}