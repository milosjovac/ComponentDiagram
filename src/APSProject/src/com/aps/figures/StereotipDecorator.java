/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aps.figures;

import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.standard.DecoratorFigure;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

/**
 * 
 * @author Mima
 */
public class StereotipDecorator extends DecoratorFigure {

	public static final int XMARGIN = 2;
	public static final int YMARGIN = 2;
	private Color colour;

	public StereotipDecorator(Color newColour) {
		colour = newColour;
	}

	public StereotipDecorator(Figure figure, Color newColour) {
        super(figure);
        while(figure instanceof DecoratorFigure){
        	figure =((DecoratorFigure) figure).peelDecoration();
        }
        ((ComponentFigure)figure).stereotip = true;
        colour = newColour;
    }

	@Override
	public Rectangle displayBox() {
		Rectangle r = fComponent.displayBox();
		r.grow(XMARGIN, YMARGIN);
		return r;
	}

	@Override
	public void figureInvalidated(FigureChangeEvent e) {
		Rectangle rect = e.getInvalidatedRectangle();
		rect.grow(XMARGIN, YMARGIN);
		super.figureInvalidated(new FigureChangeEvent(e.getFigure(), rect));
	}

	@Override
	public void draw(Graphics g) {

		super.draw(g);
		g.setColor(colour);

		Font fb = new Font("Helvetica", Font.BOLD, 17);
		TextFigure stereotip = new TextFigure();
		stereotip.setFont(fb);
		stereotip.setText("<<component>>");
		stereotip.setAttribute("TextColor", colour);
		Rectangle figR = fComponent.displayBox();
		stereotip.basicDisplayBox(new Point((int) (figR.x + (figR.width - stereotip.size().getWidth()) / 2),
				figR.y), null);
		stereotip.draw(g);
	}
}
