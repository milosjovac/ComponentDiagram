package com.aps.figures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Vector;

import CH.ifa.draw.figures.EllipseFigure;
import CH.ifa.draw.figures.GroupFigure;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.framework.Handle;

import com.aps.dmo.Interfejs;

public abstract class InterfaceFigure extends GroupFigure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3773858305815319822L;

	public ArrayList<InterfaceFigure> connectedInterfaces = new ArrayList<>();

	// DB MODEL
	public Interfejs dbInterfejsModel = new Interfejs();

	public String tip;
	public TextFigure name;
	protected Rectangle fDisplayBox;
	public boolean errorNotation = true;
	protected EllipseFigure krug;
	public Figure parentFigure = null;

	public static final int MIN_WIDTH = 30;
	public static final int MIN_HEIGHT = 30;
	public int curWidth = MIN_WIDTH;
	public int curHeight = MIN_HEIGHT;

	public InterfaceFigure() {
		super();

		fDisplayBox = new Rectangle(0, 0, 0, 0);
		krug = new EllipseFigure(new Point(0, 0), new Point(0, 0));

		krug.setAttribute("FillColor", Color.BLUE);

		Font fb = new Font("Helvetica", Font.BOLD, 17);

		name = new TextFigure();
		name.setFont(fb);
		name.setText("Name");
		name.setAttribute("TextColor", Color.black);

		super.add(name);

	}

	@Override
	public void basicDisplayBox(Point origin, Point corner) {
		fDisplayBox = new Rectangle(origin);
		fDisplayBox.add(corner);

		if (fDisplayBox.width > MIN_WIDTH)
			curWidth = fDisplayBox.width;
		if (fDisplayBox.height > MIN_HEIGHT)
			curHeight = fDisplayBox.height;

		layout();
	}

	private void layout() {
		Point partOrigin = new Point(fDisplayBox.x, fDisplayBox.y);
		Dimension extent = new Dimension(curWidth, curHeight);

		FigureEnumeration k = figures();
		while (k.hasMoreElements()) {
			Figure f = k.nextFigure();

			Dimension partExtent = f.size();
			Point corner = new Point(partOrigin.x + partExtent.width, partOrigin.y + partExtent.height);

			int y = (int) (partOrigin.y + curHeight);
			Point p = null;

			if (f.size().getWidth() < curWidth) {
				p = new Point((int) (partOrigin.x + (curWidth - f.size().getWidth()) / 2), y);
			} else {
				p = new Point(partOrigin.x, y);
			}
			f.basicDisplayBox(p, corner);

			extent.width = Math.max(extent.width, partExtent.width);
			extent.height = Math.max(extent.height, partExtent.height);

			partOrigin.y += partExtent.height;
		}

		fDisplayBox.width = extent.width;
		fDisplayBox.height = extent.height;

		krug.displayBox(new Point(fDisplayBox.x + (fDisplayBox.width - MIN_WIDTH) / 2, fDisplayBox.y),
				new Point(fDisplayBox.x + (fDisplayBox.width - MIN_WIDTH) / 2 + MIN_WIDTH, fDisplayBox.y
						+ MIN_HEIGHT));

		updateDBModel();
	}

	protected void updateDBModel() {
		dbInterfejsModel.setName(name.getText().toString());
		dbInterfejsModel.setPosX(displayBox().x);
		dbInterfejsModel.setPosY(displayBox().y);

		// dodati komponentu kroz CompInt konektor hendler
		// dodati interfejse kroz IntOInt konek,...
	}

	public Vector<Handle> handles() {
		Vector<Handle> handles = new Vector<Handle>();
		return handles;
	}

	/*
	 * public Connector connectorAt(int x, int y) { return new ChopEllipseConnector(krug); }
	 */

	public Insets connectionInsets() {
		Rectangle r = fDisplayBox;
		int cx = r.width / 2;
		int cy = r.height / 2;
		return new Insets(cy, cx, cy, cx);
	}

	public Rectangle displayBox() {
		return new Rectangle(fDisplayBox.x, fDisplayBox.y, fDisplayBox.width, fDisplayBox.height);
	}

	protected void basicMoveBy(int x, int y) {
		fDisplayBox.translate(x, y);
		super.basicMoveBy(x, y);
	}

	public void update(FigureChangeEvent e) {

		layout();
		changed();
	}

	public void figureChanged(FigureChangeEvent e) {
		update(e);

	}

	public void figureRemoved(FigureChangeEvent e) {
		update(e);
	}

	@Override
	public boolean canConnect() {
		return true;
	}

	public static Figure createInterface(Interfejs i, Figure c) {
		InterfaceFigure rezultat = null;
		

		if (i.isTip()) {
			rezultat = new InterfaceFullFigure();
		} else {
			rezultat = new InterfaceEmptyFigure();
		}

		rezultat.dbInterfejsModel = i;
		rezultat.name.setText(i.getName());

		rezultat.displayBox(new Point(i.getPosX(), i.getPosY()), new Point(i.getPosX(), i.getPosY()));

		rezultat.parentFigure = c;
		if (c != null)
			rezultat.errorNotation = false;

		return rezultat;
	}
}
