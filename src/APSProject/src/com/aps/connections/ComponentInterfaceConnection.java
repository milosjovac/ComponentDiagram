package com.aps.connections;

import java.util.Vector;

import com.aps.figures.ComponentFigure;
import com.aps.figures.InterfaceEmptyFigure;
import com.aps.figures.InterfaceFigure;
import com.aps.figures.StereotipDecorator;
import com.aps.figures.SymbolDecorator;

import CH.ifa.draw.figures.LineConnection;
import CH.ifa.draw.figures.PolyLineFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.DecoratorFigure;
import CH.ifa.draw.standard.NullHandle;

public class ComponentInterfaceConnection extends LineConnection {
	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -7959500008698525009L;

	public ComponentInterfaceConnection() {
		// setEndDecoration(new ArrowTip());
		setEndDecoration(null);
		setStartDecoration(null);
	}

	public void handleConnect(Figure start, Figure end) {
		InterfaceFigure interfejs;
		ComponentFigure komponenta;

		if (start instanceof ComponentFigure) {
			komponenta = (ComponentFigure) start;
			interfejs = (InterfaceFigure) end;

		} else {
			komponenta = (ComponentFigure) end;
			interfejs = (InterfaceFigure) start;
		}

		// FUNKCIJA UNUTAR INTERFEJSA ZA SPAJANJe (OBSERVER!!!!!)

		if (!komponenta.interfejsi.contains(interfejs))
			komponenta.interfejsi.add(interfejs);

		interfejs.parentFigure = komponenta;
		interfejs.errorNotation = false;

		// dodao Nex u model object interfejsa dodao model object komponente
		if (interfejs.dbInterfejsModel.getKomponenta() != komponenta.dbKomponenta)
			interfejs.dbInterfejsModel.setKomponenta(komponenta.dbKomponenta);

		if (!komponenta.dbKomponenta.getInterfejsi().contains(interfejs.dbInterfejsModel))
			komponenta.dbKomponenta.getInterfejsi().add(interfejs.dbInterfejsModel);

		if (interfejs instanceof InterfaceEmptyFigure)
			((InterfaceEmptyFigure) interfejs).rotate();
	}

	public void handleDisconnect(Figure start, Figure end) {

		// sam zove disconnect kada se pokusa kreiranje veze koja c
		if (start == null || end == null)
			return;

		InterfaceFigure interfejs;
		ComponentFigure komponenta;

		if (start instanceof ComponentFigure) {
			komponenta = (ComponentFigure) start;
			interfejs = (InterfaceFigure) end;

		} else {
			komponenta = (ComponentFigure) end;
			interfejs = (InterfaceFigure) start;
		}

		komponenta.dbKomponenta.getInterfejsi().remove(interfejs.dbInterfejsModel);
		interfejs.dbInterfejsModel.setKomponenta(null);

		komponenta.interfejsi.remove(interfejs);
		interfejs.parentFigure = null;
		interfejs.errorNotation = true;

	}

	// MOGUCI KVAR ZA UPDATE FUNKCIJU
	public boolean canConnect(Figure start, Figure end) {

		InterfaceFigure interfejs = null;
		ComponentFigure komponenta = null;

		Figure tempStart = start;
		Figure tempEnd = end;

		if ((tempStart instanceof ComponentFigure || tempStart instanceof DecoratorFigure)
				&& end instanceof InterfaceFigure) {

			while (tempStart instanceof DecoratorFigure) {
				if (tempStart instanceof StereotipDecorator)
					tempStart = ((StereotipDecorator) tempStart).getComponent();
				if (tempStart instanceof SymbolDecorator)
					tempStart = ((SymbolDecorator) tempStart).getComponent();
			}

			komponenta = (ComponentFigure) tempStart;
			interfejs = (InterfaceFigure) end;

		} else if (tempStart instanceof InterfaceFigure
				&& (tempEnd instanceof ComponentFigure || tempEnd instanceof DecoratorFigure)) {
			while (tempEnd instanceof DecoratorFigure) {
				if (tempEnd instanceof StereotipDecorator)
					tempEnd = ((StereotipDecorator) tempEnd).getComponent();
				if (tempEnd instanceof SymbolDecorator)
					tempEnd = ((SymbolDecorator) tempEnd).getComponent();
			}
			komponenta = (ComponentFigure) tempEnd;
			interfejs = (InterfaceFigure) start;
		}

		if (interfejs == null || komponenta == null)
			return false;

		// ako veze interfejsa pripadaju roditelju na koji pokusavamo da se nalepimo, imamo problem
		boolean sameParent = false;
		for (InterfaceFigure f : interfejs.connectedInterfaces)
			if (f.parentFigure.equals(komponenta)) {
				sameParent = true;
				break;
			}

		return !sameParent;
	}

	public Vector<NullHandle> handles() {
		Vector<NullHandle> handles = super.handles();
		// don't allow to reconnect the starting figure
		handles.setElementAt(new NullHandle(this, PolyLineFigure.locator(0)), 0);
		return handles;
	}
}