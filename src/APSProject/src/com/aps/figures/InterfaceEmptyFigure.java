package com.aps.figures;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class InterfaceEmptyFigure extends InterfaceFigure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public int startAngle = 0;
	public int endAngle = 180;

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
		Rectangle dimenzija = krug.displayBox();

		// g.drawRect(dimenzija.x, dimenzija.y, dimenzija.width,
		// dimenzija.height);

		g.drawArc(dimenzija.x, dimenzija.y, dimenzija.width, dimenzija.height, startAngle, endAngle);
	}
	public void updateDBModel() {
		super.updateDBModel();
		dbInterfejsModel.setTip(false);
		
		// dodati komponentu kroz CompInt konektor hendler
		// dodati interfejse kroz IntOInt konek,...
	}
	
	@Override
	public void moveBy(int dx, int dy) {
		// TODO Auto-generated method stub
		super.moveBy(dx, dy);

		if (parentFigure != null) {

			rotate();
		} else {
			startAngle = 0;
		}
	}

	public void rotate() {
		double delta_x = fDisplayBox.x + fDisplayBox.width / 2
				- (parentFigure.displayBox().x + parentFigure.displayBox().width / 2);
		double delta_y = fDisplayBox.y + fDisplayBox.height / 2
				- (parentFigure.displayBox().y + parentFigure.displayBox().height / 2);

		double theta_radians = Math.atan2(delta_x, delta_y);
		int razlika = (int) (theta_radians * 180 / Math.PI);

		startAngle = razlika;

	}

}
