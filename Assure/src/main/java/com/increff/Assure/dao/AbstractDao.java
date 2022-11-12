package com.increff.Assure.dao;

import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import java.util.List;

@Repository
public abstract class AbstractDao<T> {

    @PersistenceContext
    protected EntityManager em;

    public <T> T select(Class<T> pojo,Integer id)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cr = cb.createQuery(pojo);
        Root<T> root = cr.from(pojo);
        cr.select(root).where(cb.equal(root.get("id"),id));

        TypedQuery<T> query =em.createQuery(cr);
        return getSingle(query);
    }

    public <T> List<T> selectAll(Class<T> pojo)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(pojo);
        Root<T> root = cq.from(pojo);
        cq.select(root);

        TypedQuery <T> query = em.createQuery(cq);
        List<T> results = query.getResultList();
        return results;
    }

    public <T> void addAbs(T pojoObject)
    {
        em.persist(pojoObject);
    }

    public <T> T addAndReturn (T pojoObject)
    {
        em.persist(pojoObject);
        return pojoObject;
    }

    public void update()
    {
        //SYMBOLIC
    }
    public <T> T getSingle(TypedQuery <T> query)
    {
        return query.getResultList().stream().findFirst().orElse(null);
    }

    public <T> List <T> getMultiple(TypedQuery<T> query)
    {
        return query.getResultList();
    }

    protected EntityManager em() {
        return em;
    }

}
