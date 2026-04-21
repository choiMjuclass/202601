# Graphics Editor 프로젝트

Java/Swing 기반 벡터 그래픽 에디터. 마우스 드래그로 도형을 그리고 관리하는 애플리케이션.

## 빌드 및 실행

```bash
# 빌드
mvn clean package

# 실행
mvn exec:java

# 또는 bat 파일로 실행 (Windows)
run-graphicsEditor.bat
```

- Java 25, Maven 사용
- 메인 클래스: `org.graphicsEditor.GMain`
- 별도 외부 라이브러리 없음 (순수 Java Swing)

## 패키지 구조

```
src/main/java/org/graphicsEditor/
├── GMain.java          # 진입점 - GMainFrame 생성 및 표시
├── GMainFrame.java     # 최상위 JFrame - 레이아웃 구성
├── GMenuBar.java       # JMenuBar - GFileMenu 포함
├── GFileMenu.java      # 파일 메뉴
├── GShapeToolBar.java  # JToolBar - select/rectangle/oval 버튼
└── GDrawingPanel.java  # JPanel - 핵심 그리기 영역
```

## 아키텍처

- **컴포지션 계층**: GMain → GMainFrame → (GMenuBar, GShapeToolBar, GDrawingPanel)
- **레이아웃**: BorderLayout — 툴바(NORTH), 드로잉패널(CENTER)
- **상태 관리**: `EDrawingState` enum — eIdle / eDrawing / eMoving / eResizing / eShearing
- **더블 버퍼링**: `BufferedImage`를 사용한 수동 더블 버퍼링

## 핵심 클래스 설명

### GDrawingPanel
- `GShape` 내부 클래스로 도형 표현 (x0,y0 ~ x1,y1 좌표)
- `MouseHandler`가 MouseListener + MouseMotionListener 구현
- `shapes: Vector<GShape>`에 완성된 도형 누적 저장
- mousePressed → eDrawing 상태 진입, mouseReleased → eIdle 복귀

### GShape (GDrawingPanel 내부 클래스)
- 현재는 사각형(drawRect)만 구현
- `draw(Graphics2D g)` 메서드로 그리기

## 코드 컨벤션

- 클래스명: `G` 접두사 + PascalCase (예: `GDrawingPanel`)
- enum: `E` 접두사 + PascalCase (예: `EDrawingState`)
- enum 값: `e` 접두사 + PascalCase (예: `eIdle`)
- 이벤트 핸들러 클래스: `Handler` 접미사 (예: `MouseHandler`)
- 들여쓰기: 탭(tab) 사용

## 워크플로우 규칙

코드 변경 후 **항상** 아래 순서를 따를 것:
1. `mvn test` 실행 → 전체 통과 확인
2. 테스트 통과 시만 `git add` → `git commit` → `git push`
3. 테스트 실패 시 커밋하지 말 것

## 주의사항

- `GShape`는 현재 `GDrawingPanel`의 내부 클래스 — 별도 파일로 분리 시 패키지 접근 확인 필요
- `paintComponent`에서 `super.paintComponents(g)` (복수형) 호출 중 — 버그 가능성 있음
- `bufferImage`는 패널 크기 변경 시 재생성됨
