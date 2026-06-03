# 3. 기능 구현 및 단계별 구현 내용

> **프로젝트:** PILOT - 2D 횡스크롤 슈팅 게임  
> **기술 스택:** Java 17+, javax.swing  
> **개발 기간:** 4주차~7주차  
> **문서 버전:** v1.0

---

## 1. 구현 개요

PILOT 게임의 핵심 기능을 4개 주차에 걸쳐 단계별로 구현합니다.

- **4주차**: 기반 구조 완성 (플레이어, 루프, UI 기초)
- **5주차**: 게임플레이 로직 (적, 충돌, 점수)
- **6주차**: 통합 및 최적화 (모든 모듈 연동)
- **7주차**: 최종 마무리 (버그 수정, JAR 배포)

---

## 2. 패키지별 구현 로드맵

### 2.1 pilot.util 패키지 (공통 유틸리티)

#### Constants.java
**목적**: 전체 게임에서 사용하는 전역 상수 정의

**구현 사항**:
```java
// 화면 크기
public static final int WINDOW_WIDTH = 800;
public static final int WINDOW_HEIGHT = 600;
public static final int FPS = 60;
public static final int FRAME_TIME = 1000 / FPS; // 16ms

// 플레이어
public static final int PLAYER_WIDTH = 40;
public static final int PLAYER_HEIGHT = 40;
public static final int PLAYER_SPEED = 5;
public static final int PLAYER_FIRE_COOLDOWN = 200; // ms

// 적
public static final int ENEMY_WIDTH = 30;
public static final int ENEMY_HEIGHT = 30;
public static final int BASIC_ENEMY_SPEED = 2;
public static final int FAST_ENEMY_SPEED = 3;

// 총알
public static final int BULLET_WIDTH = 10;
public static final int BULLET_HEIGHT = 10;
public static final int BULLET_SPEED = 7;

// 무적 시간
public static final int INVINCIBILITY_TIME = 1500; // ms
```

**완료 기준**:
- [ ] 모든 수치 상수 정의 완료
- [ ] 팀원 A/B 모두 검토 및 동의

---

#### ResourceLoader.java
**목적**: 이미지 및 사운드 리소스를 한 번만 로드 후 캐싱

**구현 사항**:
```java
public class ResourceLoader {
    private static ResourceLoader instance;
    private Map<String, BufferedImage> imageCache = new HashMap<>();
    private Map<String, Clip> soundCache = new HashMap<>();
    
    public static ResourceLoader getInstance() { /* 싱글톤 */ }
    
    public BufferedImage getImage(String key) { 
        // HashMap에서 조회, 없으면 로드
    }
    
    public Clip getSound(String key) {
        // 사운드 클립 조회 또는 로드
    }
    
    private void preloadImages() { /* 최초 시작 시 모든 이미지 로드 */ }
}
```

**완료 기준**:
- [ ] 이미지 리소스 폴더 내 모든 파일 캐싱
- [ ] NullPointerException 처리
- [ ] 로딩 완료 후 메시지 출력

---

#### FileIO.java
**목적**: 점수 및 하이스코어를 파일에 저장/불러오기

**구현 사항**:
```java
public class FileIO {
    private static final String SCORE_FILE = "scores.dat";
    
    public static void saveScore(List<Score> scores) {
        // ObjectOutputStream으로 scores.dat에 저장
    }
    
    public static List<Score> loadScore() {
        // scores.dat 읽기, 없으면 기본값 반환
    }
}

class Score implements Serializable {
    String name;
    int score;
    LocalDateTime date;
}
```

**완료 기준**:
- [ ] scores.dat 파일 생성 및 수정 동작
- [ ] 5개까지 하이스코어 저장
- [ ] 파일 없을 시 자동 생성

---

### 2.2 pilot.entity 패키지 (게임 객체)

#### Entity.java (abstract)
**목적**: 모든 게임 오브젝트의 공통 속성 정의

**구현 사항**:
```java
public abstract class Entity {
    protected float x, y;
    protected int width, height;
    protected boolean isActive;
    
    public abstract void update();
    public abstract void draw(Graphics2D g);
    
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}
```

---

#### Player.java
**목적**: 플레이어 이동, 발사, 피격 처리

**주요 기능**:
- 8방향 이동 (방향키)
- Space 키로 총알 발사 (쿨다운)
- 적 총알 및 적과 충돌 시 체력 감소
- 피격 후 무적 시간 (깜빡임)
- 파워업 획득 시 공격력 강화

