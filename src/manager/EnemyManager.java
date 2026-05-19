// manager/EnemyManager.java
package manager;

import entity.Enemy;
import util.GameConstants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class EnemyManager {

    private final List<Enemy> enemies      = new ArrayList<>();
    private final Random      random       = new Random();
    private int               spawnTimer   = 0;
    private static final int  SPAWN_INTERVAL = 90;

    public void update() {
        spawnTimer++;
        if (spawnTimer >= SPAWN_INTERVAL) {
            spawnEnemy();
            spawnTimer = 0;
        }

        Iterator<Enemy> it = enemies.iterator();
        while (it.hasNext()) {
            Enemy e = it.next();
            e.update();
            if (e.isOutOfBounds() || !e.isAlive()) it.remove();
        }
    }

    private void spawnEnemy() {
        int x = random.nextInt(GameConstants.SCREEN_WIDTH - GameConstants.ENEMY_WIDTH);
        enemies.add(new Enemy(x, -GameConstants.ENEMY_HEIGHT));
        // TODO: 웨이브 단위 스폰, 편대 패턴 (Week 2)
    }

    public boolean checkCollision(Rectangle bulletBounds) {
        for (Enemy e : enemies) {
            if (e.getBounds().intersects(bulletBounds)) {
                e.hit();
                return true;
            }
        }
        return false;
    }

    public boolean checkPlayerCollision(Rectangle playerBounds) {
        for (Enemy e : enemies) {
            if (e.getBounds().intersects(playerBounds)) return true;
        }
        return false;
    }

    public void draw(Graphics2D g) {
        for (Enemy e : enemies) e.draw(g);
    }

    public List<Enemy> getEnemies() { return enemies; }
}