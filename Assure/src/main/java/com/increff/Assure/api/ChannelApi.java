package com.increff.Assure.api;

import com.increff.Assure.pojo.ChannelPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.increff.Assure.dao.ChannelDao;
import com.increff.exception.ApiException;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ChannelApi
{

    @Autowired
    private ChannelDao channelDao;

    @Transactional(readOnly = true)
    public List<ChannelPojo> selectAll()
    {
        return channelDao.selectAll();
    }

    public void add(ChannelPojo channelPojo)throws ApiException
    {
        getCheckChannelExists(channelPojo);
        channelDao.add(channelPojo);
    }
    public void getCheckChannelExists(ChannelPojo channelPojo)throws ApiException
    {
        if(isNull(channelDao.selectByChannel(channelPojo.getName()))==false)
        {
            throw new ApiException("Channel Already exists, channel = " + channelPojo.getName());
        }
    }

    public ChannelPojo selectByChannelName(String name)
    {
        return channelDao.selectByChannel(name);
    }

    public void getCheck(Long id)throws ApiException
    {
        if(isNull(channelDao.selectById(id)))
        {
            throw new ApiException("Channel does not exist");
        }
    }

}
