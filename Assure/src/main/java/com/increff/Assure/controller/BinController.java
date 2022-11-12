package com.increff.Assure.controller;

import com.increff.Assure.dto.BinDto;
import com.increff.exception.ApiException;
import com.increff.Assure.model.data.BinData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BinController
{

    @Autowired
    private BinDto binDto;

    @ApiOperation(value = "Create N Bins")
    @RequestMapping(path = "/bins/{numberOfBins}", method = RequestMethod.POST)
    public List<BinData> addBins(@PathVariable Long numberOfBins) throws ApiException
    {
        return binDto.add(numberOfBins);
    }

    @ApiOperation(value = "Get Bins")
    @RequestMapping(path = "/bins", method = RequestMethod.GET)
    public List<BinData> getBins()
    {
        return binDto.getAll();
    }
}
