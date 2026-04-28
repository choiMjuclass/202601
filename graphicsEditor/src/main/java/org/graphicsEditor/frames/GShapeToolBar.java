package org.graphicsEditor.frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GShapeToolBar extends JToolBar {
	public enum EShapeType {
		eSelect,
		eRectangle,
		eOval,
		eLine,
		ePolygon
	};
	private EShapeType eShapeType;
	public EShapeType getShapeType() {
		return eShapeType;
	}

	public GShapeToolBar() {
		ActionHandler actionHandler = new ActionHandler();

		ButtonGroup group = new ButtonGroup();

		JRadioButton selectButton = new JRadioButton("select");
		this.add(selectButton);
		group.add(selectButton);
		selectButton.addActionListener(actionHandler);
		selectButton.setActionCommand(EShapeType.eSelect.toString());

		JRadioButton rectangleButton = new JRadioButton("rectangle");
		this.add(rectangleButton);
		group.add(rectangleButton);
		rectangleButton.addActionListener(actionHandler);
		rectangleButton.setActionCommand(EShapeType.eRectangle.toString());

		JRadioButton ovalButton = new JRadioButton("oval");
		this.add(ovalButton);
		group.add(ovalButton);
		ovalButton.addActionListener(actionHandler);
		ovalButton.setActionCommand(EShapeType.eOval.toString());

	}

	private class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// "eSelec" ==> eSelect
			eShapeType = EShapeType.valueOf(e.getActionCommand());
		}
	}

}
