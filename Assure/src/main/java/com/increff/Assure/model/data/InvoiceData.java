package com.increff.Assure.model.data;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class InvoiceData
{
    private String invoiceGenerationTime;
    private Long orderId;
    private List<OrderItemData> orderItemDataList;
    private Double total;

    public InvoiceData()
    {

    }

    public InvoiceData(ZonedDateTime invoiceGenerationTime, Long orderId, List<OrderItemData> orderItemDataList, Double total)
    {
        this.invoiceGenerationTime = invoiceGenerationTime.toLocalDate().toString()+ " " +invoiceGenerationTime.toLocalTime().toString();
        this.orderId = orderId;
        this.orderItemDataList = orderItemDataList;
        this.total = total;
    }
}
