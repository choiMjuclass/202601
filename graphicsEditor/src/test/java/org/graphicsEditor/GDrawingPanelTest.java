package org.graphicsEditor;

import org.junit.jupiter.api.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GDrawingPanel 단위 테스트.
 *
 * headless 모드(-Djava.awt.headless=true)로 실행되므로
 * 실제 화면 없이 마우스 이벤트 디스패치로 상태·도형 목록을 검증한다.
 *
 * ★ 알려진 버그 (testDragCausesNPEDueToGetGraphics):
 *   finishRectangularShape() 내 this.getGraphics().getColor() 는
 *   컴포넌트가 화면에 표시되지 않으면 NullPointerException을 발생시킨다.
 */
class GDrawingPanelTest {

	private GDrawingPanel panel;

	@BeforeEach
	void setUp() {
		panel = new GDrawingPanel();
		// 패널 크기를 설정해야 startRectangularShape() 에서 bufferImage가 생성된다
		panel.setSize(400, 300);
	}

	// ── 헬퍼 ─────────────────────────────────────────────────────────────────

	@SuppressWarnings("unchecked")
	private Vector<GDrawingPanel.GShape> getShapes() throws Exception {
		Field f = GDrawingPanel.class.getDeclaredField("shapes");
		f.setAccessible(true);
		return (Vector<GDrawingPanel.GShape>) f.get(panel);
	}

	private String getDrawingStateName() throws Exception {
		Field f = GDrawingPanel.class.getDeclaredField("eDrawingState");
		f.setAccessible(true);
		return f.get(panel).toString();
	}

	/** 왼쪽 버튼 마우스 이벤트를 생성하는 팩토리 메서드 */
	private MouseEvent makeMouseEvent(int id, int x, int y) {
		return new MouseEvent(
			panel, id, System.currentTimeMillis(),
			InputEvent.BUTTON1_DOWN_MASK,
			x, y, 1, false, MouseEvent.BUTTON1
		);
	}

	/** Press → Release 순서로 디스패치 (drag 없음) */
	private void doPressRelease(int x0, int y0, int x1, int y1) {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED,  x0, y0));
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_RELEASED, x1, y1));
	}

	// ── 초기화 ───────────────────────────────────────────────────────────────

	@Test
	@DisplayName("초기화: 배경색이 흰색이다")
	void testInitialBackgroundIsWhite() {
		assertEquals(Color.WHITE, panel.getBackground());
	}

	@Test
	@DisplayName("초기화: 도형 목록이 비어 있다")
	void testInitialShapesListIsEmpty() throws Exception {
		assertTrue(getShapes().isEmpty());
	}

	@Test
	@DisplayName("초기화: 그리기 상태가 eIdle이다")
	void testInitialStateIsIdle() throws Exception {
		assertEquals("eIdle", getDrawingStateName());
	}

	// ── 마우스 Press ─────────────────────────────────────────────────────────

	@Test
	@DisplayName("mousePressed: 상태가 eDrawing으로 전환된다")
	void testMousePressChangesStateToDrawing() throws Exception {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED, 100, 100));
		assertEquals("eDrawing", getDrawingStateName());
	}

	@Test
	@DisplayName("mousePressed: 아직 도형이 추가되지 않는다")
	void testMousePressDoesNotAddShape() throws Exception {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED, 50, 50));
		assertEquals(0, getShapes().size());
	}

	@Test
	@DisplayName("mousePressed(eDrawing 상태): 중복 press는 상태를 바꾸지 않는다")
	void testSecondPressInDrawingStateIsIgnored() throws Exception {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED, 10, 10));
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED, 20, 20));
		assertEquals("eDrawing", getDrawingStateName());
	}

	// ── 마우스 Release (Press 없이) ───────────────────────────────────────────

	@Test
	@DisplayName("mouseReleased(eIdle 상태): 도형이 추가되지 않는다")
	void testReleaseWithoutPressAddsNoShape() throws Exception {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_RELEASED, 50, 50));
		assertEquals(0, getShapes().size());
		assertEquals("eIdle", getDrawingStateName());
	}

	// ── 마우스 Press → Release ────────────────────────────────────────────────

	@Test
	@DisplayName("Press→Release: 도형이 1개 추가된다")
	void testPressReleaseCycleAddsOneShape() throws Exception {
		doPressRelease(10, 10, 100, 100);
		assertEquals(1, getShapes().size());
	}

	@Test
	@DisplayName("Press→Release: 상태가 eIdle로 복귀한다")
	void testPressReleaseRestoresIdleState() throws Exception {
		doPressRelease(10, 10, 100, 100);
		assertEquals("eIdle", getDrawingStateName());
	}

	@Test
	@DisplayName("Press→Release x3: 도형이 3개 누적된다")
	void testThreePressReleaseCyclesAddThreeShapes() throws Exception {
		doPressRelease(0,   0,  50,  50);
		doPressRelease(60, 60, 120, 120);
		doPressRelease(130, 0, 200,  80);
		assertEquals(3, getShapes().size());
	}

	@Test
	@DisplayName("Press→Release: 동일 좌표(점) 도형도 정상 추가된다")
	void testPointShapeIsAdded() throws Exception {
		doPressRelease(50, 50, 50, 50);
		assertEquals(1, getShapes().size());
	}

	// ── 마우스 Drag (버그 문서화) ──────────────────────────────────────────────

	/**
	 * 알려진 버그: finishRectangularShape() 에서
	 * this.getGraphics().getColor() 를 호출하는데,
	 * 컴포넌트가 화면에 표시되지 않으면 getGraphics()가 null을 반환하여
	 * NullPointerException이 발생한다.
	 */
	@Test
	@DisplayName("[BUG] Drag 이벤트: getGraphics()가 null이어서 NPE 발생")
	void testDragCausesNPEDueToGetGraphics() {
		panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_PRESSED, 50, 50));

		assertThrows(NullPointerException.class, () ->
			panel.dispatchEvent(makeMouseEvent(MouseEvent.MOUSE_DRAGGED, 150, 120))
		);
	}
}
