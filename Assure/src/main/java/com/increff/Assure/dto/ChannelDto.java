package com.increff.Assure.dto;

import com.increff.Assure.model.data.ChannelData;
import com.increff.Assure.model.form.ChannelForm;
import com.increff.Assure.api.ChannelApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.increff.exception.ApiException;

import java.util.List;

import static com.increff.Assure.dto.dtoHelper.ChannelDtoHelper.*;
@Service
public class ChannelDto
{

    @Autowired
    private ChannelApi channelService;

    public List<ChannelData> getAll()
    {
        return convertChannelPojoListtoChannelDataList(channelService.selectAll());
    }

    public void add(ChannelForm channelForm)throws ApiException
    {
        validate(channelForm);
        channelForm = normalize(channelForm);
        channelService.add(convertChannelFormtoChannelPojo(channelForm));
    }







}
