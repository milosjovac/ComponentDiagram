package com.aps.figures;

import java.awt.Color;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.ActionTool;
import CH.ifa.draw.standard.DecoratorFigure;

public class SymbolTool extends ActionTool {

	public SymbolTool(DrawingView view) {
		super(view);
	}

	@Override
	public void action(Figure figure) {

		if (figure instanceof DecoratorFigure) {
			boolean flagStereo = false;
			boolean flagSymbol = false;
			while (figure instanceof DecoratorFigure) {
				if (figure instanceof SymbolDecorator)
					flagSymbol = true;
				else if (figure instanceof StereotipDecorator)
					flagStereo = true;
				drawing().replace(figure, ((DecoratorFigure) figure).peelDecoration());
				figure = ((DecoratorFigure) figure).peelDecoration();
			}

			((ComponentFigure) figure).stereotip = flagStereo;
			((ComponentFigure) figure).dekoracija = !flagSymbol;
			((ComponentFigure) figure).errorNotation = !(!flagSymbol || flagStereo);

			if (!flagSymbol) {
				Figure newFigure = new SymbolDecorator(figure, Color.blue);
				drawing().replace(figure, newFigure);
				figure = newFigure;
			}
			if (flagStereo)
				drawing().replace(figure, new StereotipDecorator(figure, Color.blue));

		} else {
			((ComponentFigure) figure).errorNotation = false;
			((ComponentFigure) figure).dekoracija = true;
			drawing().replace(figure, new SymbolDecorator(figure, Color.blue));
		}
	}
}