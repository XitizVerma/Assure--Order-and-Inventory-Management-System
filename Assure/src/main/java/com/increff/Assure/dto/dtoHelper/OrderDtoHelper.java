package com.increff.Assure.dto.dtoHelper;

import com.increff.Assure.model.data.OrderItemData;
import com.increff.exception.ApiException;
import com.increff.model.form.OrderForm;
import com.increff.model.form.OrderItemForm;
import com.increff.Assure.pojo.OrderItemPojo;
import com.increff.Assure.pojo.OrderPojo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.increff.Assure.pojo.TableConstants.MAX_LIST_SIZE;
import static com.increff.Assure.util.ValidationUtil.validateList;
import static java.util.Objects.isNull;


public class OrderDtoHelper
{
    public static DecimalFormat df = new DecimalFormat("#.##");

    public static OrderPojo convertOrderFormtoOrderPojo(OrderForm orderForm)
    {
        OrderPojo orderPojo = new OrderPojo();
        orderPojo.setChannelOrderId(orderForm.getChannelOrderId());
        orderPojo.setChannelId(orderForm.getChannelId());
        orderPojo.setClientId(orderForm.getClientId());
        orderPojo.setCustomerId(orderForm.getCustomerId());
        return orderPojo;
    }

    public static List<OrderItemPojo> convertOrderItemFormListtoOrderItemPojoList(List<OrderItemForm> orderItemFormList, Map<String,Long> clientSkuIdtoGlobalSkuIdMap)
    {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<>();
        for(OrderItemForm orderItemForm : orderItemFormList)
        {
            OrderItemPojo orderItemPojo = new OrderItemPojo();
            orderItemPojo.setGlobalSkuId(clientSkuIdtoGlobalSkuIdMap.get(orderItemForm.getClientSkuId()));
            orderItemPojo.setOrderedQuantity(orderItemForm.getQuantity());
            orderItemPojo.setSellingPricePerUnit(orderItemForm.getSellingPricePerUnit());
            orderItemPojoList.add(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public static OrderItemData convertOrderItemPojotoOrderItemData(OrderItemPojo orderItemPojo, String clientSkuId)
    {
        OrderItemData orderItemData = new OrderItemData();
        orderItemData.setOrderId(orderItemPojo.getOrderId());
        orderItemData.setOrderedQuantity(orderItemPojo.getOrderedQuantity());
        orderItemData.setClientSkuId(clientSkuId);
        orderItemData.setSellingPricePerUnit(orderItemPojo.getSellingPricePerUnit());
        return orderItemData;

    }

    public static void validateOrderForm(OrderForm orderForm)throws ApiException
    {
        if(isNull(orderForm.getClientId()))
        {
            throw new ApiException("ClientId cannot be null or empty");
        }
        if(isNull(orderForm.getChannelOrderId()) ||orderForm.getChannelOrderId().isEmpty())
        {

            throw new ApiException("ChannelOrderId cannot be null or empty");
        }
        if(isNull(orderForm.getCustomerId()))
        {
            throw new ApiException("CustomerId cannot be null or empty");
        }
        if(isNull(orderForm.getChannelId()))
        {
            throw new ApiException("ChannelId cannot be null or empty");
        }
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        validateList("Order Item List", orderItemFormList, MAX_LIST_SIZE);
        for(OrderItemForm orderItemForm : orderItemFormList)
        {
            validateOrderItemForm(orderItemForm);
        }
    }

    private static void validateOrderItemForm(OrderItemForm orderItemForm)throws ApiException
    {
        if(isNull(orderItemForm.getClientSkuId()) || orderItemForm.getClientSkuId().isEmpty())
        {
            throw new ApiException("ClientSkuId cannot be null or empty");
        }
        if(isNull(orderItemForm.getSellingPricePerUnit()) || orderItemForm.getSellingPricePerUnit()<=0)
        {
            throw new ApiException("SellingPricePerUnit cannot be null");
        }
        if(isNull(orderItemForm.getQuantity()) || orderItemForm.getQuantity()<=0)
        {
            throw new ApiException("Quantity cannot be null or zero");
        }
    }

    public static void normalize(OrderForm orderForm)
    {
        orderForm.setChannelOrderId(orderForm.getChannelOrderId().trim().toLowerCase());
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        for(OrderItemForm orderItemForm : orderItemFormList)
        {
            normalizeItem(orderItemForm);
        }
    }
    private static void normalizeItem(OrderItemForm orderItemForm)
    {
        orderItemForm.setClientSkuId(orderItemForm.getClientSkuId().trim().toLowerCase());
        orderItemForm.setSellingPricePerUnit(Double.valueOf(df.format(orderItemForm.getSellingPricePerUnit())));

    }
}
