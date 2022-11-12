package com.increff.Assure.dto;

import com.google.common.collect.ImmutableMap;
import com.increff.Assure.api.*;
import com.increff.model.data.ErrorData;
import com.increff.Assure.model.data.InvoiceData;
import com.increff.Assure.model.data.OrderItemData;
import com.increff.model.form.OrderForm;
import com.increff.model.form.OrderItemForm;
import com.increff.Assure.model.form.OrderStatusUpdateForm;
import com.increff.Assure.pojo.*;
import com.increff.Assure.util.InvoiceUtil;
import com.increff.Assure.util.OrderStatus;
import com.increff.Assure.util.UserType;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.*;

import static com.increff.Assure.dto.dtoHelper.OrderDtoHelper.*;
import static com.increff.Assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static com.increff.Assure.util.ValidationUtil.validateList;
import static java.util.Objects.isNull;


@Transactional(rollbackFor = ApiException.class)
@Service
public class OrderDto {

    private final RestTemplate restTemplate = new RestTemplate();

    private final static Long MAX_LIST_SIZE = 1000L;

    private final static String INTERNAL_CHANNEL = "internal";

    private final static Map<OrderStatus, OrderStatus> validStatusUpdateMap =
            ImmutableMap.<OrderStatus, OrderStatus>builder()
                    .put(OrderStatus.CREATED, OrderStatus.ALLOCATED).put(OrderStatus.ALLOCATED, OrderStatus.FULFILLED).build();

    @Autowired
    private OrderApi orderApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ChannelApi channelApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private InventoryApi inventoryApi;

    @Autowired
    private BinSkuApi binSkuApi;

    public Integer add(OrderForm orderForm) throws ApiException {
        List<OrderItemForm> orderItemFormList = orderForm.getOrderItemFormList();
        validateOrderForm(orderForm);
        normalize(orderForm);
        checkDuplicateClientSkuIds(orderItemFormList);

        //TODO: Make all throw Api Exception
        ChannelPojo channelPojo = channelApi.selectByChannelName(INTERNAL_CHANNEL);
        if (isNull(channelPojo)) {
            throw new ApiException(INTERNAL_CHANNEL + " channel does not exist");
        }

        Long channelId = channelPojo.getId();
        String channelOrderId = orderForm.getChannelOrderId();
        checkChannelIdAndChannelPairNotExist(channelId, channelOrderId);

        Map<String, Long> clientSkuIdtoGlobalSkuIdMap = getCheckClientSkuIdGlobalSkuIdMap(orderItemFormList, orderForm.getClientId());
        OrderPojo orderPojo = convertOrderFormtoOrderPojo(orderForm);
        List<OrderItemPojo> orderItemPojoList = convertOrderItemFormListtoOrderItemPojoList(orderItemFormList, clientSkuIdtoGlobalSkuIdMap);
        orderApi.add(orderPojo, orderItemPojoList);
        return orderItemFormList.size();
    }

    public void updateStatus(OrderStatusUpdateForm orderStatusUpdateForm) throws ApiException {
        OrderPojo orderPojo = orderApi.getCheck(orderStatusUpdateForm.getOrderId());

        if (validStatusUpdateMap.get(orderPojo.getStatus()) != orderStatusUpdateForm.getUpdateStatusTo()) {
            throw new ApiException("Invalid Order Update Status");
        }
        OrderStatus orderStatus = validStatusUpdateMap.get(orderPojo.getStatus());
        switch (orderStatus) {
            case ALLOCATED:
                allocateOrder(orderStatusUpdateForm.getOrderId());
                break;
            case FULFILLED:
                fulfillOrder(orderStatusUpdateForm.getOrderId());
                break;
        }
    }

    public static void checkDuplicateClientSkuIds(List<OrderItemForm> orderItemFormList) throws ApiException {
        HashSet<String> hashSetClientSkuId = new HashSet<>();
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;
        for (OrderItemForm orderItemForm : orderItemFormList) {
            if (hashSetClientSkuId.contains(orderItemForm.getClientSkuId())) {
                errorDataList.add(new ErrorData(row, "Duplicate clientSkuId"));
            }
            hashSetClientSkuId.add(orderItemForm.getClientSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorDataList);
    }

    private Map<String, Long> getCheckClientSkuIdGlobalSkuIdMap(List<OrderItemForm> orderItemFormList, Long clientId) throws ApiException {
        Map<String, Long> clientSkuIdToGlobalSkuIdMap = new HashMap<>();
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;

        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productApi.selectByClientSkuIdandClientId(orderItemForm.getClientSkuId(), clientId);
            if (isNull(productPojo)) {
                errorDataList.add(new ErrorData(row, "clientSkuId does not exist"));
                continue;
            }
            clientSkuIdToGlobalSkuIdMap.put(orderItemForm.getClientSkuId(), productPojo.getGlobalSkuId());
            row++;
        }
        throwErrorIfNotEmpty(errorDataList);

        return clientSkuIdToGlobalSkuIdMap;
    }

