package com.increff.Assure.dto;

import com.increff.Assure.pojo.ChannelListingPojo;
import com.increff.Assure.api.ChannelApi;
import com.increff.Assure.api.ChannelListingApi;
import com.increff.Assure.api.ProductApi;
import com.increff.Assure.api.UserApi;
import com.increff.exception.ApiException;
import com.increff.model.data.ErrorData;
import com.increff.Assure.model.form.ChannelListingForm;
import com.increff.Assure.model.form.ChannelListingUploadForm;
import com.increff.Assure.pojo.ProductPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.increff.Assure.dto.dtoHelper.ChannelListingDtoHelper.*;
import static com.increff.Assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static com.increff.Assure.util.ValidationUtil.validateListSize;
import static java.util.Objects.isNull;

@Service
public class ChannelListingDto
{

    private static final Long MAX_LIST_SIZE = 1000L;

    @Autowired
    private ChannelListingApi channelListingApi;

    @Autowired
    private UserApi userApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private ChannelApi channelApi;

    @Transactional(rollbackFor = ApiException.class)
    public Integer add(ChannelListingUploadForm channelListingUploadForm)throws ApiException
    {
        validateForm(channelListingUploadForm);
        checkDuplicateChannelListingFormList(channelListingUploadForm.getChannelListingFormList());
        normalize(channelListingUploadForm);
        Long clientId = channelListingUploadForm.getClientId();
        Long channelId = channelListingUploadForm.getChannelId();
        userApi.getCheck(clientId);
        channelApi.getCheck(channelId);

        channelListingApi.add(convertChannelListingFormToPojo(clientId,channelId,channelListingUploadForm.getChannelListingFormList()));
        return channelListingUploadForm.getChannelListingFormList().size();

    }

    private List<ChannelListingPojo> convertChannelListingFormToPojo(Long clientId, Long channelId, List<ChannelListingForm> channelListingFormList)throws ApiException
    {
        List<ChannelListingPojo> channelListingPojoList = new ArrayList<>();
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;
        for(ChannelListingForm channelListingForm : channelListingFormList)
        {
            ChannelListingPojo channelListingPojo = new ChannelListingPojo();
            ProductPojo productPojo = productApi.selectByClientSkuIdandClientId(channelListingForm.getClientSkuId(), clientId);
            if(isNull(productPojo))
            {
                errorDataList.add(new ErrorData(row,"Client Sku Id does not exist"));
                continue;
            }
            channelListingPojo.setChannelId(channelId);
            channelListingPojo.setChannelSkuId(channelListingForm.getChannelSkuId());
            channelListingPojo.setClientId(clientId);
            channelListingPojo.setGlobalSkuId(productPojo.getGlobalSkuId());
            channelListingPojoList.add(channelListingPojo);
            row++;
        }
        throwErrorIfNotEmpty(errorDataList);
        return channelListingPojoList;
    }

}
