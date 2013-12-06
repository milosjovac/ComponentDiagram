import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.aps.dmo.Dijagram;


public class Window1 {
	private static SessionFactory sessionFactory;
	private static org.hibernate.service.ServiceRegistry serviceRegistry;
	private JFrame frame;
	private JTable table;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window1 window = new Window1();
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
	public Window1() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    
	    Session session = sessionFactory.openSession();
	    List<Dijagram> lista = session.createCriteria(Dijagram.class).list();
	    
		
		frame = new JFrame();
		frame.setBounds(100, 100, 474, 333);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Select diagram");
		lblNewLabel.setBounds(12, 13, 132, 16);
		frame.getContentPane().add(lblNewLabel);
		
		String[] dijagramString = { "Bird", "Cat", "Dog", "Rabbit", "Pig" };
		JComboBox comboBox = new JComboBox(lista.toArray());
		comboBox.setBounds(12, 42, 323, 22);
		frame.getContentPane().add(comboBox);
		comboBox.setSelectedIndex(4);
		
		
		JButton btnNewButton = new JButton("Start client");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(347, 41, 97, 25);
		frame.getContentPane().add(btnNewButton);
		
		DefaultTableModel model = new DefaultTableModel(); 
		JTable table = new JTable(model); 

		// Create a couple of columns 
		model.addColumn("Col1"); 
		model.addColumn("Col2"); 
		table.getTableHeader().setVisible(true);
		//model.addRow(new Object[]{"v1", "v2"});
		table.setBounds(12, 77, 432, 196);
		frame.getContentPane().add(table);
	
	
		
		
	}
}
