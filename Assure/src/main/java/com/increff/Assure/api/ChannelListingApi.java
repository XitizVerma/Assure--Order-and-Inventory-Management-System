package com.increff.Assure.api;

import com.increff.Assure.dao.ChannelListingDao;
import com.increff.exception.ApiException;
import com.increff.model.data.ErrorData;
import com.increff.Assure.pojo.ChannelListingPojo;
import com.increff.Assure.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelListingApi
{

    @Autowired
    private ChannelListingDao channelListingDao;

    public void add(List<ChannelListingPojo> channelListingPojoList)throws ApiException
    {
        checkDataNotExist(channelListingPojoList);
        for(ChannelListingPojo channelListingPojo : channelListingPojoList)
        {
            channelListingDao.add(channelListingPojo);
        }
    }

    public ChannelListingPojo selectByAllFields(ChannelListingPojo channelListingPojo)
    {
        return channelListingDao.selectByAllFields(channelListingPojo.getChannelId(),
                channelListingPojo.getChannelSkuId(),channelListingPojo.getClientId(),
                channelListingPojo.getGlobalSkuId());
    }

    public void checkDataNotExist(List<ChannelListingPojo> channelListingPojoList)throws ApiException
    {
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row =1L;
        for(ChannelListingPojo channelListingPojo : channelListingPojoList)
        {
            ChannelListingPojo exists = selectByAllFields(channelListingPojo);
            if(!isNull(exists))
            {//TODO:CHECK 7.2
                errorDataList.add(new ErrorData(row,"Channel Listing Data already exists"));
            }
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);
    }
}
