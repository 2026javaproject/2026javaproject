package com.pilot.manager;

import com.pilot.util.GameConstants;

public class ScoreManager {

    private int score;
    private int killCount;
    private int stage;
    private boolean bossSpawnPending;

    public ScoreManager() { reset(); }

    public void reset() {
        score            = 0;
        killCount        = 0;
        stage            = 1;
        bossSpawnPending = false;
    }

    /** 적 처치 시 호출 */
    public void addKill(int points) {
        score += points;
        killCount++;
        checkStageThreshold();
    }

    private void checkStageThreshold() {
        int threshold = Math.min(39, 9 + stage);
        if (!bossSpawnPending && killCount >= threshold) {
            bossSpawnPending = true;
        }
    }

    /** 보스 처치 후 GamePanel에서 호출 */
    public void onBossDefeated(int bossScore) {
        score += bossScore;
        stage++;
        bossSpawnPending = false;
    }

    public int  getScore()              { return score; }
    public int  getKillCount()          { return killCount; }
    public int  getStage()              { return stage; }
    public boolean isBossSpawnPending() { return bossSpawnPending; }
    public void clearBossSpawn()        { bossSpawnPending = false; }
}