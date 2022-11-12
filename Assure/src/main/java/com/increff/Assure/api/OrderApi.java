package com.increff.Assure.api;

import com.increff.Assure.dao.OrderDao;
import com.increff.Assure.dao.OrderItemDao;
import com.increff.Assure.pojo.OrderItemPojo;
import com.increff.Assure.pojo.OrderPojo;
import com.increff.Assure.util.OrderStatus;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class OrderApi
{

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderItemDao orderItemDao;

    public void add(OrderPojo orderPojo, List<OrderItemPojo> orderItemPojoList)throws ApiException
    {
        orderPojo.setStatus(OrderStatus.CREATED);
        orderDao.add(orderPojo);
        Long orderId = orderPojo.getId();

        for(OrderItemPojo orderItemPojo : orderItemPojoList)
        {
            orderItemPojo.setOrderId(orderId);
            orderItemPojo.setAllocatedQuantity(0L);
            orderItemPojo.setFulfilledQuantity(0L);

            orderItemDao.add(orderItemPojo);
        }
    }

    public void updateStatus(Long id, OrderStatus orderStatus)throws ApiException
    {
        OrderPojo orderPojo = getCheck(id);
        orderPojo.setStatus(orderStatus);
        orderDao.update();
    }

    public Long allocateOrderItemQty(OrderItemPojo orderItemPojo, Long invQty)
    {
        Long allocatedQty = min(invQty,orderItemPojo.getOrderedQuantity() - orderItemPojo.getAllocatedQuantity());
        orderItemPojo.setAllocatedQuantity(allocatedQty+orderItemPojo.getAllocatedQuantity());
        return allocatedQty;
    }

    public Long fulfillQty(OrderItemPojo orderItemPojo)
    {
        orderItemPojo.setFulfilledQuantity(orderItemPojo.getAllocatedQuantity());
        orderItemPojo.setAllocatedQuantity(0L);
        orderDao.update();
        return orderItemPojo.getFulfilledQuantity();
    }

    public OrderPojo getCheck(Long orderId)throws ApiException
    {
        OrderPojo orderPojo = orderDao.selectById(orderId);
        if(isNull(orderPojo))
        {
            throw new ApiException("Order does not exist");
        }
        return orderPojo;
    }

    public OrderPojo selectByChannelIdAndChannelOrderId(Long channelId, String channelOrderId)
    {
        return orderDao.selectByChannelIdAndChannelOrderId(channelId,channelOrderId);
    }

    public List<OrderItemPojo> selectOrderItemListByOrderId(Long id)
    {
        return orderItemDao.selectByOrderId(id);
    }

    public  void setUrl(Long id, String url)
    {
        OrderPojo orderPojo = orderDao.selectById(id);
        orderPojo.setInvoiceUrl(url);
        orderDao.update();
    }
}
