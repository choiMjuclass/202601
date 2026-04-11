package org.graphicsEditor;

import javax.swing.*;

public class GShapeToolBar extends JToolBar {
	public GShapeToolBar() {
		JRadioButton selectButton = new JRadioButton("select");
		this.add(selectButton);

		JRadioButton rectangleButton = new JRadioButton("rectangle");
		this.add(rectangleButton);

		JRadioButton ovalButton = new JRadioButton("oval");
		this.add(ovalButton);
	}
}
