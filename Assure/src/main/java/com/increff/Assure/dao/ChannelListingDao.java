package com.increff.Assure.dao;

import com.increff.Assure.pojo.ChannelListingPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Repository
@Transactional
public class ChannelListingDao extends AbstractDao{

    private final static String SELECT_BY_ALL_FIELDS = "select p from ChannelListingPojo p where channelId=:channelId " +
            "and channelSkuId=:channelSkuId and clientId=:clientId and globalSkuId=:globalSkuId";

    public void add(ChannelListingPojo channelListingPojo)
    {
        addAbs(channelListingPojo);
    }


    public ChannelListingPojo selectByAllFields(Long channelId, String channelSkuId, Long clientId, Long globalSkuId)
    {
        TypedQuery<ChannelListingPojo> query = em().createQuery(SELECT_BY_ALL_FIELDS,ChannelListingPojo.class);
        query.setParameter("clientId",clientId);
        query.setParameter("globalSkuId",globalSkuId);
        query.setParameter("channelId",channelId);
        query.setParameter("channelSkuId",channelSkuId);
        return (ChannelListingPojo) getSingle(query);
    }


}
