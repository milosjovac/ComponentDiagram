

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.aps.dmo.Komponenta;

public class HibernateTest2 {

	/**
	 * @param args
	 */
	private static SessionFactory sessionFactory;
	private static org.hibernate.service.ServiceRegistry serviceRegistry;
	public static void main(String[] args) {
		Komponenta kom1 = new Komponenta();
		kom1.setIdKomponente(2);
		kom1.setIme("Nexoslav proba");
		kom1.setHeight(100);
		kom1.setWidth(300);
		kom1.setPosX(0);
		kom1.setPosY(0);
		kom1.setStereotip(true);
		kom1.setDekoracija(true);

		Configuration configuration = new Configuration();
	    configuration.configure();
	    serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();        
	    sessionFactory = configuration.buildSessionFactory(serviceRegistry);
	    
	    Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.save(kom1);
		session.getTransaction().commit();
	    /*
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(kom1);
		
		session.getTransaction().commit();
		*/
	}

}