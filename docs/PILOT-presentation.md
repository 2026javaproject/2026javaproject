---
marp: true
theme: default
paginate: true
size: 16:9
header: "PILOT - Java Swing 2D 슈팅 게임"
footer: "강민석 · 임대훈"
lang: ko
---

<!-- _class: lead -->
<!-- _paginate: false -->

# 🎮 PILOT

## Java Swing 2D 횡스크롤 슈팅 게임

**발표자**: 강민석 (팀장 / 백엔드) · 임대훈 (UI / 그래픽)

---

## 📋 프로젝트 개요

| 항목 | 내용 |
|------|------|
| **장르** | 2D 횡스크롤 슈팅 게임 |
| **기술 스택** | Java 25.0.2 · Swing |
| **개발 인원** | 2명 |
| **핵심 목표** | 60FPS 게임 루프 기반 실시간 렌더링 |
| **규모** | 6개 패키지 · 19개 클래스 |

### 🎯 게임 특징
- ✈️ 플레이어 조종 · 💣 연속 발사
- 👾 3가지 타입 적 + 보스 적
- 💥 AABB 충돌 감지
- 🎁 파워업 시스템
- 💾 하이스코어 저장

---

## ⚙️ 게임 루프 (60FPS)

```
while (running) {
    ├─ 입력 처리 (키보드 이벤트)
    ├─ 상태 갱신 (플레이어, 적, 발사체 업데이트)
    ├─ 충돌 판정 (AABB 기반)
    ├─ 렌더링 (paintComponent)
    └─ 타이밍 제어 (약 16ms/frame)
}
```

**프레임 타이밍 관리:**
```java
long FRAME_TIME = 1_000_000_000L / 60;  // 16.67ms
while (running) {
    update();           // 상태 갱신
    repaint();          // 렌더링
    sleep(FRAME_TIME);  // 프레임 대기
}
```

---

## 🏗️ 패키지 구조 (1/2)

### **com.pilot.entity** - 게임 엔티티

**Player.java** - 플레이어 비행기
```java
public void update() {
    int currentSpeed = speedBuffTimer > 0 ? BASE_SPEED + 2 : BASE_SPEED;
    if (left)  x -= currentSpeed;
    if (right) x += currentSpeed;
    if (up)    y -= currentSpeed;
    if (down)  y += currentSpeed;
    
    // 화면 경계 제한
    x = Math.max(0, Math.min(x, SCREEN_WIDTH - PLAYER_WIDTH));
}

public void shoot() {
    // 무기 레벨에 따라 1~4발 발사
    for (int i = 0; i < weaponLevel; i++) {
        bullets.add(new Bullet(bx, y, true));
    }
}
```

**Enemy 타입**: EnemyTypeA (직진) · EnemyTypeB (웨이브) · EnemyTypeC (추적) · BossEnemy

---

## 🏗️ 패키지 구조 (2/2)

### **com.pilot.manager** - 게임 로직

**CollisionDetector.java** - AABB 충돌 판정
```java
public static void checkBulletEnemyCollision(List<Bullet> bullets, List<Enemy> enemies) {
    for (Bullet bullet : bullets) {
        for (Enemy enemy : enemies) {
            // 직사각형 교집합 판정
            if (bullet.getBounds().intersects(enemy.getBounds())) {
                enemy.takeDamage(bullet.getDamage());
                bullets.remove(bullet);
                break;
            }
        }
    }
}
```

### **com.pilot.screen** - UI 화면
- MainMenuPanel: 메인 메뉴
- GamePanel: 게임 플레이 루프
- GameOverPanel: 게임 종료 화면

### **com.pilot.util** - 유틸리티
- GameConstants: 전역 상수 (800×600, FPS=60)
- HighScoreFileIO: 파일 I/O

---

## 👥 역할 분담

| 이름 | 역할 | 담당 분야 |
|------|------|---------|
| **강민석** | 팀장 / 백엔드 | 게임 루프 · 엔티티 로직 · 충돌 감지 · 구조 설계 |
| **임대훈** | 프론트엔드 | UI 디자인 · 화면 렌더링 · 사용자 인터페이스 |

### 🎓 주요 학습 목표
- ✅ 60FPS 게임 루프 구현
- ✅ AABB 충돌 판정 알고리즘
- ✅ Swing GUI 프로그래밍
- ✅ 객체 지향 설계 패턴
- ✅ 상태 관리 및 전환

---

<!-- _class: lead -->

# 감사합니다!

질문 환영합니다 🙌

**📦 JAR 배포**: `pilot-game.jar`
**🚀 GitHub**: icandoitsir/2026javaproject
