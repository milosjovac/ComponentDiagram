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

		session.beginTransaction();

		Query q = session.createQuery(query);
		List<?> resultList = q.list();

		session.getTransaction().commit();
		return resultList;
	}

	public void saveDiagram(Dijagram dijagram) {
		session.beginTransaction();

		session.save(dijagram);

		for (Komponenta komponenta : dijagram.getKomponente()) {
			session.save(komponenta);
			for (Interfejs interfesjs : komponenta.getInterfejsi()) {
				if (interfesjs.getInterfejsi().size() == 0 || interfesjs.isTip())
					session.save(interfesjs);
			}
		}

		session.getTransaction().commit();
	}

	public void createSession() {
		session = sessionFactory.openSession();
	}

	public void closeSession() {
		session.close();
	}
}