**구현 포인트**:
```java
private int hp = 3;
private long lastFireTime = 0;
private boolean invincible = false;
private long invincibleEndTime = 0;

public void update() {
    // 1. 키 입력에 따른 이동
    handleInput();
    
    // 2. 화면 경계 제한
    clampToBounds();
    
    // 3. 무적 시간 체크
    if (System.currentTimeMillis() > invincibleEndTime) {
        invincible = false;
    }
}

public void takeDamage(int damage) {
    if (!invincible) {
        hp -= damage;
        invincible = true;
        invincibleEndTime = System.currentTimeMillis() + Constants.INVINCIBILITY_TIME;
    }
}

public void fire() {
    long now = System.currentTimeMillis();
    if (now - lastFireTime > Constants.PLAYER_FIRE_COOLDOWN) {
        GameManager.getInstance().addBullet(new Bullet(this.x, this.y, ...));
        lastFireTime = now;
    }
}
```

**완료 기준**:
- [ ] 8방향 이동 정상 작동
- [ ] 화면 경계 내 이동만 가능
- [ ] Space 키 발사 쿨다운 동작
- [ ] 체력 0 이하 시 게임오버

---

#### Bullet.java
**목적**: 플레이어 및 적 총알 이동 로직

**구현 사항**:
```java
public class Bullet extends Entity {
    private int speedX, speedY;
    private int damage;
    private boolean isPlayerBullet;
    
    public void update() {
        x += speedX;
        y += speedY;
        
        // 화면 밖으로 나가면 비활성화
        if (x < 0 || x > Constants.WINDOW_WIDTH || 
            y < 0 || y > Constants.WINDOW_HEIGHT) {
            isActive = false;
        }
    }
}
```

**완료 기준**:
- [ ] 총알이 일직선으로 이동
- [ ] 화면 밖 나가면 자동 제거
- [ ] 플레이어/적 구분 정상 작동

---

#### Enemy 계층

##### Enemy.java (abstract)
```java
public abstract class Enemy extends Entity {
    protected int hp;
    protected int speed;
    protected long lastFireTime;
    
    public abstract void updateMovement(); // 각 적 유형별 패턴
    public abstract void fire(); // 총알 발사 패턴
}
```

##### BasicEnemy.java
- **이동**: 일직선 좌측
- **공격**: 일정 간격으로 정면 단발

##### FastEnemy.java
- **이동**: 사인 곡선 패턴
- **공격**: 빠른 2연사

##### ArmoredEnemy.java
- **이동**: 느린 직선
- **공격**: 없음 (몸통 박치기만)

##### Boss.java
- **이동**: 상하 움직임 + 패이즈 전환
- **공격**: 다방향 탄막, 레이저 (페이즈 2)

---

#### Item 계층

##### Item.java (abstract)
```java
public abstract class Item extends Entity {
    public abstract void applyEffect(Player player);
}
```

##### PowerUpItem.java
- 획득 시 플레이어 공격력 증가 (발사 쿨다운 감소)

##### LifeItem.java
- 획득 시 플레이어 체력 회복

---

### 2.3 pilot.manager 패키지 (게임 로직)

#### GameManager.java
**목적**: 전체 게임 상태 및 엔티티 관리

**싱글톤 구조**:
```java
public class GameManager {
    private static GameManager instance;
    
    private Player player;
    private List<Bullet> bullets;
    private List<Enemy> enemies;
    private List<Item> items;
    private GameState currentState; // MENU, PLAYING, PAUSED, GAME_OVER
    
    public static GameManager getInstance() { /* 싱글톤 */ }
    
    public void addBullet(Bullet bullet) { /* ... */ }
    public void removeEnemy(Enemy enemy) { /* ... */ }
    public void update() { /* 모든 엔티티 업데이트 호출 */ }
}
```

**완료 기준**:
- [ ] 싱글톤 패턴 정상 작동
- [ ] 엔티티 추가/제거 동작
- [ ] 상태 전환 정상 작동

---

#### StageManager.java
**목적**: 스테이지 및 웨이브 제어

**구현 사항**:
```java
public class StageManager {
    private int currentStage = 1;
    private int currentWave = 0;
    private Queue<Wave> waveQueue;
    
    public void startStage(int stageNum) {
        // 스테이지별 웨이브 설정
        loadWaves(stageNum);
    }
    
    public void update() {
        if (currentWave완료) {
            nextWave();
        }
        
        if (allWavesComplete && noBossSpawned) {
            spawnBoss();
        }
    }
}
```

