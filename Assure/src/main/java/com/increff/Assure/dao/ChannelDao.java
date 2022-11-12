package com.increff.Assure.dao;

import com.increff.Assure.pojo.ChannelPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ChannelDao extends AbstractDao {

   private final static String SELECT_BY_INVOICE = "select p from ChannelPojo p where invoiceType=:invoiceType";

    private final static String SELECT_BY_ID = "select p from ChannelPojo p where id =:id";

    private final static String SELECT_BY_CHANNEL = "select p from ChannelPojo p where name=:name";

    public List<ChannelPojo> selectAll() {
        return selectAll(ChannelPojo.class);
    }

    public ChannelPojo selectByChannel(String name) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_CHANNEL, ChannelPojo.class);
        query.setParameter("name", name);
        return (ChannelPojo) getSingle(query);

    }

    public ChannelPojo selectById(Long id) {
        TypedQuery<ChannelPojo> query = em().createQuery(SELECT_BY_ID, ChannelPojo.class);
        query.setParameter("id", id);
        return (ChannelPojo) getSingle(query);

    }

    public void add(ChannelPojo channelPojo)
    {
        addAbs(channelPojo);
    }

}
