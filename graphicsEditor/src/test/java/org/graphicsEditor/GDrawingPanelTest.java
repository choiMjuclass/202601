package org.graphicsEditor;

import org.graphicsEditor.frames.GDrawingPanel;
import org.graphicsEditor.shapes.GShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GDrawingPanel 테스트")
class GDrawingPanelTest {

    private GDrawingPanel panel;

    @BeforeEach
    void setUp() {
        panel = new GDrawingPanel();
        panel.setSize(600, 400); // BufferedImage 생성 조건 충족
    }

    // --- 리플렉션 헬퍼 ---
    @SuppressWarnings("unchecked")
    private Vector<GShape> getShapes() throws Exception {
        Field f = GDrawingPanel.class.getDeclaredField("shapes");
        f.setAccessible(true);
        return (Vector<GShape>) f.get(panel);
    }

    private Object getDrawingState() throws Exception {
        Field f = GDrawingPanel.class.getDeclaredField("eDrawingState");
        f.setAccessible(true);
        return f.get(panel);
    }

    private MouseEvent mouseEvent(int id, int x, int y, int button, int clickCount) {
        return new MouseEvent(panel, id, System.currentTimeMillis(),
                0, x, y, clickCount, false, button);
    }

    // --- 초기 상태 테스트 ---

    @Test
    @DisplayName("초기 배경색은 흰색이다")
    void testInitialBackground_isWhite() {
        assertEquals(Color.WHITE, panel.getBackground());
    }

    @Test
    @DisplayName("초기 도형 목록은 비어있다")
    void testInitialShapes_isEmpty() throws Exception {
        assertTrue(getShapes().isEmpty(), "초기 shapes 벡터는 비어있어야 한다");
    }

    @Test
    @DisplayName("초기 상태는 eIdle이다")
    void testInitialState_isIdle() throws Exception {
        assertEquals("eIdle", getDrawingState().toString());
    }

    // --- 마우스 이벤트 테스트 ---

    @Test
    @DisplayName("Press → Drag → Release 시 도형이 1개 추가된다")
    void testMouseDragCycle_addsOneShape() throws Exception {
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_PRESSED,  10, 10, MouseEvent.BUTTON1, 1));
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_DRAGGED,  50, 80, MouseEvent.BUTTON1, 0));
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_RELEASED, 50, 80, MouseEvent.BUTTON1, 1));

        assertEquals(1, getShapes().size(), "드래그 후 도형 1개가 추가되어야 한다");
    }

    @Test
    @DisplayName("Release 후 상태는 다시 eIdle로 돌아온다")
    void testAfterRelease_stateIsIdle() throws Exception {
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_PRESSED,  10, 10, MouseEvent.BUTTON1, 1));
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_DRAGGED,  50, 80, MouseEvent.BUTTON1, 0));
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_RELEASED, 50, 80, MouseEvent.BUTTON1, 1));

        assertEquals("eIdle", getDrawingState().toString());
    }

    @Test
    @DisplayName("여러 번 드래그하면 도형이 누적된다")
    void testMultipleDrags_accumulateShapes() throws Exception {
        for (int i = 0; i < 3; i++) {
            panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_PRESSED,  i * 10,     i * 10,     MouseEvent.BUTTON1, 1));
            panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_DRAGGED,  i * 10 + 5, i * 10 + 5, MouseEvent.BUTTON1, 0));
            panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_RELEASED, i * 10 + 5, i * 10 + 5, MouseEvent.BUTTON1, 1));
        }

        assertEquals(3, getShapes().size(), "드래그 3회 후 도형 3개가 있어야 한다");
    }

    @Test
    @DisplayName("Press만 하고 Release 없으면 도형이 추가되지 않는다")
    void testPressOnly_doesNotAddShape() throws Exception {
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_PRESSED, 10, 10, MouseEvent.BUTTON1, 1));

        assertEquals(0, getShapes().size(), "Release 없이는 도형이 추가되지 않아야 한다");
    }

    @Test
    @DisplayName("Press 후 상태는 eDrawing이다")
    void testAfterPress_stateIsDrawing() throws Exception {
        panel.dispatchEvent(mouseEvent(MouseEvent.MOUSE_PRESSED, 10, 10, MouseEvent.BUTTON1, 1));

        assertEquals("eDrawing", getDrawingState().toString());
    }
}
