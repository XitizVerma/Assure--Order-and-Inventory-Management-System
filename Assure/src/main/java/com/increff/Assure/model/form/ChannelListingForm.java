package com.increff.Assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ChannelListingForm
{
    @NotBlank
    private String clientSkuId;

    @NotBlank
    private String channelSkuId;
}
