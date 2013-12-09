package com.aps.figures;

import java.util.Vector;

import CH.ifa.draw.figures.ArrowTip;
import CH.ifa.draw.figures.LineConnection;
import CH.ifa.draw.figures.PolyLineFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.standard.NullHandle;

public class InterfaceInterfaceConnection extends LineConnection {
	/*
	 * Serialization support.
	 */
	private static final long serialVersionUID = -7959500008698525009L;
	private int pertDependencySerializedDataVersion = 1;

	public InterfaceInterfaceConnection() {
		setEndDecoration(new ArrowTip());
		// setEndDecoration(null);
		setStartDecoration(null);
	}

	public void handleConnect(Figure start, Figure end) {
		InterfaceFullFigure provider;
		InterfaceEmptyFigure slot;

		if (start instanceof InterfaceFullFigure) {
			provider = (InterfaceFullFigure) start;
			slot = (InterfaceEmptyFigure) end;

		} else {
			provider = (InterfaceFullFigure) end;
			slot = (InterfaceEmptyFigure) start;
		}

		slot.dbInterfejsModel.getInterfejsi().add(provider.dbInterfejsModel);
		provider.dbInterfejsModel.getInterfejsi().add(slot.dbInterfejsModel);

		//

		// ADDING ALL DEPENDECES
		slot.connectedInterfaces.add(provider);
		provider.connectedInterfaces.add(slot);
	}

	public void handleDisconnect(Figure start, Figure end) {

		// sam zove disconnect kada se pokusa kreiranje veze koja c
		if (start == null || end == null)
			return;

		InterfaceFullFigure provider;
		InterfaceEmptyFigure slot;

		if (start instanceof InterfaceFullFigure) {
			provider = (InterfaceFullFigure) start;
			slot = (InterfaceEmptyFigure) end;

		} else {
			provider = (InterfaceFullFigure) end;
			slot = (InterfaceEmptyFigure) start;
		}

		slot.dbInterfejsModel.getInterfejsi().remove(provider.dbInterfejsModel);
		provider.dbInterfejsModel.getInterfejsi().remove(slot.dbInterfejsModel);

		if (slot.connectedInterfaces.contains(provider))
			slot.connectedInterfaces.remove(provider);

		if (provider.connectedInterfaces.contains(slot))
			provider.connectedInterfaces.remove(slot);

	}

	public boolean canConnect(Figure start, Figure end) {
		if ((start instanceof InterfaceEmptyFigure) && (end instanceof InterfaceFullFigure)) {
			if (!((InterfaceFigure) start).connectedInterfaces.contains(end))
				if (((InterfaceFigure) start).parentFigure != null
						&& ((InterfaceFigure) end).parentFigure != null)
					if (!((InterfaceFigure) start).parentFigure.equals(((InterfaceFigure) end).parentFigure))
						return true;
		}
		return false;
	}

	public Vector handles() {
		Vector handles = super.handles();
		// don't allow to reconnect the starting figure
		handles.setElementAt(new NullHandle(this, PolyLineFigure.locator(0)), 0);
		return handles;
	}
}