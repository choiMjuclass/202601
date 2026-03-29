package org.graphicsEditor;

import javax.swing.*;

public class GMenuBar extends JMenuBar {
	// components
	private GFileMenu fileMenu;
	// associations

	public GMenuBar() {
		this.fileMenu = new GFileMenu();
		this.add(this.fileMenu);
	}
}
