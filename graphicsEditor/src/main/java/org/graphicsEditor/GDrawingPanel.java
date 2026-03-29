package org.graphicsEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GDrawingPanel extends JPanel {
	// constructors
	public GDrawingPanel() {
		this.setBackground(Color.CYAN);

		MouseHandler mouseHandler = new MouseHandler();
		this.addMouseListener(mouseHandler);
		this.addMouseMotionListener(mouseHandler);
	}

	private class MouseHandler implements MouseListener, MouseMotionListener {

		@Override
		public void mouseClicked(MouseEvent e) {
		}
		private void mouse1Clicked(MouseEvent e) {
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		}
		private void mouse2Clicked(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {			
		}
		@Override
		public void mouseDragged(MouseEvent e) {
		}
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
		}
		@Override
		public void mouseExited(MouseEvent e) {
		}
	}
}