**스테이지 구성**:
- **Stage 1**: BasicEnemy 10마리 × 2웨이브
- **Stage 2**: BasicEnemy + FastEnemy × 3웨이브 + Boss
- **Stage 3**: 모든 유형 × 4웨이브 + Boss 2 (페이즈 2)

**완료 기준**:
- [ ] 웨이브별 적 정확한 스폰
- [ ] 웨이브 클리어 판정 정상
- [ ] Boss 등장 타이밍 정확

---

#### CollisionManager.java
**목적**: AABB 충돌 감지

**주요 충돌 종류**:
1. Player Bullet × Enemy → 적 체력 감소, 점수 +100
2. Enemy Bullet × Player → 플레이어 피격 처리
3. Enemy Body × Player → 플레이어 피격 + 적 파괴
4. Item × Player → 아이템 효과 적용

**구현**:
```java
public class CollisionManager {
    public static void checkAll() {
        checkPlayerBulletEnemyCollision();
        checkEnemyBulletPlayerCollision();
        checkEnemyPlayerCollision();
        checkItemPlayerCollision();
    }
    
    private static boolean isColliding(Rectangle a, Rectangle b) {
        return a.intersects(b);
    }
}
```

**완료 기준**:
- [ ] 플레이어 총알 충돌 정상
- [ ] 적 총알 충돌 정상
- [ ] 적 몸체 충돌 정상
- [ ] 아이템 획득 정상

---

#### ScoreManager.java
**목적**: 점수 및 하이스코어 관리

**구현**:
```java
public class ScoreManager {
    private static ScoreManager instance;
    private int currentScore = 0;
    private int lives = 3;
    private List<Score> highScores;
    
    public void addScore(int points) { currentScore += points; }
    
    public void saveHighScore(String playerName) {
        // FileIO를 통해 저장
        FileIO.saveScore(highScores);
    }
    
    public List<Score> getHighScores() {
        return FileIO.loadScore();
    }
}
```

**점수 규칙**:
- BasicEnemy 격파: +100
- FastEnemy 격파: +150
- ArmoredEnemy 격파: +200
- Boss 격파: +1000
- Stage 클리어: +500

**완료 기준**:
- [ ] 점수 정확한 누적
- [ ] 하이스코어 저장/불러오기
- [ ] 목숨 감소 정상

---

#### SoundManager.java
**목적**: BGM 및 효과음 재생

**구현**:
```java
public class SoundManager {
    private static SoundManager instance;
    
    public void playBGM(String trackName) { /* 루프 재생 */ }
    public void stopBGM() { /* ... */ }
    public void playSFX(String effectName) { /* 1회 재생 */ }
}
```

**사운드 종류**:
- BGM: main_bgm, stage1_bgm, stage2_bgm, boss_bgm
- SFX: fire, hit, enemy_hit, powerup, gameover

**완료 기준**:
- [ ] BGM 루프 재생
- [ ] SFX 1회 재생
- [ ] 소리 끊김 없음

---

### 2.4 pilot.screen 패키지 (화면)

#### Screen.java (interface)
```java
public interface Screen {
    void handleInput(KeyEvent e);
    void update();
    void draw(Graphics2D g);
}
```

#### MainMenuScreen.java
- 메뉴 버튼: 게임 시작, 하이스코어, 종료
- 배경 렌더링

#### StageSelectScreen.java
- Stage 1, 2, 3 선택 화면
- 선택 시 GameScreen으로 전환

#### GameScreen.java
- 게임 메인 화면 (배경 + 엔티티 + HUD)
- ESC 키로 Pause 화면으로

#### PauseScreen.java
- 재개, 재시작, 메인 메뉴 선택

#### GameOverScreen.java
- 하이스코어 입력
- 재시작, 메인 메뉴 선택

---

### 2.5 pilot.ui 패키지 (HUD)

#### HUD.java
**표시 내용**:
- 플레이어 체력
- 현재 점수
- 목숨 개수
- 현재 스테이지/웨이브

**렌더링 위치**:
- 좌상단: 체력 + 목숨
- 우상단: 점수
- 하단: 스테이지 정보

**완료 기준**:
- [ ] 모든 정보 정확히 표시
- [ ] 글자 명확함 (폰트 크기 적절)
- [ ] 실시간 업데이트