    private void checkChannelIdAndChannelPairNotExist(Long channelId, String channelOrderId) throws ApiException {
        if (!isNull(orderApi.selectByChannelIdAndChannelOrderId(channelId, channelOrderId))) {
            throw new ApiException("Channel Order Id already exists for this Channel");
        }
    }

    public void allocateOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderApi.getCheck(id);

        List<OrderItemPojo> orderItemPojoList = orderApi.selectOrderItemListByOrderId(id);
        Map<OrderItemPojo, InventoryPojo> orderItemPojoInventoryQtyMap = getOrderItemPojoInvQtyMap(orderItemPojoList);
        Long countOfFullyAllocatedItems = 0L;

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long invQty = orderItemPojoInventoryQtyMap.get(orderItemPojo).getAvailableQuantity();
            Long allocatedQty = orderApi.allocateOrderItemQty(orderItemPojo, invQty);
            inventoryApi.allocateQty(allocatedQty, orderItemPojo.getGlobalSkuId());
            binSkuApi.allocateQty(allocatedQty, orderItemPojo.getGlobalSkuId());

            if (orderItemPojo.getOrderedQuantity() == orderItemPojo.getAllocatedQuantity())
                countOfFullyAllocatedItems++;
        }
        if (countOfFullyAllocatedItems == orderItemPojoList.size())
            orderApi.updateStatus(id, OrderStatus.ALLOCATED);
    }

    public void fulfillOrder(Long id) throws ApiException {
        OrderPojo orderPojo = orderApi.getCheck(id);
        List<OrderItemPojo> orderItemPojoList = orderApi.selectOrderItemListByOrderId(id);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            Long fulfilledQty = orderApi.fulfillQty(orderItemPojo);
            inventoryApi.fulfillQty(fulfilledQty, orderItemPojo.getGlobalSkuId());

        }
        orderApi.updateStatus(id, OrderStatus.FULFILLED);
    }


    public Map<OrderItemPojo, InventoryPojo> getOrderItemPojoInvQtyMap(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        Map<OrderItemPojo, InventoryPojo> orderItemPojoInventoryPojoMap = new HashMap<>();
        List<ErrorData> errorDataList = new ArrayList<>();

        Long row = 1L;

        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            InventoryPojo inventoryPojo = inventoryApi.selectByGlobalSkuId(orderItemPojo.getGlobalSkuId());
            if (isNull(inventoryPojo)) {
                errorDataList.add(new ErrorData(row, "Inventory for orderItem does not exist"));
            } else
                orderItemPojoInventoryPojoMap.put(orderItemPojo, inventoryPojo);
            row++;
        }
        throwErrorIfNotEmpty(errorDataList);
        return orderItemPojoInventoryPojoMap;
    }


    public String getInvoice(Long orderId) throws ApiException, IOException, TransformerException {

        OrderPojo orderPojo = orderApi.getCheck(orderId);
        if(orderPojo.getStatus()==OrderStatus.ALLOCATED)
            fulfillOrder(orderId);
        orderPojo = orderApi.getCheck(orderId);
        if (orderPojo.getStatus() != OrderStatus.FULFILLED) {
            throw new ApiException("Order should be fulfilled for invoice generation");
        }

        if (!isNull(orderPojo.getInvoiceUrl())) {
            return orderPojo.getInvoiceUrl();
        }
        String url = null;
        Long internalChannelId = channelApi.selectByChannelName(INTERNAL_CHANNEL).getId();
        if (orderPojo.getChannelId().equals(internalChannelId)) {
            url = createPDFAndGetUrl(orderId);
        } else {
            url = createPDFAndGetUrl(orderId);

        }
        orderApi.setUrl(orderId, url);
        return url;
    }

    private String createPDFAndGetUrl(Long orderId) throws ApiException, IOException, TransformerException {
        OrderPojo orderPojo = orderApi.getCheck(orderId);
        List<OrderItemPojo> orderItemPojoList = orderApi.selectOrderItemListByOrderId(orderId);
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            String clientSkuId = productApi.selectById(orderItemPojo.getGlobalSkuId()).getClientSkuId();
            OrderItemData orderItemData = convertOrderItemPojotoOrderItemData(orderItemPojo, clientSkuId);
            orderItemDataList.add(orderItemData);
        }

        ZonedDateTime time = orderPojo.getCreatedAt();
        Double total = 0.0;
        for (OrderItemData orderItemData : orderItemDataList) {
            total += orderItemData.getOrderedQuantity() * orderItemData.getSellingPricePerUnit();
        }

        InvoiceData invoiceData = new InvoiceData(time, orderId, orderItemDataList, total);

        String xml = InvoiceUtil.jaxbObjectToXML(invoiceData);
        File xsltFile = new File("src", "invoice.xml");
        File pdfFile = new File("src", "invoice.pdf");
        System.out.println(xml);
        InvoiceUtil.convertToPDF(invoiceData, xsltFile, pdfFile, xml);
        return null;

    }
}
