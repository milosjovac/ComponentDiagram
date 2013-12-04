import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.aps.dmo.Komponenta;

public class HibernateTest2 {

	/**
	 * @param args
	 */
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

		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		
		session.save(kom1);
		
		session.getTransaction().commit();
		
	}

}