package com.aps.core;

import java.awt.Color;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.hibernate.Session;

import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.figures.TextTool;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingChangeEvent;
import CH.ifa.draw.framework.DrawingChangeListener;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureChangeEvent;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.ActionTool;
import CH.ifa.draw.standard.ConnectionTool;
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.standard.DecoratorFigure;
import CH.ifa.draw.standard.FigureEnumerator;
import CH.ifa.draw.standard.StandardDrawing;
import CH.ifa.draw.standard.ToolButton;
import CH.ifa.draw.util.Geom;

import com.aps.connections.ComponentInterfaceConnection;
import com.aps.connections.InterfaceInterfaceConnection;
import com.aps.dmo.Dijagram;
import com.aps.dmo.Interfejs;
import com.aps.dmo.Komponenta;
import com.aps.figures.ComponentFigure;
import com.aps.figures.InterfaceEmptyFigure;
import com.aps.figures.InterfaceFigure;
import com.aps.figures.InterfaceFullFigure;
import com.aps.figures.StereotipDecorator;
import com.aps.figures.SymbolDecorator;
import com.aps.tools.StereotipTool;
import com.aps.tools.SymbolTool;

public class ClientApp extends DrawApplication {

	private static final long serialVersionUID = 5251415922750693346L;
	public static final String DIAGRAM_IMAGES = "/com/aps/images/";

	Dijagram dijagram;

	private boolean wPermission = false;
	String clientName;
	MainWindow server;

	Thread savingThread;

	public void setDijagram(Dijagram d) {
		int tmpHash = this.dijagram.getHashID();
		this.dijagram = d;
		this.dijagram.setHashID(tmpHash);
	}

	public ClientApp(MainWindow server, String title, MainWindow mainWindow, Dijagram dijagram,
			String clientName) {
		super(title + " - " + clientName);
		this.clientName = clientName;
		this.server = server;
		this.dijagram = dijagram;
		System.out.println(clientName + " : " + dijagram.hashCode());
		loadFromDB();
	}

	private void startSavingProcess() {

		if (!wPermission)
			return;

		if (savingThread != null && savingThread.isAlive())
			return;

		savingThread = new Thread(new Runnable() {

			@Override
			public void run() {
				if (saveToDB())
					updateObservers(dijagram);
				System.out.println("saved_to_DB");
			}
		});
		savingThread.start();
	}

	public void updateObservers(Dijagram dijagram) {
		server.updateObservers(dijagram);
	}

	protected void createTools(Panel panel) {
		super.createTools(panel);

		Tool tool;

		tool = new TextTool(view(), new TextFigure());
		panel.add(createToolButton(IMAGES + "TEXT", "Text Tool", tool));

		tool = new CreationTool(view(), new StereotipDecorator(new ComponentFigure(dijagram.getIme()),
				Color.blue));
		panel.add(createToolButton(DIAGRAM_IMAGES + "DEKORACIJA", "Component Tool", tool));

		ActionTool stereotipTool = new StereotipTool(view());
		ToolButton atmosphereButton = new ToolButton(this, DIAGRAM_IMAGES + "STEREOTIP_", "Stereotip Tool",
				stereotipTool);
		panel.add(atmosphereButton);

		ActionTool symbolTool = new SymbolTool(view());
		ToolButton symbolButton = new ToolButton(this, DIAGRAM_IMAGES + "DEK", "Decoration Tool", symbolTool);
		panel.add(symbolButton);

		tool = new ConnectionTool(view(), new ComponentInterfaceConnection());
		panel.add(createToolButton(DIAGRAM_IMAGES + "CONN", "Implementation Connector Tool", tool));

		tool = new CreationTool(view(), new InterfaceFullFigure());
		panel.add(createToolButton(DIAGRAM_IMAGES + "INT_FULL", "Provider Tool", tool));

		tool = new CreationTool(view(), new InterfaceEmptyFigure());
		panel.add(createToolButton(DIAGRAM_IMAGES + "INT_EMPTY", "Socket Tool", tool));

		tool = new ConnectionTool(view(), new InterfaceInterfaceConnection());
		panel.add(createToolButton(DIAGRAM_IMAGES + "INT_CONN", "Interface Connector Tool", tool));

	}

