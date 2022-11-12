package com.increff.Assure.model.form;

import com.increff.Assure.util.InvoiceType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ChannelForm
{
    @NotBlank
    private String name;

    @NotNull
    private InvoiceType invoiceType;

}
