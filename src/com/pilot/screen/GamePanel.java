package com.pilot.screen;

import com.pilot.entity.*;
import com.pilot.main.GameMain;
import com.pilot.manager.EnemyManager;
import com.pilot.manager.ItemManager;
import com.pilot.ui.HUD;
import com.pilot.util.GameConstants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    private final GameMain gameMain;
    private Thread gameThread;
    private volatile boolean running;
    private volatile boolean paused;

    private final Object stateLock = new Object();

    private Player player;
    private final List<Enemy> enemies = new ArrayList<>();
    private final List<Bullet> enemyBullets = new ArrayList<>();
    private final List<PowerUpItem> items = new ArrayList<>();
    private final List<Explosion> explosions = new ArrayList<>();
    private final List<Star> stars = new ArrayList<>();

    private final EnemyManager enemyManager;
    private final ItemManager itemManager;
    private BossEnemy boss;
    private boolean bossActive;
    private boolean bossPendingWarning;
    private boolean bossWarningActive;

    private int stage = 1;
    private int kills = 0;
    private int score = 0;
    private int shootCooldown = 0;
    private boolean shooting;

    private final HUD hud;

    private final JButton pauseButton;
    private final JButton mainMenuButton;

    public GamePanel(GameMain gameMain) {
        this.gameMain = gameMain;
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);

        player = new Player(GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT - 80);
        enemyManager = new EnemyManager(player);
        itemManager = new ItemManager();
        hud = new HUD();

        initStars();

        pauseButton = new JButton("PAUSE");
        pauseButton.setFocusable(false);
        pauseButton.setBounds(GameConstants.SCREEN_WIDTH - 120, 10, 100, 30);
        pauseButton.addActionListener(e -> togglePause());
        pauseButton.setVisible(false);
        add(pauseButton);

        mainMenuButton = new JButton("MAIN MENU");
        mainMenuButton.setFocusable(false);
        int menuButtonWidth = 180;
        int menuButtonHeight = 40;
        int menuButtonX = (GameConstants.SCREEN_WIDTH - menuButtonWidth) / 2;
        int menuButtonY = (GameConstants.SCREEN_HEIGHT - menuButtonHeight) / 2 + 60;
        mainMenuButton.setBounds(menuButtonX, menuButtonY, menuButtonWidth, menuButtonHeight);
        mainMenuButton.addActionListener(e -> returnToMainMenu());
        mainMenuButton.setVisible(false);
        add(mainMenuButton);

        // 자동공격 활성화
        shooting = true;

        setupInput();
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }

    public void startGameLoop() {
        if (running) {
            return;
        }
        running = true;
        gameThread = new Thread(this, "PILOT-GameLoop");
        gameThread.start();
    }

    private void setupInput() {
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // WASD 키 처리 (WHEN_IN_FOCUSED_WINDOW)
        inputMap.put(KeyStroke.getKeyStroke('W'), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke('w'), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke('A'), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke('a'), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke('S'), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke('s'), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke('D'), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke('d'), "moveRight");

        // 화살표 키 처리
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");

        // ESC 키 처리
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "togglePause");

        // Up 액션
        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setUp(true);
            }
        });

        // Left 액션
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setLeft(true);
            }
        });

        // Down 액션
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setDown(true);
            }
        });

        // Right 액션
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.setRight(true);
            }
        });

        // Pause 액션
        actionMap.put("togglePause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });

        // KeyRelease 처리를 위해 KeyListener 추가
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> player.setUp(false);
                    case KeyEvent.VK_LEFT -> player.setLeft(false);
                    case KeyEvent.VK_DOWN -> player.setDown(false);
                    case KeyEvent.VK_RIGHT -> player.setRight(false);
                }
                char c = Character.toUpperCase(e.getKeyChar());
                switch (c) {
                    case 'W' -> player.setUp(false);
                    case 'A' -> player.setLeft(false);
                    case 'S' -> player.setDown(false);
                    case 'D' -> player.setRight(false);
                }
            }
        });
    }

    private void togglePause() {
        setPaused(!paused);
    }

    private void setPaused(boolean paused) {
        this.paused = paused;
        pauseButton.setText(paused ? "RESUME" : "PAUSE");
        pauseButton.setVisible(paused);
        mainMenuButton.setVisible(paused);
    }

    private void returnToMainMenu() {
        running = false;
        setPaused(false);
        SwingUtilities.invokeLater(() -> gameMain.showMainMenu());
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / GameConstants.FPS;
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerFrame;
            lastTime = now;

            boolean shouldRender = false;
            while (delta >= 1) {
                if (!paused) {
                    updateGame();
                }
                delta--;
                shouldRender = true;
            }

            if (shouldRender) {
                repaint();
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                running = false;
            }
        }
    }

    private void updateGame() {
        synchronized (stateLock) {
            updateStars();
            player.update();
            handleShooting();
            updatePlayerBullets();
            updateEnemyBullets();
            updateEnemies();
            updateItems();
            updateExplosions();
            checkBossTrigger();
            hud.updateStats(score, player.getHp(), stage, player.isShieldActive(), kills);
            hud.updateWarning();
            maybeSpawnBoss();
            if (!player.isAlive()) {
                endGame();
            }
        }
    }

    private void handleShooting() {
        if (shootCooldown > 0) {
            shootCooldown--;
        }
        if (shooting && shootCooldown <= 0) {
            player.shoot();
            int interval = GameConstants.SHOOT_INTERVAL;
            if (player.hasAttackSpeedBuff()) {
                interval = Math.max(1, interval / 2);
            }
            shootCooldown = interval;
        }
    }

    private void updatePlayerBullets() {
        Iterator<Bullet> it = player.getBullets().iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.update();
            if (bullet.isOutOfBounds()) {
                it.remove();
            }
        }
    }

    private void updateEnemyBullets() {
        Iterator<Bullet> it = enemyBullets.iterator();
        while (it.hasNext()) {
            Bullet bullet = it.next();
            bullet.update();
            if (bullet.isOutOfBounds()) {
                it.remove();
                continue;
            }
            if (bullet.getBounds().intersects(player.getBounds())) {
                player.hit();
                it.remove();
            }
        }
    }

    private void updateEnemies() {
        if (hud.isWarningActive() || bossWarningActive) {
            return; // 경고 중에는 적 이동/충돌/스폰 전체 중단
        }

        if (!bossActive) {
            enemyManager.update(enemies, stage);
        }

        Iterator<Enemy> enemyIt = enemies.iterator();
        while (enemyIt.hasNext()) {
            Enemy enemy = enemyIt.next();
            enemy.update(player, enemyBullets);

            if (enemy.isOffScreen()) {
                enemyIt.remove();
                continue;
            }

            if (enemy.getBounds().intersects(player.getBounds())) {
                player.hit();
                addExplosion(enemy.getX(), enemy.getY(), 36);
                enemyIt.remove();
                continue;
            }

            boolean destroyed = false;
            Iterator<Bullet> bulletIt = player.getBullets().iterator();
            while (bulletIt.hasNext()) {
                Bullet bullet = bulletIt.next();
                if (bullet.getBounds().intersects(enemy.getBounds())) {
                    enemy.takeDamage(1);
                    bulletIt.remove();
                    if (!enemy.isAlive()) {
                        score += enemy.getScoreValue();
                        kills++;
                        itemManager.tryDrop(enemy.getX(), enemy.getY(), items);
                        addExplosion(enemy.getX(), enemy.getY(), 36);
                        enemyIt.remove();
                        destroyed = true;
                    }
                    break;
                }
            }

            if (destroyed) {
                continue;
            }
        }

        if (bossActive && boss != null) {
            boss.update(player, enemyBullets);
            
            // 보스가 발사한 총알을 enemyBullets에 추가
            enemyBullets.addAll(boss.getBullets());
            boss.getBullets().clear();

            if (boss.getBounds().intersects(player.getBounds())) {
                player.hit();
            }

            Iterator<Bullet> bulletIt = player.getBullets().iterator();
            while (bulletIt.hasNext()) {
                Bullet bullet = bulletIt.next();
                if (bullet.getBounds().intersects(boss.getBounds())) {
                    boss.takeDamage(1);
                    bulletIt.remove();
                    if (!boss.isAlive()) {
                        score += boss.getScoreValue();
                        addExplosion(boss.getX(), boss.getY(), 90);
                        
                        // 보스 아이템 드롭 (2-3개 랜덤)
                        itemManager.dropBossItems(boss.getX(), boss.getY(), items);
                        
                        bossActive = false;
                        boss = null;
                        bossPendingWarning = false;
                        kills = 0;
                        stage++;
                        enemyManager.reset();
                    }
                    break;
                }
            }
        }
    }

    private void updateItems() {
        Iterator<PowerUpItem> it = items.iterator();
        while (it.hasNext()) {
            PowerUpItem item = it.next();
            item.update();
            if (item.isOffScreen()) {
                it.remove();
                continue;
            }
            if (item.getBounds().intersects(player.getBounds())) {
                item.applyEffect(player);
                it.remove();
            }
        }
    }

    private void updateExplosions() {
        Iterator<Explosion> it = explosions.iterator();
        while (it.hasNext()) {
            Explosion ex = it.next();
            ex.update();
            if (ex.isDone()) {
                it.remove();
            }
        }
    }

    private void checkBossTrigger() {
        if (bossActive || bossPendingWarning) {
            return;
        }
        int index = stage - 1;
        if (index < 0 || index >= GameConstants.STAGE_KILL_THRESHOLD.length) {
            return;
        }
        if (kills >= GameConstants.STAGE_KILL_THRESHOLD[Math.min(index, GameConstants.STAGE_KILL_THRESHOLD.length - 1)]) {
            bossPendingWarning = true;
            bossWarningActive = true;
            hud.startWarning(150);
        }
    }

    private void maybeSpawnBoss() {
        if (!bossPendingWarning || hud.isWarningActive() || bossActive) {
            return;
        }
        boss = new BossEnemy();
        bossActive = true;
        bossPendingWarning = false;
        bossWarningActive = false;
    }

    private void updateStars() {
        for (Star star : stars) {
            star.update();
        }
    }

    private void initStars() {
        Random random = new Random();
        for (int i = 0; i < 60; i++) {
            stars.add(new Star(
                    random.nextInt(GameConstants.SCREEN_WIDTH),
                    random.nextInt(GameConstants.SCREEN_HEIGHT),
                    1 + random.nextInt(2),
                    1 + random.nextInt(3)
            ));
        }
    }

    private void addExplosion(int x, int y, int size) {
        explosions.add(new Explosion(x + size / 2, y + size / 2, size));
    }

    private void endGame() {
        running = false;
        SwingUtilities.invokeLater(() -> gameMain.showGameOver(score));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        synchronized (stateLock) {
            drawBackground(g2);

            for (PowerUpItem item : items) {
                item.draw(g2);
            }

            for (Enemy enemy : enemies) {
                enemy.draw(g2);
            }

            if (bossActive && boss != null) {
                boss.draw(g2);
            }

            for (Bullet bullet : player.getBullets()) {
                bullet.draw(g2);
            }

            for (Bullet bullet : enemyBullets) {
                bullet.draw(g2);
            }

            player.draw(g2);

            for (Explosion ex : explosions) {
                ex.draw(g2);
            }

            hud.draw(g2);

            if (paused) {
                drawPauseOverlay(g2);
            }
        }
    }

    private void drawBackground(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        for (Star star : stars) {
            star.draw(g2);
        }
    }

    private void drawPauseOverlay(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRect(0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Dialog", Font.BOLD, 36));
        String text = "PAUSED";
        FontMetrics fm = g2.getFontMetrics();
        int x = (GameConstants.SCREEN_WIDTH - fm.stringWidth(text)) / 2;
        int y = (GameConstants.SCREEN_HEIGHT - fm.getAscent()) / 2;
        g2.drawString(text, x, y);
    }

    private static class Explosion {
        private final int x;
        private final int y;
        private final int maxRadius;
        private int timer;

        private Explosion(int x, int y, int size) {
            this.x = x;
            this.y = y;
            this.maxRadius = Math.max(18, size / 2 + 10);
        }

        private void update() {
            timer++;
        }

        private boolean isDone() {
            return timer >= 24;
        }

        private void draw(Graphics2D g2) {
            float progress = timer / 24f;
            int radius = (int) (maxRadius * progress);
            int alpha = (int) (200 * (1 - progress));
            g2.setColor(new Color(255, 120, 50, Math.max(alpha, 0)));
            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);
            g2.setColor(new Color(255, 200, 120, Math.max(alpha - 40, 0)));
            g2.drawOval(x - radius, y - radius, radius * 2, radius * 2);
        }
    }

    private static class Star {
        private int x;
        private int y;
        private final int size;
        private final int speed;

        private Star(int x, int y, int size, int speed) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.speed = speed;
        }

        private void update() {
            y += speed;
            if (y > GameConstants.SCREEN_HEIGHT) {
                y = -size;
            }
        }

        private void draw(Graphics2D g2) {
            g2.setColor(new Color(180, 220, 255));
            g2.fillOval(x, y, size, size);
        }
    }
}