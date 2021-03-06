package com.aps.core;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.aps.dmo.Dijagram;

public class MainWindow {
	private JFrame frame;
	public ArrayList<Dijagram> cashedDiagrams = new ArrayList<Dijagram>();
	public static ArrayList<Dijagram> dijagramiAktivni;

	int clientCounter = 0;
	DefaultTableModel model = new DefaultTableModel();
	JTable table = new JTable(model);

	int hashId = 0;

	private HashMap<Integer, ArrayList<ClientApp>> statistika = new HashMap<>();

	private Collection<ClientApp> clientWindows = new ArrayList<ClientApp>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void updateObservers(Dijagram dijagram) {

		ArrayList<ClientApp> observeri = statistika.get(dijagram.getHashID());
		// Brisemo prvi jer je on onaj koji menja crtez
		for (int i = 1; i < observeri.size(); i++) {
			ClientApp ca = observeri.get(i);
			// ca.setDijagram(dijagram);
			ca.reloadDrawing();
		}

	}

	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 474, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JLabel lblNewLabel = new JLabel("Select diagram");
		lblNewLabel.setBounds(12, 13, 132, 16);
		frame.getContentPane().add(lblNewLabel);

		if (dijagramiAktivni == null)
			dijagramiAktivni = new ArrayList<>();

		final JComboBox<String> comboBox = new JComboBox<String>();

		comboBox.addItem("New");
		cashedDiagrams = (ArrayList<Dijagram>) ORMManager.getManager().getAllDiagrams();
		for (Dijagram d : cashedDiagrams) {
			d.setHashID(hashId++);
			comboBox.addItem(d.getIme());
		}

		comboBox.setBounds(12, 42, 323, 22);
		frame.getContentPane().add(comboBox);
		// comboBox.setSelectedIndex(4);

		JButton btnNewButton = new JButton("Start client");

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Dijagram dijagram = null;
				ClientApp client = null;

				// NEW DIAGRAM
				if (comboBox.getSelectedIndex() == 0) {

					// uzmi ime novog dijagrama
					String name = JOptionPane.showInputDialog(null, "Choose the diagram name", "Name",
							JOptionPane.WARNING_MESSAGE);

					if (name == null)
						return;

					dijagram = new Dijagram();
					dijagram.setHashID(hashId++);
					dijagram.setDate(new Date());
					dijagram.setIme(name);
					cashedDiagrams.add(dijagram);

					// update comboBox
					comboBox.addItem(dijagram.getIme());
					comboBox.updateUI();

					client = new ClientApp(MainWindow.this, name, MainWindow.this, dijagram, "C"
							+ clientCounter++);
					client.open();

					// DIAGRAM IS LOADED FROM DATABASE
				} else {
					dijagram = cashedDiagrams.get(comboBox.getSelectedIndex() - 1);
					client = new ClientApp(MainWindow.this, dijagram.getIme(), MainWindow.this, dijagram, "C"
							+ clientCounter++);
					client.open();
				}

				dijagramiAktivni.add(dijagram);

				if (statistika.get(dijagram.getHashID()) == null) {
					ArrayList<ClientApp> q = new ArrayList<ClientApp>();
					q.add(client);
					statistika.put(dijagram.getHashID(), q);
				} else {
					statistika.get(dijagram.getHashID()).add(client);
				}
				if (statistika.get(dijagram.getHashID()).size() == 1)
					client.setwPermission(true);
				else
					client.setwPermission(false);

				refreshTableStat();

			}
		});
		btnNewButton.setBounds(347, 41, 97, 25);
		frame.getContentPane().add(btnNewButton);

		// Create a couple of columns
		model.addColumn("Diagram name");
		model.addColumn("Working queue");
		table.setBounds(12, 77, 432, 196);
		frame.getContentPane().add(table);
		btnNewButton.requestFocusInWindow();
	}

	public void refreshTableStat() {
		model.setRowCount(0);
		for (int d : statistika.keySet()) {
			String queue = "";
			for (ClientApp cap : statistika.get(d))
				queue += cap.clientName + " <- ";

			if (queue.length() > 0)
				queue = queue.substring(0, queue.length() - 4);
			Dijagram tmpDijagram = null;
			for (Dijagram dijagram : cashedDiagrams) {
				if (dijagram.getHashID() == d) {
					tmpDijagram = dijagram;
					break;
				}
			}
			model.addRow(new Object[] { tmpDijagram.getIme(), "[ " + queue + " ]" });
		}

		table.updateUI();
	}

	public void clientDisposed(Dijagram d, ClientApp client) {

		statistika.get(d.getHashID()).remove(client);
		if (statistika.get(d.getHashID()).size() > 0)
			statistika.get(d.getHashID()).get(0).setwPermission(true);
		else
			statistika.remove(d.getHashID());
		refreshTableStat();
	}
}
