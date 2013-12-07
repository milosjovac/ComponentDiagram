package com.aps.figures;

import java.awt.Color;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.ActionTool;

public class SymbolTool extends ActionTool {

	public SymbolTool(DrawingView view) {
		super(view);
	}

	@Override
	public void action(Figure figure) {

		if (figure instanceof SymbolDecorator) {

			Figure temp = ((SymbolDecorator) figure).peelDecoration();
			((ComponentFigure) figure).dekoracija = false;
			drawing().replace(figure, temp);

			if (temp instanceof ComponentFigure)
				((ComponentFigure) temp).errorNotation = true;
		}

		else if (figure instanceof ComponentFigure) {
			((ComponentFigure) figure).errorNotation = false;
			drawing().replace(figure, new SymbolDecorator(figure, Color.blue));

		}

		else if (figure instanceof StereotipDecorator) {

			Figure temp = ((StereotipDecorator) figure).peelDecoration();

			if (temp instanceof ComponentFigure) {
				drawing().replace(figure, new SymbolDecorator(figure, Color.blue));

			} else if (temp instanceof SymbolDecorator) {
				temp = ((SymbolDecorator) temp).peelDecoration();
				((ComponentFigure) figure).dekoracija = false;
				drawing().replace(figure, new StereotipDecorator(temp, Color.blue));
			}

		}

	}
}