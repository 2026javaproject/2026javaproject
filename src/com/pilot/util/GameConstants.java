package com.pilot.util;

public class GameConstants {
    public static final int SCREEN_WIDTH   = 480;
    public static final int SCREEN_HEIGHT  = 720;
    public static final int FPS            = 60;
    public static final int DELAY          = 1000 / FPS;

    // Player constants
    public static final int PLAYER_SPEED   = 5;
    public static final int PLAYER_SPEED_BUFFED = 7;
    public static final int PLAYER_HP      = 3;
    public static final int PLAYER_WIDTH   = 40;
    public static final int PLAYER_HEIGHT  = 40;
    public static final int PLAYER_BASE_Y  = 80;
    public static final int PLAYER_INVINCIBLE_DURATION = 90;

    // Bullet constants
    public static final int BULLET_SPEED   = 5;
    public static final int BULLET_WIDTH   = 6;
    public static final int BULLET_HEIGHT  = 14;
    public static final int SHOOT_INTERVAL = 10;
    public static final int SHOOT_INTERVAL_BOOSTED = 5;

    // Enemy constants
    public static final int ENEMY_WIDTH    = 36;
    public static final int ENEMY_HEIGHT   = 36;
    public static final int ENEMY_SPEED    = 2;
    public static final int ENEMY_SPAWN_MIN_INTERVAL = 25;
    public static final int ENEMY_SPAWN_BASE_INTERVAL = 70;

    // Boss constants
    public static final int BOSS_WIDTH     = 90;
    public static final int BOSS_HEIGHT    = 70;
    public static final int BOSS_INITIAL_Y = -BOSS_HEIGHT - 10;
    public static final int BOSS_POSITIONING_Y = 60;
    public static final int SCORE_BOSS     = 1000;

    // Stage constants
    // 스테이지마다 1마리씩 증가하다가 39마리에서 유지
    public static final int[] STAGE_KILL_THRESHOLD = {
        11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
        21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
        31, 32, 33, 34, 35, 36, 37, 38, 39,
        39, 39, 39, 39, 39, 39, 39, 39, 39  // 39마리에서 유지 (최대 44스테이지까지 지원)
    };
    public static final int STAGE_THRESHOLD_LENGTH = STAGE_KILL_THRESHOLD.length;

    // UI constants
    public static final int HUD_FONT_SIZE  = 24;
    public static final int HEART_SIZE     = 20;
    public static final int HEART_SPACING  = 35;

    // Item constants
    public static final int ITEM_SIZE      = 23;
    public static final int ITEM_FALL_SPEED = 2;
    public static final int SHIELD_DURATION = 240;
}