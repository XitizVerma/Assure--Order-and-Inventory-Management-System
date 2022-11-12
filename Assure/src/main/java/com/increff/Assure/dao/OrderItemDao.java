package com.increff.Assure.dao;

import com.increff.Assure.pojo.OrderItemPojo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class OrderItemDao extends AbstractDao
{

    private static final String SELECT_BY_ORDER_ID = "select p from OrderItemPojo p where orderId=:orderId";
    public void add(OrderItemPojo orderItemPojo)
    {
        addAbs(orderItemPojo);
    }

    public List<OrderItemPojo> selectByOrderId(Long orderId)
    {
        TypedQuery<OrderItemPojo> query = em().createQuery(SELECT_BY_ORDER_ID,OrderItemPojo.class);
        query.setParameter("orderId",orderId);
        return getMultiple(query);
    }

}
