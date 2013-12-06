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

	private ORMManager() {
		Configuration configuration = new Configuration();
		configuration.configure();
		serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
				.buildServiceRegistry();
		sessionFactory = configuration.buildSessionFactory(serviceRegistry);
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
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		Query q = session.createQuery(query);
		List<?> resultList = q.list();

		session.getTransaction().commit();
		session.close();

		return resultList;
	}

	public void saveDiagram(Dijagram dijagram) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(dijagram);

		for (Komponenta komponenta : dijagram.getKomponente()) {
			session.save(komponenta);
			for (Interfejs interfesjs : komponenta.getInterfejsi()) {
				session.save(interfesjs);
				for (Interfejs interfejs2 : interfesjs.getInterfejsi()) {
					session.save(interfejs2);
				}
			}
		}

		session.getTransaction().commit();
		session.close();
	}
}