---

## 3. 단계별 구현 순서

### Step 1: Constants.java (4주차 1일)
- 게임 전역 상수 정의

### Step 2-3: ResourceLoader.java + FileIO.java (4주차 1-2일)
- 리소스 캐싱 및 파일 I/O

### Step 4-5: Entity.java + Player.java (4주차 2-3일)
- 플레이어 기본 이동 및 발사

### Step 6: Bullet.java (4주차 3일)
- 총알 이동 로직

### Step 7: GamePanel.java + GameWindow.java (4주차 4-5일)
- 60FPS 루프 및 창 설정

### Step 8: 기본 화면 (MainMenuScreen, GameScreen) (4주차 말)
- UI 기초 렌더링

### Step 9: BasicEnemy + FastEnemy (5주차 초)
- 적 AI 및 이동 패턴

### Step 10: CollisionManager (5주차 중)
- 충돌 감지 완성

### Step 11: StageManager + ScoreManager (5주차 말)
- 웨이브 제어 및 점수

### Step 12: Boss.java + Item (6주차 초)
- 보스 및 파워업 아이템

### Step 13-14: 통합 및 최적화 (6주차)
- 모든 모듈 연결 및 버그 수정

### Step 15: 최종 JAR 배포 (7주차)
- 실행 JAR 생성 및 검증

---

## 4. 인터페이스 계약 (팀원 간 연동)

### GameManager 공개 API
```java
GameManager.getInstance().getPlayer() → Player
GameManager.getInstance().getEnemies() → List<Enemy>
GameManager.getInstance().getBullets() → List<Bullet>
GameManager.getInstance().getScore() → int
GameManager.getInstance().getLives() → int
GameManager.getInstance().getCurrentStage() → int
GameManager.getInstance().getCurrentState() → GameState
```

### ScoreManager 공개 API
```java
ScoreManager.getInstance().getScore() → int
ScoreManager.getInstance().getLives() → int
ScoreManager.getInstance().getHighScores() → List<Score>
ScoreManager.getInstance().addScore(int) → void
```

### SoundManager 공개 API
```java
SoundManager.getInstance().playBGM(String)
SoundManager.getInstance().stopBGM()
SoundManager.getInstance().playSFX(String)
```

---

## 5. 테스트 체크리스트

### M1 점검 (4주차 말)
- [ ] `java -jar PILOT.jar` 실행 시 메인메뉴 출력
- [ ] 방향키로 플레이어 이동 (경계 제한)
- [ ] Space 키로 총알 발사 (쿨다운)
- [ ] 60FPS 안정적 동작
- [ ] 리소스 로딩 정상

### M2 점검 (5주차 말)
- [ ] 적 스폰 및 이동 패턴 동작
- [ ] 충돌 감지 정확
- [ ] 점수 누적 정상
- [ ] 웨이브 클리어 판정 정상
- [ ] 배경 무한 스크롤
- [ ] 사운드 재생 정상

### M3 점검 (6주차 말)
- [ ] Stage 1 전 플레이 가능
- [ ] Boss 격파 가능
- [ ] 하이스코어 저장/불러오기
- [ ] JAR 단독 실행 (외부 의존성 없음)
- [ ] 모든 화면 전환 정상
- [ ] 5분 이상 플레이 가능

### M4 최종 (7주차)
- [ ] 버그 없음
- [ ] 모든 기능 정상 작동
- [ ] 문서 완비
- [ ] JAR 최종 배포

---

## 6. 주의 사항

### 성능 최적화
- 더블 버퍼링으로 화면 깜빡임 방지
- 사용하지 않는 엔티티는 즉시 제거
- 리소스 캐싱으로 반복 로드 방지

### 동기화 문제
- 게임 루프 중 리스트 수정 시 ConcurrentModificationException 주의
- Iterator 또는 별도 제거 리스트 사용

### 좌표계
- 좌상단 (0,0) 기준
- x: 좌→우 증가
- y: 상→하 증가

---

## 7. 참고 자료

- **프로젝트 아키텍처**: 상위 디렉토리 `2_프로젝트구조및역할분담/README.md` 참조
- **요구사항**: 상위 디렉토리 `1_요구사항분석및설계/README.md` 참조
- **테스트 가이드**: `4_통합테스트및최적화/README.md` 참조

---

*최종 작성: 3주차 | 갱신 예정: 각 마일스톤 완료 후*
