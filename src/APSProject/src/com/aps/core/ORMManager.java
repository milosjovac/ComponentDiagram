package com.aps.core;

import java.awt.Component;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.service.ServiceRegistryBuilder;

import com.aps.dmo.Dijagram;
import com.aps.dmo.Komponenta;
import com.aps.dmo.Interfejs;
import com.aps.figures.ComponentFigure;

public class ORMManager {

	private SessionFactory sessionFactory;
	private org.hibernate.service.ServiceRegistry serviceRegistry;
	private static ORMManager manager = null;
	public Session session;

	private ORMManager() {
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		createSession();
	}

	public synchronized static ORMManager getManager() {
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

	public boolean saveDiagram(Dijagram dijagram) {

		evict2ndLevelCache();
		try {
			createSession();
			session.beginTransaction();

			session.saveOrUpdate(dijagram);

			// session.save(dijagram);

			for (Komponenta komponenta : dijagram.getKomponente()) {
				session.saveOrUpdate(komponenta);

				for (Interfejs interfesjs : komponenta.getInterfejsi()) {
					session.saveOrUpdate(interfesjs);

				}
			}

			session.flush();
			session.clear();

			session.getTransaction().commit();

		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		} finally {
			if (session.isOpen())
				session.close();
		}

		return true;
	}

	public void createSession() {
		session = sessionFactory.openSession();
	}

	public void closeSession() {
		session.close();
	}

	public void loadDiagramTree(Dijagram dijagram, ClientApp ca) {
		try {
			createSession();
			session.beginTransaction();

			int id = dijagram.getId();
			if (id != 0){
				dijagram = (Dijagram) session.get(Dijagram.class, id);
				
			}

			// session.evict(dijagram);
			// Query query = session
			// .createQuery("from Komponenta where DIAGRAM_ID = :code ");
			// query.setParameter("code", dijagram.getId());

			// List<Komponenta> listaKomponenti = query.list();
			List<Komponenta> listaKomponenti = null;
			if (dijagram != null)
				listaKomponenti = (List<Komponenta>) dijagram.getKomponente();
			if (listaKomponenti.size() != 0) {

				for (Komponenta k : listaKomponenti) {

					k.getInterfejsi();
					for (Interfejs inter : k.getInterfejsi())
						inter.getSoketi();
				}
				ca.setDijagram(dijagram);
			}
			session.getTransaction().commit();

		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		} finally {
			if (session.isOpen())
				session.close();
		}
	}

	public void deleteDijagram(Dijagram o) {
		try {
			createSession();
			session.beginTransaction();

			session.delete(o);
			session.flush();
			session.clear();
			session.getTransaction().commit();

			sessionFactory.getCache().evictEntityRegions();
			sessionFactory.getCache().evictCollectionRegions();
			sessionFactory.getCache().evictDefaultQueryRegion();
			sessionFactory.getCache().evictQueryRegions();

		} catch (RuntimeException e) {
			System.out.println(e);
			session.getTransaction().rollback();
			throw e;
		} finally {
			if (session.isOpen())
				session.close();
		}
	}

	public void evict2ndLevelCache() {
		try {
			Map<String, ClassMetadata> classesMetadata = sessionFactory.getAllClassMetadata();
			for (String entityName : classesMetadata.keySet()) {

				sessionFactory.evictEntity(entityName);
			}
		} catch (Exception e) {

		}
	}
}
