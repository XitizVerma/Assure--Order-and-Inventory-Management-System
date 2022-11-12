package com.increff.Assure.model.data;

import com.increff.Assure.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelData
{
    private Long id;
    private String name;
    private InvoiceType invoiceType;

}
