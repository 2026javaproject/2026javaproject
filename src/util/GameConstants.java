package com.pilot.util;

public class GameConstants {
    public static final int SCREEN_WIDTH   = 480;
    public static final int SCREEN_HEIGHT  = 720;
    public static final int FPS            = 60;
    public static final int DELAY          = 1000 / FPS;

    public static final int PLAYER_SPEED   = 5;
    public static final int BULLET_SPEED   = 5;
    public static final int SHOOT_INTERVAL = 10;
    public static final int ENEMY_SPEED    = 2;

    public static final int PLAYER_HP      = 3;
    public static final int PLAYER_WIDTH   = 40;
    public static final int PLAYER_HEIGHT  = 40;

    public static final int BULLET_WIDTH   = 6;
    public static final int BULLET_HEIGHT  = 14;

    public static final int ENEMY_WIDTH    = 36;
    public static final int ENEMY_HEIGHT   = 36;
    
    public static final int SCORE_BOSS     = 1000;
    public static final int[] STAGE_KILL_THRESHOLD = {7, 13, 26};

    // TODO: 스테이지/보스 등장 기준값 추가 (Week 2)
}