package com.increff.Assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BinSkuItemForm
{
    @NotBlank
    private String clientSkuId;
    @NotNull
    private Long binId;
    @NotNull
    private Long quantity;

}
