package pl.edu.agh.iisg.to.battleships.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionService {

    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    private static Session session;

    public static void openSession() {
        session = sessionFactory.openSession();
    }

    public static Session getSession() {
        return session;
    }

    public static void closeSession() {
        session.close();
    }
}
