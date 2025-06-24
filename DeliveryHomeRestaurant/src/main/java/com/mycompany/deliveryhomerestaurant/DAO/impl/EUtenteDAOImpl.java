package com.mycompany.deliveryhomerestaurant.DAO.impl;

import com.mycompany.deliveryhomerestaurant.DAO.EUtenteDAO;
import com.mycompany.deliveryhomerestaurant.Model.EUtente;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;

public class EUtenteDAOImpl implements EUtenteDAO {

    private final EntityManager em;

    public EUtenteDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public EUtente findByUsername(String email) {
        try {
            return em.createQuery("FROM EUtente u WHERE u.email = :email", EUtente.class)
                     .setParameter("email", email)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(EUtente user) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.merge(user); 
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
    
    @Override
    public EUtente findById(int id){
         try {
            return em.createQuery("FROM EUtente u WHERE u.id = :id", EUtente.class)
                     .setParameter("id", id)
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
        
    }
}
