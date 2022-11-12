package com.increff.Assure.controller;

import com.increff.Assure.dto.ChannelDto;
import com.increff.Assure.model.data.ChannelData;
import com.increff.Assure.model.form.ChannelForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(value = "/api/")
public class ChannelController
{
    @Autowired
    private ChannelDto channelDto;

    @ApiOperation(value = "Get Channels")
    @RequestMapping(path = "/channels",method = RequestMethod.GET)
    public List<ChannelData> getChannels()
    {
        return channelDto.getAll();
    }

    @ApiOperation(value="Add Channel")
    @RequestMapping(path="/channels", method = RequestMethod.POST)
    public void addChannel(@RequestBody ChannelForm channelForm)throws ApiException
    {
        channelDto.add(channelForm);
    }
}
