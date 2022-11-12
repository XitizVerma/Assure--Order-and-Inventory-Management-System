package com.increff.Assure.controller;

import com.increff.Assure.dto.ChannelListingDto;
import com.increff.Assure.model.form.ChannelListingUploadForm;
import com.increff.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api
@RestController
public class ChannelListingController
{

    @Autowired
    private ChannelListingDto channelListingDto;

    @ApiOperation(value = "Add ChannelListings")
    @RequestMapping(path = "/channel-listings", method = RequestMethod.POST)
    public void addChannelListings(@RequestBody ChannelListingUploadForm channelListingUploadForm)throws ApiException
    {
        channelListingDto.add(channelListingUploadForm);
    }
}
