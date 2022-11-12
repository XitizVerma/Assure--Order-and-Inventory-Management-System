package com.increff.Assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
public class BinSkuUpdateForm
{
    @PositiveOrZero
    private Long quantity;
}
