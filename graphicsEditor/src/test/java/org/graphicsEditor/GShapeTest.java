package org.graphicsEditor;

import org.junit.jupiter.api.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GDrawingPanel.GShape 내부 클래스 단위 테스트.
 * GShape 필드가 private이므로 리플렉션으로 좌표값 검증.
 */
class GShapeTest {

	private GDrawingPanel panel;
	private GDrawingPanel.GShape shape;

	@BeforeEach
	void setUp() {
		panel = new GDrawingPanel();
		shape = panel.new GShape(10, 20, 50, 80);
	}

	private int getIntField(Object obj, String fieldName) throws Exception {
		Field f = GDrawingPanel.GShape.class.getDeclaredField(fieldName);
		f.setAccessible(true);
		return (int) f.get(obj);
	}

	// ── 생성자 ───────────────────────────────────────────────────────────────

	@Test
	@DisplayName("생성자: 네 좌표가 올바르게 초기화된다")
	void testConstructorSetsCoordinates() throws Exception {
		assertEquals(10, getIntField(shape, "x0"));
		assertEquals(20, getIntField(shape, "y0"));
		assertEquals(50, getIntField(shape, "x1"));
		assertEquals(80, getIntField(shape, "y1"));
	}

	// ── setLocation0 ─────────────────────────────────────────────────────────

	@Test
	@DisplayName("setLocation0: 시작점(x0,y0)이 변경된다")
	void testSetLocation0UpdatesStartPoint() throws Exception {
		shape.setLocation0(5, 15);
		assertEquals(5,  getIntField(shape, "x0"));
		assertEquals(15, getIntField(shape, "y0"));
	}

	@Test
	@DisplayName("setLocation0: 종료점(x1,y1)은 변경되지 않는다")
	void testSetLocation0DoesNotChangeEndPoint() throws Exception {
		shape.setLocation0(5, 15);
		assertEquals(50, getIntField(shape, "x1"));
		assertEquals(80, getIntField(shape, "y1"));
	}

	// ── setLocation1 ─────────────────────────────────────────────────────────

	@Test
	@DisplayName("setLocation1: 종료점(x1,y1)이 변경된다")
	void testSetLocation1UpdatesEndPoint() throws Exception {
		shape.setLocation1(100, 200);
		assertEquals(100, getIntField(shape, "x1"));
		assertEquals(200, getIntField(shape, "y1"));
	}

	@Test
	@DisplayName("setLocation1: 시작점(x0,y0)은 변경되지 않는다")
	void testSetLocation1DoesNotChangeStartPoint() throws Exception {
		shape.setLocation1(100, 200);
		assertEquals(10, getIntField(shape, "x0"));
		assertEquals(20, getIntField(shape, "y0"));
	}

	// ── setSize ──────────────────────────────────────────────────────────────

	@Test
	@DisplayName("setSize: x1 = x0 + width, y1 = y0 + height 로 계산된다")
	void testSetSizeCalculatesEndPointFromOrigin() throws Exception {
		// x0=10, y0=20 → setSize(30,40) → x1=40, y1=60
		shape.setSize(30, 40);
		assertEquals(40, getIntField(shape, "x1"));
		assertEquals(60, getIntField(shape, "y1"));
	}

	@Test
	@DisplayName("setSize(0,0): 시작점과 종료점이 동일해진다")
	void testSetSizeZeroMakesPointShape() throws Exception {
		shape.setSize(0, 0);
		assertEquals(getIntField(shape, "x0"), getIntField(shape, "x1"));
		assertEquals(getIntField(shape, "y0"), getIntField(shape, "y1"));
	}

	// ── draw ─────────────────────────────────────────────────────────────────

	@Test
	@DisplayName("draw: 사각형 네 모서리 픽셀이 검정색으로 그려진다")
	void testDrawRendersRectangleOutline() {
		// GShape(10,20,50,80) → drawRect(10,20,40,60)
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 100, 100);
		shape.draw(g);
		g.dispose();

		int black = Color.BLACK.getRGB();
		assertEquals(black, img.getRGB(10, 20), "top-left corner");
		assertEquals(black, img.getRGB(50, 20), "top-right corner");
		assertEquals(black, img.getRGB(10, 80), "bottom-left corner");
		assertEquals(black, img.getRGB(50, 80), "bottom-right corner");
	}

	@Test
	@DisplayName("draw: 사각형 외부 픽셀은 검정색이 아니다")
	void testDrawDoesNotFillInterior() {
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 100, 100);
		shape.draw(g);
		g.dispose();

		// 도형 내부 중심 픽셀 (30,50)은 흰색이어야 한다
		assertNotEquals(Color.BLACK.getRGB(), img.getRGB(30, 50), "interior pixel");
		// 도형 바깥 픽셀 (5,5)
		assertNotEquals(Color.BLACK.getRGB(), img.getRGB(5, 5), "outside pixel");
	}

	@Test
	@DisplayName("draw: 너비/높이가 0인 도형도 예외 없이 그려진다")
	void testDrawZeroSizeShapeNoException() {
		GDrawingPanel.GShape zeroShape = panel.new GShape(10, 20, 10, 20);
		BufferedImage img = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 50, 50);
		assertDoesNotThrow(() -> zeroShape.draw(g));
		g.dispose();
	}

	@Test
	@DisplayName("draw: 음수 크기(뒤집힌) 도형도 예외 없이 그려진다")
	void testDrawInvertedShapeNoException() {
		// x0 > x1 → 너비가 음수 → drawRect에 음수 전달
		GDrawingPanel.GShape invertedShape = panel.new GShape(50, 80, 10, 20);
		BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 100, 100);
		assertDoesNotThrow(() -> invertedShape.draw(g));
		g.dispose();
	}
}
