package com.increff.Assure.model.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class ChannelListingUploadForm
{
    //Taking Client and Channel Id instead of Client Name and Channel Name as UserType and Invoice Types Enums
    //will also be required fro Unique Combination Check.
    @NotNull
    Long clientId;

    @NotNull
    Long channelId;

    @NotNull
    List<@NotNull ChannelListingForm> channelListingFormList;
}
