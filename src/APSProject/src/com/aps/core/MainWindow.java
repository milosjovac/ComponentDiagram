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
import org.hibernate.SessionFactory;
import org.omg.CORBA.OMGVMCID;

import com.aps.dmo.Dijagram;

public class MainWindow {
	private JFrame frame;
	public ArrayList<Dijagram> cashedDiagrams = new ArrayList<Dijagram>();
	public static ArrayList<Dijagram> dijagramiAktivni;

	int clientCounter = 0;
	DefaultTableModel model = new DefaultTableModel();
	JTable table = new JTable(model);

	private HashMap<Dijagram, Queue<ClientApp>> statistika = new HashMap<>();

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
	public void updateObservers() {
		for (ClientApp ca : clientWindows) {
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

					// Sacuvaj model u bazu
					dijagram = new Dijagram();
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

				if (statistika.get(dijagram) == null) {
					Queue q = new LinkedList<ClientApp>();
					q.add(client);
					statistika.put(dijagram, q);
				} else {
					statistika.get(dijagram).add(client);
				}
				if (statistika.get(dijagram).size() == 1)
					client.setwPermission(true);

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

	}

	public void refreshTableStat() {
		model.setRowCount(0);
		for (Dijagram d : statistika.keySet()) {
			String queue = "";
			for (ClientApp cap : statistika.get(d))
				queue += cap.clientName + " <- ";

			if (queue.length() > 0)
				queue = queue.substring(0, queue.length() - 4);
			model.addRow(new Object[] { d.getIme(), "[ " + queue + " ]" });
		}

		table.updateUI();
	}

	public void clientDisposed(Dijagram d, ClientApp client) {

		statistika.get(d).remove(client);
		if (statistika.get(d).size() > 0)
			statistika.get(d).peek().setwPermission(true);
		else
			statistika.remove(d);
		refreshTableStat();
	}
}
