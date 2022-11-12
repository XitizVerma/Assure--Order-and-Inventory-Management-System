package com.increff.Assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class ProductForm
{
    @NotBlank
    private String clientSkuId;

    @NotBlank
    private String name;

    @NotBlank
    private String brandId;

    @PositiveOrZero
    private Double mrp;

    @NotBlank
    private String description;
}
