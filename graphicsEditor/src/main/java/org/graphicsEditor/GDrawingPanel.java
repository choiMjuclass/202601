package org.graphicsEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {
	private enum EDrawingState {
		eIdle,
		eDrawing,
		eMoving,

		eResizing,
		eShearing
	}
	private EDrawingState eDrawingState;

	private BufferedImage bufferImage;
	private Vector<GShape> shapes;
	private GShape currentShape;

	// constructors
	public GDrawingPanel() {
		// attributes
		this.setBackground(Color.WHITE);
		this.eDrawingState = EDrawingState.eIdle;
		// components list
		this.shapes = new Vector<GShape>();

		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);

		if (g != null) {
			g.drawImage(this.bufferImage, 0, 0, null);
		}
	}

	private void startRectangularShape(int x, int y) {
		this.currentShape = new GShape(x, y, x, y);

		if (this.getWidth() <= 0 || this.getHeight() <= 0) {
			return;
		}

		if (this.bufferImage == null
				|| this.bufferImage.getWidth() != this.getWidth()
				|| this.bufferImage.getHeight() != this.getHeight()) {
			this.bufferImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics2D bufferGraphics = this.bufferImage.createGraphics();
			bufferGraphics.setColor(this.getBackground());
			bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			bufferGraphics.dispose();
		}
	}
	private void finishRectangularShape(int x, int y) {
		this.currentShape.setLocation1(x, y);

		Graphics2D bufferGraphics = this.bufferImage.createGraphics();
		bufferGraphics.setColor(this.getBackground());
		bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		bufferGraphics.setColor(this.getGraphics().getColor());
		for (GShape shape : this.shapes) {
			shape.draw(bufferGraphics);
		}
		this.currentShape.draw(bufferGraphics);
		bufferGraphics.dispose();

		repaint();
	}

	private void addShape() {
		this.shapes.add(this.currentShape);
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == 1) { // left button
				if (e.getClickCount() == 1) { // single click
					mouseLButton1Clocked(e);
				} else if (e.getClickCount() == 2) { // double click
					mouseLButton2Clocked(e);
				}
			}
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
		private void mouseLButton1Clocked(MouseEvent e) {
		}
		private void mouseLButton2Clocked(MouseEvent e) {

		}
		@Override
		public void mousePressed(MouseEvent e) {
			if (eDrawingState == EDrawingState.eIdle) {
				startRectangularShape(e.getX(), e.getY());
				eDrawingState = EDrawingState.eDrawing;
			}
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			if (eDrawingState == EDrawingState.eDrawing) {
				finishRectangularShape(e.getX(), e.getY());
			}
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			if (eDrawingState == EDrawingState.eDrawing) {
				addShape();
				eDrawingState = EDrawingState.eIdle;
			}
		}


		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}

	}

	public class GShape {
		private int x0, y0, x1, y1;

		public GShape(int x0, int y0, int x1, int y1) {
			this.x0 = x0;
			this.y0 = y0;
			this.x1 = x1;
			this.y1 = y1;
		}

		public void setLocation0(int x, int y) {
			this.x0 = x;
			this.y0 = y;
		}
		public void setLocation1(int x, int y) {
			this.x1 = x;
			this.y1 = y;
		}

		public void setSize(int width, int height) {
			this.x1 = x0 + width;
			this.y1 = y0 + height;
		}
		public void draw(Graphics2D g) {
			g.setColor(Color.BLACK);
			g.drawRect(x0, y0, x1 - x0, y1 - y0);
		}

	}
}
