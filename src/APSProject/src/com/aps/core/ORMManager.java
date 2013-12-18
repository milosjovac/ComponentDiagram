package com.aps.core;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.aps.dmo.Dijagram;
import com.aps.dmo.Komponenta;
import com.aps.dmo.Interfejs;

public class ORMManager {

	private SessionFactory sessionFactory;
	private org.hibernate.service.ServiceRegistry serviceRegistry;
	private static ORMManager manager = null;
	Session session;

	private ORMManager() {
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		createSession();
	}

	public static ORMManager getManager() {
		if (manager == null)
			manager = new ORMManager();
		return manager;
	}

	public List<Dijagram> getAllDiagrams() {
		return (List<Dijagram>) executeQuery("from DIAGRAM");
	}

	public List<Komponenta> getAllComponents(Dijagram parentDijagram) {
		return null;
	}

	public List<Interfejs> getAllInterfaces(Komponenta parentDijagram) {
		return null;
	}

	private List<?> executeQuery(String query) {
		List<?> resultList = null;

		try {
			session.beginTransaction();

			Query q = session.createQuery(query);
			resultList = q.list();

			session.getTransaction().commit();
		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		}
		return resultList;
	}

	public void saveDiagram(Dijagram dijagram) {
		try {
			session.beginTransaction();

			session.save(dijagram);

			for (Komponenta komponenta : dijagram.getKomponente()) {
				session.save(komponenta);
				for (Interfejs interfesjs : komponenta.getInterfejsi()) {
					session.save(interfesjs);
				}
			}

			session.getTransaction().commit();

		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		}
	}

	public void createSession() {
		session = sessionFactory.openSession();
	}

	public void closeSession() {
		session.close();
	}

	public void loadDiagramTree(Dijagram dijagram) {
		try {
			session.beginTransaction();
			for (Komponenta k : dijagram.getKomponente()) {
				k.getInterfejsi();
				for (Interfejs inter : k.getInterfejsi())
					inter.getSoketi();
			}
			session.getTransaction().commit();

		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		}
	}
}
