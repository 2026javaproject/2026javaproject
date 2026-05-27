package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;

public class PowerUpItem {

    public enum ItemType { SPEED_UP, MULTI_SHOT, SHIELD }

    private final ItemType type;
    private int x, y;
    private boolean collected = false;
    private static final int SIZE = 23;

    public PowerUpItem(ItemType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += 2; // 낙하
    }

    public void applyEffect(Player player) {
        switch (type) {
            case SPEED_UP   -> player.addSpeedBuff(180);   // 3초간 속도 +2
            case MULTI_SHOT -> player.upgradeWeapon(2);    // weaponLevel → 2
            case SHIELD     -> player.activateShield(1);   // 피격 1회 무효
        }
        collected = true;
    }

    public void draw(Graphics2D g) {
        Color c = switch (type) {
            case SPEED_UP   -> Color.YELLOW;
            case MULTI_SHOT -> Color.CYAN;
            case SHIELD     -> Color.GREEN;
        };
        g.setColor(c);
        g.fillOval(x, y, SIZE, SIZE);
        g.setColor(Color.WHITE);
        g.drawOval(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds()  { return new Rectangle(x, y, SIZE, SIZE); }
    public boolean isCollected()  { return collected; }
    public boolean isOffScreen()  { return y > GameConstants.SCREEN_HEIGHT; }
    public ItemType getType()     { return type; }
    public int getX()             { return x; }
    public int getY()             { return y; }
}