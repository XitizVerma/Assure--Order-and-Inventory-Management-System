package com.increff.Assure.dto.dtoHelper;

import com.increff.Assure.model.form.ChannelListingUploadForm;
import com.increff.exception.ApiException;
import com.increff.model.data.ErrorData;
import com.increff.Assure.model.form.ChannelListingForm;
import com.increff.Assure.util.ValidationUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.increff.Assure.pojo.TableConstants.MAX_LIST_SIZE;
import static com.increff.Assure.util.ValidationUtil.validateListSize;
import static java.util.Objects.isNull;

public class ChannelListingDtoHelper {

    public static void checkDuplicateChannelListingFormList(List<ChannelListingForm> channelListingFormList) throws ApiException {
        HashSet<String> hashSetChannelSkuId = new HashSet<>();
        HashSet<String> hashSetClientSkuId = new HashSet<>();
        List<ErrorData> errorDataList = new ArrayList<>();

        Long row = 1L;
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            if (hashSetChannelSkuId.contains(channelListingForm.getChannelSkuId())) {
                errorDataList.add(new ErrorData(row, "Duplicate Values of ChannelSkuId"));
            }
            if (hashSetClientSkuId.contains(channelListingForm.getClientSkuId())) {
                errorDataList.add(new ErrorData(row, "Duplicate Values of ClientSkuId"));
            }

            hashSetChannelSkuId.add(channelListingForm.getChannelSkuId());
            hashSetClientSkuId.add(channelListingForm.getClientSkuId());
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);
    }

    public static void validateForm(ChannelListingUploadForm channelListingUploadForm) throws ApiException
    {
        validateListSize("Channel Listing Form", channelListingUploadForm.getChannelListingFormList(), MAX_LIST_SIZE);

        if (isNull(channelListingUploadForm.getClientId())) {
            throw new ApiException("ClientId cannot be null or empty");
        }
        if (isNull(channelListingUploadForm.getChannelId())) {
            throw new ApiException("ChannelId cannot be null or empty");
        }
        List<ChannelListingForm> channelListingFormList = channelListingUploadForm.getChannelListingFormList();
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            if (isNull(channelListingForm.getChannelSkuId()) || channelListingForm.getChannelSkuId().isEmpty()) {
                throw new ApiException("ChannelSkuId cannot be null or empty");
            }
            if (isNull(channelListingForm.getClientSkuId()) || channelListingForm.getClientSkuId().isEmpty()) {
                throw new ApiException("ClientSkuId cannot be null or empty");
            }
        }

    }

    public static void normalize(ChannelListingUploadForm channelListingUploadForm) {
        List<ChannelListingForm> channelListingFormList = channelListingUploadForm.getChannelListingFormList();
        for (ChannelListingForm channelListingForm : channelListingFormList) {
            channelListingForm.setChannelSkuId(channelListingForm.getChannelSkuId().trim().toLowerCase());
            channelListingForm.setClientSkuId(channelListingForm.getClientSkuId().trim().toLowerCase());
        }
    }
}

