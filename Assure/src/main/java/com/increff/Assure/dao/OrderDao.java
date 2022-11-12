package com.increff.Assure.dao;

import com.increff.Assure.pojo.OrderPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class OrderDao extends AbstractDao
{
    private static final String SELECT_BY_ORDER_ID = "select p from OrderPojo p where id=:id";

    private static final String SELECT_BY_CHANNEL_ID_AND_CHANNEL_ORDER_ID = "select p from OrderPojo p" +
            " where channelId=:channelId and channelOrderId=:channelOrderId";
    public void add(OrderPojo orderPojo)
    {
        addAbs(orderPojo);
    }

    public OrderPojo selectById(Long id)
    {
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_ORDER_ID, OrderPojo.class);
        query.setParameter("id",id);
        return (OrderPojo) getSingle(query);
    }

    public List<OrderPojo> selectAll()
    {
        return selectAll(OrderPojo.class);
    }

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId)
    {
        TypedQuery<OrderPojo> query = em().createQuery(SELECT_BY_CHANNEL_ID_AND_CHANNEL_ORDER_ID, OrderPojo.class);
        query.setParameter("channelId",channelId);
        query.setParameter("channelOrderId",channelOrderId);
        return (OrderPojo) getSingle(query);

    }
}
