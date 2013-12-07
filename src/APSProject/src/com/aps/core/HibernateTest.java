package com.aps.core;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.aps.dmo.Dijagram;
import com.aps.dmo.Interfejs;
import com.aps.dmo.Komponenta;

public class HibernateTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
		Interfejs i1 = new Interfejs();
		i1.setPosX(100);
		i1.setPosY(100);
		i1.setTip(true);

		Interfejs i2 = new Interfejs();
		i2.setPosX(100);
		i2.setPosY(100);
		i2.setTip(false);

		Komponenta kom = new Komponenta();
		kom.setIme("kom");
		kom.setHeight(20);
		kom.setWidth(200);
		kom.setPosX(20);
		kom.setPosY(20);
		kom.setStereotip(true);
		kom.setDekoracija(true);
		kom.getInterfejsi().add(i1);
		i1.setKomponenta(kom);
		i1.getInterfejsi().add(i2);

		Komponenta kom1 = new Komponenta();
		kom1.setIme("Kom1");
		kom1.setHeight(20);
		kom1.setWidth(200);
		kom1.setPosX(20);
		kom1.setPosY(20);
		kom1.setStereotip(true);
		kom1.setDekoracija(true);
		i2.setKomponenta(kom1);
		kom1.getInterfejsi().add(i2);

		Dijagram d = new Dijagram();
		d.getKomponente().add(kom);
		d.getKomponente().add(kom1);
		kom.setDijagram(d);
		kom1.setDijagram(d);
		d.setDate(new Date());
		d.setIme("ProbaDijag1");

		// WRITING OBJECT TO DATABASE
		SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		session.save(d);
		session.save(d);
		session.save(kom1);
		session.save(kom);
		session.save(i1);
		session.save(i2);

		session.getTransaction().commit();
		session.close();
		/*
		 * // RETRIEVING DATABASE INPUT AS OBJECT kom = null;
		 * 
		 * session = sessionFactory.openSession(); session.beginTransaction(); kom = (Komponenta)
		 * session.get(Komponenta.class, 3); System.out.println("Ime komponente je: " + kom.getIme());
		 * session.close();
		 */

	}

}
