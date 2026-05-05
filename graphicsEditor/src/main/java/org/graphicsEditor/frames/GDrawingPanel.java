package org.graphicsEditor.frames;

import org.graphicsEditor.global.GConstants;
import org.graphicsEditor.shapes.GRectangle;
import org.graphicsEditor.shapes.GShape;
import org.graphicsEditor.shapes.GOval;
import org.graphicsEditor.shapes.GShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class GDrawingPanel extends JPanel {

	private GShapeToolBar toolBar;
	public void associateWith(GShapeToolBar toolBar) {
		this.toolBar = toolBar;
	}

	private enum EDrawingState {
		eIdle,
		eDrawing,
		eMoving,
		eResizing,
		eRotating,
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
		if (this.eDrawingState == EDrawingState.eIdle) {
			if (this.toolBar.getShapeType() == GConstants.EShapeType.eSelect) {
				for (GShape shape : this.shapes) {
					GShape.EAnchor eAnchor = shape.onShape(x, y);
					if (eAnchor != null) {
						if (eAnchor == GShape.EAnchor.eRotate) {
							eDrawingState = EDrawingState.eRotating;
						} else if (eAnchor == GShape.EAnchor.eMove) {
							eDrawingState = EDrawingState.eMoving;
						} else { // resize
							eDrawingState = EDrawingState.eResizing;
						}
						this.currentShape = shape;
						break;
					}
				}
			} else { // drawing
				if (this.toolBar.getShapeType() == GConstants.EShapeType.eOval) {
					this.currentShape = new GOval(x, y, x, y);
				} else if (this.toolBar.getShapeType() == GConstants.EShapeType.eRectangle) {
					this.currentShape = new GRectangle(x, y, x, y);
				}
				eDrawingState = EDrawingState.eDrawing;
			}

			if (this.getWidth() <= 0 || this.getHeight() <= 0 || this.eDrawingState == EDrawingState.eIdle) {
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
	}
	private void keepRectangularShape(int x, int y) {
		if (this.eDrawingState != EDrawingState.eIdle) {
			Graphics2D bufferGraphics = this.bufferImage.createGraphics();
			bufferGraphics.setColor(this.getBackground());
			bufferGraphics.fillRect(0, 0, this.getWidth(), this.getHeight());
			bufferGraphics.setColor(this.getForeground());

			if (this.eDrawingState == EDrawingState.eDrawing) {
				this.currentShape.setLocation1(x, y);
				this.currentShape.draw(bufferGraphics);
			} else if (this.eDrawingState == EDrawingState.eMoving) {
				this.currentShape.move(x, y);
			} else if (this.eDrawingState == EDrawingState.eResizing) {
				this.currentShape.resize(x, y);
			} else if (this.eDrawingState == EDrawingState.eRotating) {
				this.currentShape.rotate(x, y);
			}
			for (GShape shape : this.shapes) {
				shape.draw(bufferGraphics);
			}
			bufferGraphics.dispose();
			repaint();
		}
	}

	private void finishRectangularShape(int x, int y) {
		if (this.eDrawingState != EDrawingState.eIdle) {
			if (this.eDrawingState == EDrawingState.eDrawing) {
				this.shapes.add(this.currentShape);
			}
			this.eDrawingState = EDrawingState.eIdle;
			this.currentShape = null;
		}
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
			startRectangularShape(e.getX(), e.getY());
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			keepRectangularShape(e.getX(), e.getY());
		}
		@Override
		public void mouseReleased(MouseEvent e) {
			finishRectangularShape(e.getX(), e.getY());
		}


		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}

	}
}
