package org.graphicsEditor;

import org.graphicsEditor.frames.GDrawingPanel;
import org.graphicsEditor.shapes.GShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GShape 테스트")
class GShapeTest {

    private GShape shape;

    @BeforeEach
    void setUp() {
        shape = new GShape(10, 20, 50, 80);
    }

    // --- 좌표 접근 헬퍼 ---
    private int getField(String name) throws Exception {
        Field f = GShape.class.getDeclaredField(name);
        f.setAccessible(true);
        return (int) f.get(shape);
    }

    @Test
    @DisplayName("생성자: 좌표가 올바르게 설정된다")
    void testConstructor_setsCoordinates() throws Exception {
        assertEquals(10, getField("x0"));
        assertEquals(20, getField("y0"));
        assertEquals(50, getField("x1"));
        assertEquals(80, getField("y1"));
    }

    @Test
    @DisplayName("setLocation0: x0, y0가 업데이트된다")
    void testSetLocation0_updatesOrigin() throws Exception {
        shape.setLocation0(5, 15);

        assertEquals(5, getField("x0"));
        assertEquals(15, getField("y0"));
    }

    @Test
    @DisplayName("setLocation1: x1, y1이 업데이트된다")
    void testSetLocation1_updatesEnd() throws Exception {
        shape.setLocation1(100, 200);

        assertEquals(100, getField("x1"));
        assertEquals(200, getField("y1"));
    }

    @Test
    @DisplayName("setSize: x1 = x0 + width, y1 = y0 + height 로 계산된다")
    void testSetSize_calculatesEndFromOrigin() throws Exception {
        shape.setLocation0(10, 20);
        shape.setSize(40, 60);

        assertEquals(50, getField("x1")); // 10 + 40
        assertEquals(80, getField("y1")); // 20 + 60
    }

    @Test
    @DisplayName("setSize(0, 0): x1 == x0, y1 == y0 (크기 0)")
    void testSetSize_zero() throws Exception {
        shape.setLocation0(10, 20);
        shape.setSize(0, 0);

        assertEquals(10, getField("x1"));
        assertEquals(20, getField("y1"));
    }

    @Test
    @DisplayName("draw: 사각형 테두리가 검은색으로 그려진다")
    void testDraw_drawsBlackRectangle() {
        int width = 200, height = 200;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // (10, 20) → (50, 80) 사각형 그리기
        shape.draw(g);
        g.dispose();

        // 상단 모서리 픽셀 확인
        assertEquals(Color.BLACK.getRGB(), img.getRGB(10, 20), "상단 좌측 모서리는 검은색이어야 한다");
        assertEquals(Color.BLACK.getRGB(), img.getRGB(50, 20), "상단 우측 모서리는 검은색이어야 한다");
        assertEquals(Color.BLACK.getRGB(), img.getRGB(10, 80), "하단 좌측 모서리는 검은색이어야 한다");
    }

    @Test
    @DisplayName("draw: 내부 픽셀은 흰색(배경색)이다")
    void testDraw_interiorIsWhite() {
        int width = 200, height = 200;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        shape.draw(g); // (10,20) ~ (50,80)
        g.dispose();

        // 내부 픽셀 (30, 50)
        assertEquals(Color.WHITE.getRGB(), img.getRGB(30, 50), "사각형 내부는 흰색이어야 한다");
    }
}
