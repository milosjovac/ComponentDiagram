import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.aps.dmo.Komponenta;

public class HibernateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Komponenta kom = new Komponenta();
		kom.setIme("Proba");
		kom.setHeight(20);
		kom.setWidth(200);
		kom.setPosX(20);
		kom.setPosY(20);
		kom.setStereotip(true);
		kom.setDekoracija(true);

		
		// WRITING OBJECT TO DATABASE
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(kom);		
		session.getTransaction().commit();
		session.close();
		
		// RETRIEVING DATABASE INPUT AS OBJECT
		kom = null;
		
		session = sessionFactory.openSession();
		session.beginTransaction();
		kom = (Komponenta) session.get(Komponenta.class, 3);
		System.out.println("Ime komponente je: " + kom.getIme());
		session.close();
		
		
	}

}
