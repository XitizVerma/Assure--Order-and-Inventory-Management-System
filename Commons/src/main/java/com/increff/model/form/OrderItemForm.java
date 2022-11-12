package com.increff.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class OrderItemForm {

    @NotBlank
    private String clientSkuId;

    @PositiveOrZero
    private Long quantity;

    @DecimalMin(value = "0")
    private Double sellingPricePerUnit;
}
