package com.pilot.manager;

import com.pilot.entity.*;
import com.pilot.util.GameConstants;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    private int frameCounter;
    private final Random random = new Random();
    private Player player;

    public EnemyManager(Player player) {
        this.player = player;
        this.frameCounter = 0;
    }

    public void setPlayer(Player player) { this.player = player; }
    public void reset() { frameCounter = 0; }

    /** 매 프레임 호출 */
    public void update(List<Enemy> enemies, int stage) {
        frameCounter++;

        int interval = Math.max(70 - stage * 10, 25);
        if (frameCounter % interval == 0) {
            spawnEnemy(enemies, stage);
        }

        // 스테이지 2 이상: 가끔 2마리 동시 스폰
        if (stage >= 2 && frameCounter % (interval * 3) == 0) {
            spawnEnemy(enemies, stage);
        }
    }

    private void spawnEnemy(List<Enemy> enemies, int stage) {
        int x = random.nextInt(
                Math.max(1, GameConstants.SCREEN_WIDTH - GameConstants.ENEMY_WIDTH));
        int y = -GameConstants.ENEMY_HEIGHT;

        int roll = random.nextInt(10);
        if (stage == 1) {
            // A 80%, B 20%
            if (roll < 8) enemies.add(new EnemyTypeA(x, y));
            else          enemies.add(new EnemyTypeB(x, y));
        } else if (stage == 2) {
            // A 40%, B 30%, C 30%
            if      (roll < 4) enemies.add(new EnemyTypeA(x, y));
            else if (roll < 7) enemies.add(new EnemyTypeB(x, y));
            else               enemies.add(new EnemyTypeC(x, y, player));
        } else {
            // A 20%, B 40%, C 40%
            if      (roll < 2) enemies.add(new EnemyTypeA(x, y));
            else if (roll < 6) enemies.add(new EnemyTypeB(x, y));
            else               enemies.add(new EnemyTypeC(x, y, player));
        }
    }
}