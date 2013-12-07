package com.aps.core;

import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.figures.TextTool;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.ActionTool;
import CH.ifa.draw.standard.ConnectionTool;
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.standard.ToolButton;

import com.aps.dmo.Dijagram;
import com.aps.figures.ComponentFigure;
import com.aps.figures.ComponentInterfaceConnection;
import com.aps.figures.InterfaceEmptyFigure;
import com.aps.figures.InterfaceFullFigure;
import com.aps.figures.InterfaceInterfaceConnection;
import com.aps.figures.StereotipDecorator;
import com.aps.figures.StereotipTool;
import com.aps.figures.SymbolTool;

public class ClientApp extends DrawApplication {

	private static final long serialVersionUID = 5251415922750693346L;
	static private final String DIAGRAM_IMAGES = "/dijagram/images/";

	Dijagram dijagram;

	private boolean wPermission = false;
	String clientName;
	MainWindow server;

	int Did; // cita se iz baze
	String Dname = "Dijagram Nexoslavljev"; // cita se iz baze

	// HashMap<Komponenta, List<Interfejs>> komponente;

	public ClientApp(MainWindow server, String title, MainWindow mainWindow, Dijagram dijagram,
			String clientName) {
		super(title + " - " + clientName);
		this.clientName = clientName;
		this.server = server;
		this.dijagram = dijagram;
	}

	protected void createTools(Panel panel) {
		super.createTools(panel);

		Tool tool;

		tool = new TextTool(view(), new TextFigure());
		panel.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		tool = new CreationTool(view(), new StereotipDecorator(new ComponentFigure(dijagram.getIme()),
				Color.blue));
		panel.add(createToolButton(DIAGRAM_IMAGES + "PERT", "Komponenta", tool));

		ActionTool stereotipTool = new StereotipTool(view());
		ToolButton atmosphereButton = new ToolButton(this, IMAGES + "PERT", "Stereotip Tool", stereotipTool);
		panel.add(atmosphereButton);

		ActionTool symbolTool = new SymbolTool(view());
		ToolButton symbolButton = new ToolButton(this, IMAGES + "PERT", "Stereotip Tool", symbolTool);
		panel.add(symbolButton);

		tool = new ConnectionTool(view(), new ComponentInterfaceConnection());
		panel.add(createToolButton(IMAGES + "CONN", "Dependency Tool", tool));

		tool = new CreationTool(view(), new InterfaceFullFigure());
		panel.add(createToolButton(DIAGRAM_IMAGES + "PERT", "Komponenta", tool));

		tool = new CreationTool(view(), new InterfaceEmptyFigure());
		panel.add(createToolButton(DIAGRAM_IMAGES + "PERT", "Komponenta", tool));

		tool = new ConnectionTool(view(), new InterfaceInterfaceConnection());
		panel.add(createToolButton(IMAGES + "CONN", "Dependency Tool", tool));

	}

	/**
	 * Creates the drawing used in this application. You need to override this method to use a Drawing
	 * subclass in your application. By default a standard Drawing is returned.
	 */
	/*
	 * protected Drawing createDrawing() { Drawing d = new StandardDrawing();
	 * 
	 * List<Komponenta> kk = new LinkedList(komponente.keySet());
	 * 
	 * for (Komponenta k : kk) { Figure figK = ComponentFigure.createComponent(k); for (Interfejs i :
	 * komponente.get(k)) { Figure figI = InterfaceFigure.createInterface(i, figK); d.add(figI);
	 * 
	 * ComponentInterfaceConnection conCI = new ComponentInterfaceConnection(); }
	 * 
	 * d.add(figK); } /* ConnectionFigure cf = new SmartConnectionFigure(); cf.setLiner(new ElbowLiner());
	 * cf.setStartConnector(ta.findConnector(Geom.center(ta.getBounds()), cf));
	 * cf.setEndConnector(tb.findConnector(Geom.center(tb.getBounds()), cf));
	 */

	/*
	 * return d; }
	 */

	@Override
	public void promptSaveAs() {
		toolDone();
		saveToDB();
		// Save the mutherfucker
	}

	protected Menu createFileMenu() {

		Menu menu = new Menu("File");

		MenuItem mi = new MenuItem("Save", new MenuShortcut('s'));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				promptSaveAs();
			}
		});
		menu.add(mi);

		mi = new MenuItem("Delete", new MenuShortcut('d'));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				// deleteAll();
			}
		});
		menu.add(mi);

		menu.add(mi);
		menu.addSeparator();
		mi = new MenuItem("Print...", new MenuShortcut('p'));
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				print();
			}
		});
		menu.add(mi);
		menu.addSeparator();
		mi = new MenuItem("Exit");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				exit();
			}
		});
		menu.add(mi);
		return menu;
	}

	public void reloadDrawing() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {
				dispose();
				server.clientDisposed(dijagram, ClientApp.this);
				ORMManager.getManager().closeSession();
			}
		});
	}

	public boolean iswPermission() {
		return wPermission;
	}

	public void setwPermission(boolean wPermission) {
		if (wPermission && !this.wPermission) {
			JOptionPane.showMessageDialog(ClientApp.this, "Write permission granted", "Permission",
					JOptionPane.INFORMATION_MESSAGE);
		} else if (!wPermission && this.wPermission) {
			JOptionPane.showMessageDialog(ClientApp.this, "Write permission restricted", "Permission",
					JOptionPane.INFORMATION_MESSAGE);
		}
		this.wPermission = wPermission;
	}

	public void saveToDB() {
		// dopuni DMO dijagram sa realnim figurama
		/*
		 * FigureEnumeration figure = drawing().figures();
		 * 
		 * while (figure.hasMoreElements()) { Figure fig = figure.nextFigure(); if (fig instanceof
		 * ComponentFigure || fig instanceof DecoratorFigure) { Komponenta komponenta = new Komponenta(fig,
		 * dijagram, modelInterfejsi); dijagram.getKomponente().add(komponenta); } }
		 * 
		 * 
		 * /* for (Interfejs interfejs : modelInterfejsi) { // prodji kroz sve zive interface if
		 * (interfejs.getInterfejsFigura().connectedInterfaces.size() > 0) { // ako neki od njih ima vise //
		 * od 0 konekcije for (InterfaceFigure figura : interfejs.getInterfejsFigura().connectedInterfaces) //
		 * prodji kroz sve interface u globalu
		 * 
		 * for (Interfejs interfejsIn : modelInterfejsi) { // i uporedi ih sa konekcijama if
		 * (figura.equals(interfejsIn.getInterfejsFigura())) { interfejs.getInterfejsi().add(interfejsIn); } }
		 * } }
		 */

		// sacuvaj dijagram for real
		ORMManager.getManager().saveDiagram(dijagram);
	}
}
