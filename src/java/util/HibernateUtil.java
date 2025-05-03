package util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;

    static {
        try {
            System.out.println("Initialisation de HibernateUtil...");
            sessionFactory = new Configuration()
                .configure("config/hibernate.cfg.xml") 
                .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Erreur d'initialisation de la SessionFactory : " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
