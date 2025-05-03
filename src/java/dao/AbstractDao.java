package dao;

import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;

import java.util.List;

public abstract class AbstractDao<T> implements IDao<T> {

    private final Class<T> entityClass;

    public AbstractDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public boolean create(T o) {
        return executeTransaction(session -> session.save(o));
    }

    @Override
    public boolean delete(T o) {
        return executeTransaction(session -> session.delete(o));
    }

    @Override
    public boolean update(T o) {
        return executeTransaction(session -> session.update(o));
    }

    @Override
    public List<T> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<T> result = null;
        try {
            result = session.createQuery("FROM " + entityClass.getSimpleName()).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public T findById(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        T entity = null;
        try {
            entity = (T) session.get(entityClass, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return entity;
    }

    private boolean executeTransaction(HibernateOperation<T> operation) {
        Session session = null;
        Transaction tx = null;
        boolean success = false;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            operation.execute(session);
            tx.commit();
            success = true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }

        return success;
    }

    @FunctionalInterface
    private interface HibernateOperation<T> {
        void execute(Session session);
    }
}
