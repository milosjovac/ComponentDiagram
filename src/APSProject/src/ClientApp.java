import CH.ifa.draw.application.DrawApplication;
import CH.ifa.draw.standard.ActionTool;
import CH.ifa.draw.standard.ConnectionTool;
import CH.ifa.draw.standard.CreationTool;
import CH.ifa.draw.standard.ToolButton;
import CH.ifa.draw.figures.TextFigure;
import CH.ifa.draw.figures.TextTool;
import CH.ifa.draw.framework.Drawing;
import CH.ifa.draw.framework.DrawingEditor;
import CH.ifa.draw.framework.Figure;
import CH.ifa.draw.framework.FigureEnumeration;
import CH.ifa.draw.framework.Tool;
import CH.ifa.draw.standard.FigureEnumerator;
import CH.ifa.draw.standard.StandardDrawing;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;

import com.aps.dmo.Dijagram;

public class ClientApp extends DrawApplication {

	/**
     *
     */
	private static final long serialVersionUID = 5251415922750693346L;
	static private final String DIAGRAM_IMAGES = "/dijagram/images/";

	boolean wPermission = false;
	String clientName;

	int Did; // cita se iz baze
	String Dname = "Dijagram Nexoslavljev"; // cita se iz baze

	// HashMap<Komponenta, List<Interfejs>> komponente;

	public ClientApp(String title, MainWindow mainWindow, Dijagram dijagram, String clientName) {
		super(title + " - " + clientName);
		this.clientName = clientName;
	}

	/*
	 * public ClientApp(String title, ClientForm client, int ID, HashMap<Komponenta, List<Interfejs>> komp) {
	 * super(title); Dname = title; this.client = client; this.Did = ID; this.komponente = komp;
	 * 
	 * }
	 */
	/*
	 * protected void createTools(Panel panel) { super.createTools(panel);
	 * 
	 * Tool tool;
	 * 
	 * tool = new TextTool(view(), new TextFigure()); panel.add(createToolButton(IMAGES + "TEXT", "Text Tool",
	 * tool));
	 * 
	 * tool = new CreationTool(view(), new StereotipDecorator(new ComponentFigure(), Color.blue));
	 * panel.add(createToolButton(DIAGRAM_IMAGES + "PERT", "Komponenta", tool));
	 * 
	 * ActionTool stereotipTool = new StereotipTool(view()); ToolButton atmosphereButton = new
	 * ToolButton(this, IMAGES + "PERT", "Stereotip Tool", stereotipTool); panel.add(atmosphereButton);
	 * 
	 * ActionTool symbolTool = new SymbolTool(view()); ToolButton symbolButton = new ToolButton(this, IMAGES +
	 * "PERT", "Stereotip Tool", symbolTool); panel.add(symbolButton);
	 * 
	 * tool = new ConnectionTool(view(), new ComponentInterfaceConnection());
	 * panel.add(createToolButton(IMAGES + "CONN", "Dependency Tool", tool));
	 * 
	 * tool = new CreationTool(view(), new InterfaceFullFigure()); panel.add(createToolButton(DIAGRAM_IMAGES +
	 * "PERT", "Komponenta", tool));
	 * 
	 * tool = new CreationTool(view(), new InterfaceEmptyFigure()); panel.add(createToolButton(DIAGRAM_IMAGES
	 * + "PERT", "Komponenta", tool));
	 * 
	 * tool = new ConnectionTool(view(), new InterfaceInterfaceConnection());
	 * panel.add(createToolButton(IMAGES + "CONN", "Dependency Tool", tool));
	 * 
	 * }
	 */

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

}
