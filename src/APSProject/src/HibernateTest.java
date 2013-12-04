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
		kom.setIdKomponente(1);
		kom.setIme("Proba");
		kom.setHeight(20);
		kom.setWidth(200);
		kom.setPosX(20);
		kom.setPosY(20);
		kom.setStereotip(true);
		kom.setDekoracija(true);

		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(kom);
		
		session.getTransaction().commit();
		
	}

}
