package com.pilot.manager;

import com.pilot.entity.*;
import java.util.List;
import java.util.Random;

public class ItemManager {
    private static final int DROP_CHANCE = 15;
    private static final int BOSS_DROP_COUNT = 2;
    private static final int BOSS_OFFSET_X_RANGE = 60;
    private static final int BOSS_OFFSET_X_CENTER = 30;
    private static final int BOSS_OFFSET_Y_RANGE = 40;
    private static final int BOSS_OFFSET_Y_CENTER = 20;

    private final Random random = new Random();

    public void tryDrop(int enemyX, int enemyY, List<PowerUpItem> items) {
        if (random.nextInt(DROP_CHANCE) == 0) {
            items.add(new PowerUpItem(PowerUpItem.ItemType.SHIELD, enemyX, enemyY));
        }
    }
    
    public void dropBossItems(int bossX, int bossY, List<PowerUpItem> items) {
        for (int i = 0; i < BOSS_DROP_COUNT; i++) {
            int offsetX = random.nextInt(BOSS_OFFSET_X_RANGE) - BOSS_OFFSET_X_CENTER;
            int offsetY = random.nextInt(BOSS_OFFSET_Y_RANGE) - BOSS_OFFSET_Y_CENTER;
            items.add(new PowerUpItem(PowerUpItem.ItemType.SHIELD, bossX + offsetX, bossY + offsetY));
        }
    }
}
