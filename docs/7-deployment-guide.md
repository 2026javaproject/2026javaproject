# 7. 배포 및 실행 가이드

## 7.1 시스템 요구사항

| 항목 | 요구사항 |
|------|---------|
| **OS** | Windows 10/11, macOS, Linux |
| **Java** | 17.0.0 이상 |
| **메모리** | 최소 256MB (권장 512MB) |
| **디스크** | 50MB |
| **해상도** | 800×600 이상 |

---

## 7.2 설치 방법

### 1. Java 설치 확인
```bash
java -version
# 출력: java version "25.0.2" 이상
```

### 2. JAR 파일 준비
```
pilot-game.jar
```

### 3. 게임 실행
```bash
java -jar pilot-game.jar
```

---

## 7.3 메모리 조정

### 기본 실행
```bash
java -jar pilot-game.jar
```

### 메모리 증가 (문제 시)
```bash
java -Xmx512m -jar pilot-game.jar
```

---

## 7.4 문제 해결

| 증상 | 원인 | 해결 |
|------|------|------|
| "Java가 없습니다" | Java 미설치 | Java 설치 |
| 게임이 느림 | 메모리 부족 | `-Xmx512m` 옵션 추가 |
| 창이 열리지 않음 | 디스플레이 문제 | 해상도 확인 |

---

## 7.5 하이스코어 초기화

하이스코어 파일 위치:
```
Windows: C:\Users\[사용자명]\AppData\Local\
macOS: ~/Library/Application Support/
Linux: ~/.local/share/
```

파일명: `highscore.dat`

---

## 7.6 배포 환경

### 개발 환경
- IDE: IntelliJ IDEA / Eclipse
- Build: javac, jar
- JDK: Java 25.0.2

### 배포 환경
- 독립형 JAR 실행
- 외부 의존성: 없음

---

## 7.7 버전 정보

```
프로젝트: PILOT
버전: v1.0.0
릴리즈 날짜: 2026-06-03
Java: 25.0.2
Swing: Java SE 내장
```

