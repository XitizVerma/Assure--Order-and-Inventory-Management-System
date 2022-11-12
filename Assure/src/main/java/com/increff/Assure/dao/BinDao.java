package com.increff.Assure.dao;

import com.increff.Assure.pojo.BinPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BinDao extends AbstractDao
{
    private final static String SELECT_BY_ID = "select p from BinPojo p where binId=:binId";

    public BinPojo add(BinPojo binPojo)
    {
        return (BinPojo) addAndReturn(binPojo);
    }

    public List<BinPojo> selectAll()
    {
        return selectAll(BinPojo.class);
    }

    public BinPojo selectById(Long binId)
    {
        TypedQuery<BinPojo> query = em().createQuery(SELECT_BY_ID,BinPojo.class);
        query.setParameter("binId",binId);
        return (BinPojo) getSingle(query);
    }

}
