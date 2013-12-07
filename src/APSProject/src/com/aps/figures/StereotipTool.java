/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aps.figures;

import CH.ifa.draw.framework.DrawingView;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.ActionTool;
import java.awt.Color;

/**
 * 
 * @author Mima
 */
public class StereotipTool extends ActionTool {

	public StereotipTool(DrawingView view) {
		super(view);
	}

	@Override
	public void action(Figure figure) {

		if (figure instanceof StereotipDecorator) {
			Figure temp = ((StereotipDecorator) figure).peelDecoration();
			((ComponentFigure) figure).stereotip = false;
			drawing().replace(figure, temp);

			if (temp instanceof ComponentFigure)
				((ComponentFigure) temp).errorNotation = true;
		}

		else if (figure instanceof ComponentFigure) {
			((ComponentFigure) figure).errorNotation = false;
			drawing().replace(figure, new StereotipDecorator(figure, Color.blue));

		}

		else if (figure instanceof SymbolDecorator) {

			Figure temp = ((SymbolDecorator) figure).peelDecoration();

			if (temp instanceof ComponentFigure) {
				drawing().replace(figure, new StereotipDecorator(figure, Color.blue));

			} else if (temp instanceof StereotipDecorator) {
				temp = ((StereotipDecorator) temp).peelDecoration();
				((ComponentFigure) figure).stereotip = false;
				drawing().replace(figure, new SymbolDecorator(temp, Color.blue));
			}

		}

	}
}
