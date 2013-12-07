package com.aps.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class InterfaceFullFigure extends InterfaceFigure {

	private static final long serialVersionUID = 1L;

	public void draw(Graphics g) {

		if (errorNotation) {
			name.setAttribute("TextColor", Color.red);
			krug.setAttribute("FillColor", Color.red);
			g.setColor(Color.red);
		} else {
			name.setAttribute("TextColor", Color.black);
			krug.setAttribute("FillColor", Color.blue);
			g.setColor(Color.black);
		}
		super.draw(g);
		Rectangle r = displayBox();
		// g.drawRect(r.x, r.y, r.width, r.height);
		krug.draw(g);
	}

	public void updateDBModel() {
		super.updateDBModel();
		dbInterfejsModel.setTip(true);

		// dodati komponentu kroz CompInt konektor hendler
		// dodati interfejse kroz IntOInt konek,...
	}

}
