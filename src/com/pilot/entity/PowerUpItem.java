package com.pilot.entity;

import com.pilot.util.GameConstants;
import java.awt.*;

/**
 * PowerUpItem - 게임에서 획득할 수 있는 파워업 아이템
 * 
 * 아이템 종류 (색상별):
 * ┌─────────────────────────────────────────────────────────────┐
 * │ 🟡 YELLOW (SPEED_UP)                                         │
 * │    기능: 이동 속도 +2 (3초간)                                │
 * │    효과: 빠른 피하기 가능                                    │
 * ├─────────────────────────────────────────────────────────────┤
 * │ 🔵 CYAN (MULTI_SHOT)                                         │
 * │    기능: 무기 레벨 +1 (최대 4단계)                           │
 * │    효과: 레벨별 발사 총알 개수                               │
 * │         Lv1: 1발 (중앙)                                      │
 * │         Lv2: 2발 (중앙 + 우측)                               │
 * │         Lv3: 3발 (중앙 + 양옆)                               │
 * │         Lv4: 4발 (중앙 + 양옆 2개 + 우측2)                  │
 * ├─────────────────────────────────────────────────────────────┤
 * │ 🟢 GREEN (SHIELD)                                            │
 * │    기능: 체력 1 회복 (최대 3까지)                            │
 * │    효과: 체력이 3(최대)이면 효과 없음                        │
 * ├─────────────────────────────────────────────────────────────┤
 * │ 🟣 MAGENTA (ATTACK_SPEED)                                    │
 * │    기능: 공격 속도 2배 증가 (4초간)                          │
 * │    효과: 총알 발사 간격이 절반으로 단축                      │
 * └─────────────────────────────────────────────────────────────┘
 */
public class PowerUpItem {

    public enum ItemType { 
        SPEED_UP,       // 🟡 노란색 - 이동 속도 증가
        MULTI_SHOT,     // 🔵 청록색 - 멀티샷 무기
        SHIELD,         // 🟢 초록색 - 방어막
        ATTACK_SPEED    // 🟣 자홍색 - 공격 속도 증가
    }

    private final ItemType type;
    private int x, y;
    private boolean collected = false;
    private static final int SIZE = 23;
    private int pulseTimer = 0;

    // 아이템 색상 정의 (색각이상 친화적)
    private static final Color SPEED_COLOR = Color.YELLOW;      // 🟡 밝음 (가시성 높음)
    private static final Color MULTI_COLOR = Color.CYAN;        // 🔵 밝은 청록
    private static final Color SHIELD_COLOR = Color.GREEN;      // 🟢 밝은 초록
    private static final Color ATTACK_COLOR = Color.MAGENTA;    // 🟣 밝은 자홍
    
    private final Rectangle bounds = new Rectangle(0, 0, SIZE, SIZE);

    public PowerUpItem(ItemType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update() {
        y += 2; // 낙하
        pulseTimer++;
    }

    public void applyEffect(Player player) {
        switch (type) {
            case SPEED_UP -> player.addSpeedBuff(180);          // 3초 (180프레임 @ 60FPS)
            case MULTI_SHOT -> player.upgradeWeapon(player.getWeaponLevel() + 1); // 무기 레벨 +1 (최대 4)
            case SHIELD -> player.recoverHealth(1);             // 체력 1 회복 (최대 3)
            case ATTACK_SPEED -> player.addAttackSpeedBuff(240); // 4초 (240프레임 @ 60FPS)
        }
        collected = true;
    }

    public void draw(Graphics2D g) {
        Color c = switch (type) {
            case SPEED_UP -> SPEED_COLOR;
            case MULTI_SHOT -> MULTI_COLOR;
            case SHIELD -> SHIELD_COLOR;
            case ATTACK_SPEED -> ATTACK_COLOR;
        };

        // 펄스 효과: 크기와 투명도 변화
        double pulse = (Math.sin(pulseTimer * 0.18) + 1.0) / 2.0; // 0~1 범위
        int glowSize = SIZE + (int) (10 * pulse);
        int glowX = x - (glowSize - SIZE) / 2;
        int glowY = y - (glowSize - SIZE) / 2;
        int alpha = 60 + (int) (120 * pulse);

        // 외부 빛 효과
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha));
        g.fillOval(glowX, glowY, glowSize, glowSize);

        // 본체
        g.setColor(c);
        g.fillOval(x, y, SIZE, SIZE);
        
        // 테두리
        g.setColor(Color.WHITE);
        g.drawOval(x, y, SIZE, SIZE);
    }

    public Rectangle getBounds() {
        bounds.x = x;
        bounds.y = y;
        return bounds;
    }
    
    public boolean isCollected()  { return collected; }
    public boolean isOffScreen()  { return y > GameConstants.SCREEN_HEIGHT; }
    public ItemType getType()     { return type; }
    public int getX()             { return x; }
    public int getY()             { return y; }
}