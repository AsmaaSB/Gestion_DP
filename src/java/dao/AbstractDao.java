/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public abstract class AbstractDao<T> {
    private final EntityManagerFactory emf;
    private final Class<T> entityClass;
    
    public AbstractDao(Class<T> entityClass) {
        this.emf = Persistence.createEntityManagerFactory("ExpenseManagerPU");
        this.entityClass = entityClass;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public void create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void update(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public void delete(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.merge(entity));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    public T findById(Object id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(entityClass, id);
        } finally {
            em.close();
        }
    }
    
    public List<T> findAll() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e");
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
    public int count() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT COUNT(e) FROM " + entityClass.getSimpleName() + " e");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}