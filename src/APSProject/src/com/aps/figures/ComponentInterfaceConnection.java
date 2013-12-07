package com.aps.figures;

import java.util.Vector;

import com.aps.dmo.Interfejs;

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

		komponenta.interfejsi.add(interfejs);
		interfejs.parentFigure = komponenta;
		interfejs.errorNotation = false;

		// dodao Nex u model object interfejsa dodao model object komponente
		interfejs.dbInterfejsModel.setKomponenta(komponenta.dbKomponenta);
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

	public boolean canConnect(Figure start, Figure end) {

		InterfaceFigure interfejs = null;
		ComponentFigure komponenta = null;

		if ((start instanceof ComponentFigure || start instanceof DecoratorFigure)
				&& end instanceof InterfaceFigure) {
			while (start instanceof DecoratorFigure)
				start = ((DecoratorFigure) start).peelDecoration();

			komponenta = (ComponentFigure) start;
			interfejs = (InterfaceFigure) end;

		} else if (start instanceof InterfaceFigure
				&& (end instanceof ComponentFigure || end instanceof DecoratorFigure)) {
			while (end instanceof DecoratorFigure)
				end = ((DecoratorFigure) end).peelDecoration();
			komponenta = (ComponentFigure) end;
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