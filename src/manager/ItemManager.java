package com.pilot.manager;

import com.pilot.entity.PowerUpItem;
import java.util.List;
import java.util.Random;

public class ItemManager {

    private static final int DROP_CHANCE = 15; // 1/15 확률
    private final Random random = new Random();

    /** 적이 죽은 위치에서 아이템 드롭 시도 */
    public void tryDrop(int enemyX, int enemyY, List<PowerUpItem> items) {
        if (random.nextInt(DROP_CHANCE) == 0) {
            PowerUpItem.ItemType[] types = PowerUpItem.ItemType.values();
            PowerUpItem.ItemType type = types[random.nextInt(types.length)];
            items.add(new PowerUpItem(type, enemyX, enemyY));
        }
    }
}