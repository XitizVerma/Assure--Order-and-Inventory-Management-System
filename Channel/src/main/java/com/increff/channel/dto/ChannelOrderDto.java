package com.increff.channel.dto;

import com.increff.exception.ApiException;
import com.increff.model.form.OrderForm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelOrderDto {


    @Value("${server.Uri}")
    private String assureServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public Integer add(OrderForm orderForm) throws ApiException {
        try {
            return restTemplate.postForObject(assureServerUrl + "orders", orderForm, Integer.class);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());
        }
    }

    public String getInvoice(Long orderId) throws ApiException {

        try {
            return restTemplate.postForObject(assureServerUrl + "/channel-orders/{orderId}/get-invoice" + orderId + "/get-invoice", orderId, String.class);
        } catch (RestClientException e) {
            throw new ApiException(e.getMessage());

        }
    }


}
