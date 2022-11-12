package com.increff.Assure.controller;

import com.increff.model.form.OrderForm;
import com.increff.Assure.dto.OrderDto;
import com.increff.exception.ApiException;
import com.increff.Assure.model.form.OrderStatusUpdateForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.TransformerException;
import java.io.IOException;

@Api
@RestController
public class OrderController
{

    @Autowired
    private OrderDto orderDto;

    @ApiOperation(value = "Create Order")
    @RequestMapping(path = "/orders", method = RequestMethod.POST)
    public Integer addOrder(@RequestBody OrderForm orderForm)throws ApiException
    {
        return orderDto.add(orderForm);
    }

    @ApiOperation(value = "Update Order Status")
    @RequestMapping(path = "/orders/{orderId}", method = RequestMethod.PUT)
    public void updateStatus(@RequestBody OrderStatusUpdateForm orderStatusUpdateForm)throws ApiException
    {
        orderDto.updateStatus(orderStatusUpdateForm);
    }

    @ApiOperation(value = "Update Order Status")
    @RequestMapping(path = "/orders/{orderId}/allocate-order", method = RequestMethod.PUT)
    public void allocateOrder(@PathVariable Long orderId)throws ApiException
    {
        orderDto.allocateOrder(orderId);
    }


    @ApiOperation(value = "Get Invoice and FulFill Order")
    @RequestMapping(path = "/orders/{orderId}/get-invoice",method = RequestMethod.GET)
    public String getInvoice(@PathVariable Long orderId)throws ApiException, IOException, TransformerException
    {
        return orderDto.getInvoice(orderId);
    }

}
