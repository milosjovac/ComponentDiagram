package com.aps.figures;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Vector;

import CH.ifa.draw.figures.GroupFigure;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.framework.Handle;
import CH.ifa.draw.standard.BoxHandleKit;
import CH.ifa.draw.standard.ConnectionHandle;
import CH.ifa.draw.standard.RelativeLocator;

import com.aps.core.MainWindow;
import com.aps.dmo.Dijagram;
import com.aps.dmo.Komponenta;

public class ComponentFigure extends GroupFigure {

	// db model
	Komponenta dbKomponenta = new Komponenta();
	public boolean dekoracija = false;
	public boolean stereotip = true;

	private static final long serialVersionUID = 1L;

	public boolean errorNotation = false;
	public LinkedList<InterfaceFigure> interfejsi = new LinkedList<InterfaceFigure>();

	public Rectangle getfDisplayBox() {
		return fDisplayBox;
	}

	public void setfDisplayBox(Rectangle fDisplayBox) {
		this.fDisplayBox = fDisplayBox;
	}

	public TextFigure getName() {
		return name;
	}

	public void setName(TextFigure name) {
		this.name = name;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getMinWidth() {
		return MIN_WIDTH;
	}

	public static int getMinHeight() {
		return MIN_HEIGHT;
	}

	private Rectangle fDisplayBox;
	private TextFigure name;

	public static final int MIN_WIDTH = 220;
	public static final int MIN_HEIGHT = 60;
	public int curWidth = MIN_WIDTH;
	public int curHeight = MIN_HEIGHT;
	private String imeDijagrama;

	public ComponentFigure(String dijagramName) {
		super();

		fDisplayBox = new Rectangle(0, 0, 0, 0);
		imeDijagrama = dijagramName;
		Font fb = new Font("Helvetica", Font.BOLD, 17);

		name = new TextFigure();
		name.setFont(fb);
		name.setText("Name");
		name.setAttribute("TextColor", Color.black);

		add(name);

	}

	public ComponentFigure() {
		// TODO Auto-generated constructor stub
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
			extent.height += partExtent.height;
			partOrigin.y += partExtent.height;
		}

		fDisplayBox.width = extent.width;
		fDisplayBox.height = extent.height;

		updateDBModel();
	}

	private void updateDBModel() {
		dbKomponenta.setDekoracija(dekoracija);
		dbKomponenta.setHeight(displayBox().height);
		dbKomponenta.setWidth(displayBox().width);
		dbKomponenta.setIme(name.getText().toString());
		dbKomponenta.setPosX(displayBox().x);
		dbKomponenta.setPosY(displayBox().y);
		dbKomponenta.setStereotip(stereotip);

		if (dbKomponenta.getDijagram() == null) {
			Dijagram dijagram = null;
			for (int i = 0; i < MainWindow.dijagramiAktivni.size(); i++) {
				if (MainWindow.dijagramiAktivni.get(i).getIme().equals(imeDijagrama)) {
					dijagram = MainWindow.dijagramiAktivni.get(i);
					break;
				}
			}

			if (dijagram != null) {
				dbKomponenta.setDijagram(dijagram);
				dijagram.getKomponente().add(dbKomponenta);
			} else
				System.out.println("Tebra nesto ne valja, dijagram nije nadjen!");
		}

	}

	public Vector<Handle> handles() {

		Vector<Handle> handles = new Vector<Handle>();
		handles.add(BoxHandleKit.southEast(this));

		handles.addElement(new ConnectionHandle(this, RelativeLocator.east(),
				new ComponentInterfaceConnection()));

		return handles;
	}

	public void draw(Graphics g) {

		if (errorNotation) {
			name.setAttribute("TextColor", Color.red);
			g.setColor(Color.red);
		} else {
			name.setAttribute("TextColor", Color.black);
			g.setColor(Color.black);
		}
		super.draw(g);
		Rectangle r = displayBox();
		g.drawRect(r.x, r.y, r.width, r.height);
	}

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

		for (InterfaceFigure intF : interfejsi) {
			if (intF instanceof InterfaceEmptyFigure)
				((InterfaceEmptyFigure) intF).rotate();
		}
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

	public static Figure createComponent(Komponenta k) {

		Figure rezultat = null;

		ComponentFigure rez = new ComponentFigure();
		rez.fDisplayBox = new Rectangle(0, 0, 0, 0);
		rez.dbKomponenta = k;
		rez.imeDijagrama = k.getDijagram().getIme();

		TextFigure name = new TextFigure();
		Font fb = new Font("Helvetica", Font.BOLD, 17);
		name.setFont(fb);
		name.setText(k.getIme());
		name.setAttribute("TextColor", Color.black);
		rez.add(name);
		rez.setName(name);

		rezultat = rez;

		if (k.isDekoracija())
			rezultat = new SymbolDecorator(rezultat, Color.blue);

		if (k.isStereotip())
			rezultat = new StereotipDecorator(rezultat, Color.blue);

		rez.displayBox(new Point(k.getPosX(), k.getPosY()), new Point(k.getPosX() + k.getWidth(), k.getPosY()
				+ k.getHeight()));

		return rezultat;
	}

}
