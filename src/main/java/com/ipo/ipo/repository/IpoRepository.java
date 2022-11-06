package com.ipo.ipo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import com.ipo.ipo.domain.Ipo;
import org.springframework.stereotype.Repository;

@Repository
public class IpoRepository {
    
    private final EntityManager em;

    public IpoRepository(EntityManager em) {
        this.em = em;
    }

    public void save(Ipo ipo) {
        em.persist(ipo);
    }

    public void delete(Long id) {
        Ipo ipo = em.find(Ipo.class, id);
        em.remove(ipo);
    }

    public Ipo findOne(Long ipoId) {
        return em.find(Ipo.class, ipoId);
    }

    public List<Ipo> findAll() {
        return em.createQuery("select i from Ipo i", Ipo.class)
                .getResultList();
    }

    public Ipo findByIpoStockId(Long ipoStockId) {
        return em.createQuery("select i from Ipo i where i.ipoStockId = :ipoStockId", Ipo.class)
                .setParameter("ipoStockId", ipoStockId)
                .getSingleResult();
    }

}
