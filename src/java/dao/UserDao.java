package dao;

import entities.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.Query;
import util.HibernateUtil;

import java.util.List;

public class UserDao extends AbstractDao<User> {

    public UserDao() {
        super(User.class);
    }

    /**
     * Recherche d'un utilisateur par email et mot de passe
     * Utilisé pour la méthode authenticate() dans UserService
     */
    public User findByEmailAndPassword(String email, String password) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("FROM User WHERE email = :email AND motDePasse = :password");
            query.setParameter("email", email);
            query.setParameter("password", password);
            user = (User) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }

    /**
     * Recherche d'un utilisateur uniquement par email (utile si tu veux vérifier existence avant création)
     */
    public User findByEmail(String email) {
        Session session = null;
        User user = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("FROM User WHERE email = :email");
            query.setParameter("email", email);
            user = (User) query.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return user;
    }
}