	@Override
	public void promptSaveAs() {
		toolDone();
		saveToDB();
		updateObservers(dijagram);
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
				// createDrawingFromDB(drawing());
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
				savingThread.interrupt();
				dispose();
				server.clientDisposed(dijagram, ClientApp.this);
			}
		});
		menu.add(mi);
		return menu;
	}

	public void reloadDrawing() {
		FigureEnumeration figures = drawing().figures();
		ArrayList<Figure> forDelete = new ArrayList<Figure>();
		while (figures.hasMoreElements()) {
			Figure fig = figures.nextFigure();
			forDelete.add(fig);
		}
		for (Figure f : forDelete) {
			drawing().remove(f);
		}
		// dijagram.getKomponente().clear();

		loadFromDB();

		createDrawingFromDB(drawing());
	}

	@Override
	protected void addListeners() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent event) {

				// savingThread.interrupt();
				server.clientDisposed(dijagram, ClientApp.this);
				dispose();
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
		} else if (!wPermission && !this.wPermission) {
			JOptionPane.showMessageDialog(ClientApp.this, "Read permission granted", "Permission",
					JOptionPane.INFORMATION_MESSAGE);
		}
		this.wPermission = wPermission;
	}

	private void loadFromDB() {
		// ORMManager.getManager().loadDiagramTree(dijagram);
		ORMManager.getManager().loadDiagramTree(dijagram, this);
	}

	/**
	 * Creates the drawing used in this application. You need to override this method to use a Drawing
	 * subclass in your application. By default a standard Drawing is returned.
	 */
	protected Drawing createDrawing() {
		Drawing drawing = new StandardDrawing();

		createDrawingFromDB(drawing);

		drawing.addDrawingChangeListener(new DrawingChangeListener() {

			@Override
			public void drawingRequestUpdate(DrawingChangeEvent e) {
				startSavingProcess();
			}

			@Override
			public void drawingInvalidated(DrawingChangeEvent e) {
				startSavingProcess();
			}
		});
		return drawing;
	}

	private void createDrawingFromDB(Drawing drawing) {
		ArrayList<InterfaceFigure> providers = new ArrayList<>();
		ArrayList<InterfaceFigure> socets = new ArrayList<>();

		// KREIRANJE SVIH KOMPONENTI I NJIOVIH INTERFEJSA
		// KREIRATI SAMO ONE KOJE VEC NE POSTOJE NA CRTEZU
		ORMManager ormManager = ORMManager.getManager();
		try {
			ormManager.createSession();
			ormManager.session.beginTransaction();

			for (Komponenta komponenta : dijagram.getKomponente()) {

				Figure figK = ComponentFigure.createComponent(komponenta);
				drawing.add(figK);

				for (Interfejs interfejs : komponenta.getInterfejsi()) {

					InterfaceFigure figI = InterfaceFigure.createInterface(interfejs, figK);
					drawing.add(figI);

					if (figI instanceof InterfaceEmptyFigure)
						socets.add(figI);
					else if (figI instanceof InterfaceFullFigure)
						providers.add(figI);

					// CRTANJE VEZE OD KOMPONENTE ZA INTERFEJSIMA
					ComponentInterfaceConnection veza = new ComponentInterfaceConnection();

					if (veza.canConnect(figK, figI)) {

						Point startPoint = Geom.center(figK.displayBox());
						veza.startPoint(startPoint.x, startPoint.y);
						veza.connectStart(figK.connectorAt(figK.displayBox().x, figK.displayBox().y));
						Point endPoint = Geom.center(figI.displayBox());
						veza.endPoint(endPoint.x, endPoint.y);
						veza.connectEnd(figI.connectorAt(figI.displayBox().x, figI.displayBox().y));
						veza.updateConnection();

						drawing.add(veza);
					}
				}
			}

			// KREIRANJE VEZA IZMEDJU INTERFEJSA

			for (InterfaceFigure provider : providers)
				for (Interfejs soketModel : provider.dbInterfejsModel.getSoketi())
					for (InterfaceFigure soket : socets)
						if (soket.dbInterfejsModel.equals(soketModel)) {
							// pravimo vezu
							// CRTANJE VEZE OD INT KA INTERFEJSIMA
							InterfaceInterfaceConnection veza = new InterfaceInterfaceConnection();

							Point startPoint = Geom.center(soket.displayBox());
							veza.startPoint(startPoint.x, startPoint.y);
							veza.connectStart(soket.connectorAt(soket.displayBox().x, soket.displayBox().y));

							Point endPoint = Geom.center(provider.displayBox());
							veza.endPoint(endPoint.x, endPoint.y);
							veza.connectEnd(provider.connectorAt(provider.displayBox().x,
									provider.displayBox().y));
							veza.updateConnection();

							drawing.add(veza);

						}

			FigureEnumerator fe = (FigureEnumerator) drawing.figures();
			while (fe.hasMoreElements()) {
				Figure f = fe.nextFigure();
				drawing.figureRequestUpdate(new FigureChangeEvent(f));
			}

			// this.invalidate();
			ormManager.session.getTransaction().commit();

		} catch (RuntimeException e) {
			System.out.println(e);
			ormManager.session.getTransaction().rollback();
			throw e;
		} finally {
			ormManager.closeSession();
		}

	}

	public synchronized boolean saveToDB() {
		boolean canSave = true;
		FigureEnumeration figures = drawing().figures();

		while (figures.hasMoreElements()) {
			Figure fig = figures.nextFigure();
			Figure tempFig = fig;
			if (tempFig instanceof ComponentFigure) {
				if (((ComponentFigure) tempFig).errorNotation) {
					canSave = false;
					break;
				}
			} else if (tempFig instanceof DecoratorFigure) {
				while (tempFig instanceof DecoratorFigure) {
					if (tempFig instanceof StereotipDecorator)
						tempFig = ((StereotipDecorator) tempFig).getComponent();
					if (tempFig instanceof SymbolDecorator)
						tempFig = ((SymbolDecorator) tempFig).getComponent();
				}
				if (((ComponentFigure) tempFig).errorNotation) {
					canSave = false;
					break;
				}

			} else if (fig instanceof InterfaceFigure) {
				if (((InterfaceFigure) fig).errorNotation) {
					canSave = false;
					break;
				}
			}
			tempFig = fig;
		}

		// sacuvaj dijagram for real
		if (canSave)
			return (ORMManager.getManager().saveDiagram(dijagram));
		else
			return false;
	}

}
