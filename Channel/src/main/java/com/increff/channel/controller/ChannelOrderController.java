package com.increff.channel.controller;

import com.increff.exception.ApiException;
import com.increff.model.form.OrderForm;
import com.increff.channel.dto.ChannelOrderDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api
@RestController
public class ChannelOrderController
{
    @Autowired
    ChannelOrderDto channelOrderDto;

    @ApiOperation(value = "Push the order via this channel")
    @RequestMapping(value = "/channel-orders", method = RequestMethod.POST)
    public Integer addOrder(@RequestBody OrderForm orderForm) throws ApiException {
        return channelOrderDto.add(orderForm);
    }

    @ApiOperation(value = "get invoice")
    @RequestMapping(value = "/channel-orders/{orderId}/get-invoice", method = RequestMethod.POST)
    public String getInvoice(@PathVariable Long orderId) throws ApiException {
        return channelOrderDto.getInvoice(orderId);
    }
}

