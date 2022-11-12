package com.increff.Assure.dao;

import com.increff.Assure.pojo.BinSkuPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class BinSkuDao extends AbstractDao
{
    private final static String SELECT_BY_ID = "select p from BinSkuPojo p where id=:id";

    private final static String SELECT_BY_GLOBAL_SKU_ID_AND_BIN_ID = "select p from BinSkuPojo p where binId=:binId and globalSkuId=:globalSkuId";

    private final static String SELECT_BY_GLOBAL_SKU_ID = "select p from BinSkuPojo p where globalSkuId=:globalSkuId";

    public void add(BinSkuPojo binSkuPojo)
    {
        addAbs(binSkuPojo);
    }

    public List<BinSkuPojo> selectAll()
    {
        return selectAll(BinSkuPojo.class);
    }
    public BinSkuPojo selectById(Long id)
    {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_ID,BinSkuPojo.class);
        query.setParameter("id",id);
        return (BinSkuPojo) getSingle(query);
    }

    public BinSkuPojo selectByGlobalSkuIdAndBinId(Long binId,Long globalSkuId)
    {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_GLOBAL_SKU_ID_AND_BIN_ID,BinSkuPojo.class);
        query.setParameter("binId",binId);
        query.setParameter("globalSkuId",globalSkuId);
        return (BinSkuPojo) getSingle(query);

    }
    public List<BinSkuPojo> selectByGlobalSkuId(Long globalSkuId)
    {
        TypedQuery<BinSkuPojo> query = em().createQuery(SELECT_BY_GLOBAL_SKU_ID,BinSkuPojo.class);
        query.setParameter("globalSkuId",globalSkuId);
        return getMultiple(query);
    }
}
