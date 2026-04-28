# Graphics Editor Project

## Project Overview
Java Swing 기반의 그래픽 편집기 애플리케이션.

## Build & Run
```bash
# 빌드
mvn compile

# 실행
mvn exec:java

# 테스트
mvn test
```

## Project Structure
```
src/main/java/org/graphicsEditor/
├── GMain.java          # 진입점 (main class)
├── GMainFrame.java     # 메인 윈도우 (JFrame, BorderLayout)
├── GMenuBar.java       # 메뉴바
├── GFileMenu.java      # File 메뉴
├── GShapeToolBar.java  # 도형 선택 툴바 (select, rectangle, oval)
└── GDrawingPanel.java  # 드로잉 캔버스 (마우스 이벤트, BufferedImage 더블버퍼링)
    ├── EDrawingState   # 상태 enum (eIdle, eDrawing, eMoving, eResizing, eShearing)
    ├── GShape          # 도형 내부 클래스 (현재 사각형만 구현)
    └── MouseHandler    # 마우스 이벤트 핸들러
```

## Architecture
- `GMainFrame`이 모든 컴포넌트를 소유 (aggregation hierarchy)
- `GDrawingPanel`은 BufferedImage로 더블 버퍼링
- 마우스 드래그로 사각형 그리기 (mousePressed → mouseDragged → mouseReleased)
- `shapes` Vector에 완성된 도형 누적 저장

## Naming Conventions
- 클래스명: `G` 접두사 (GMain, GShape, GDrawingPanel...)
- Enum 값: `e` 접두사 (eIdle, eDrawing...)
- 이벤트 핸들러: `Handler` 접미사 (MouseHandler, TooButtonActionHandler...)
- 필드 접근 시 `this.` 명시

## Tech Stack
- Java 25
- Maven
- Swing (javax.swing, java.awt)
- JUnit Jupiter 5.11.4 (테스트)

## Key Implementation Notes
- 도형 그리기: `startRectangularShape` → `finishRectangularShape` → `addShape` 순서
- `paintComponent`에서 bufferImage를 캔버스에 그림
- 툴바의 라디오 버튼과 드로잉 패널 간 연결은 아직 미구현
- `GShape`는 현재 `GDrawingPanel`의 inner class
