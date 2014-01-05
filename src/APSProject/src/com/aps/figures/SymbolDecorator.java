package com.aps.figures;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.standard.DecoratorFigure;

public class SymbolDecorator extends DecoratorFigure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2708663497486797220L;
	public static final int XMARGIN = 2;
	public static final int YMARGIN = 2;
	private Color colour;

	public SymbolDecorator(Color newColour) {
		colour = newColour;
	}

	public SymbolDecorator(Figure figure, Color newColour) {
		super(figure);
		Figure tempFig = figure;
		while (tempFig instanceof DecoratorFigure) {
			if (tempFig instanceof StereotipDecorator)
				tempFig = ((StereotipDecorator) tempFig).getComponent();
			if (tempFig instanceof SymbolDecorator)
				tempFig = ((SymbolDecorator) tempFig).getComponent();
		}
		((ComponentFigure) tempFig).dekoracija = true;
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

		Rectangle figR = fComponent.displayBox();

		int x = figR.x + figR.width - 30;
		int y = figR.y + 10;

		g.drawRect(x, y, 20, 29);
		g.fillRect(x - 7, y + 5, 15, 7);
		g.fillRect(x - 7, y + 17, 15, 7);

	}

	public Figure getComponent() {
		return super.fComponent;
	}
}
