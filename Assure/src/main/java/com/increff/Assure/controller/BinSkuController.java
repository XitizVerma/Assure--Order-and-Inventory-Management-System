package com.increff.Assure.controller;

import com.increff.Assure.dto.BinSkuDto;
import com.increff.Assure.model.data.BinSkuData;
import com.increff.Assure.model.form.BinSkuUpdateForm;
import com.increff.exception.ApiException;
import com.increff.Assure.model.form.BinSkuForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class BinSkuController
{

    @Autowired
    private BinSkuDto binSkuDto;


    @ApiOperation(value = "Add BinSkus")
    @RequestMapping(path = "/bin-skus", method = RequestMethod.POST)
    public Integer addBinSku(@RequestBody BinSkuForm binSkuForm)throws ApiException
    {
        return binSkuDto.add(binSkuForm);
    }

    @ApiOperation(value = "Get BinSkus")
    @RequestMapping(path = "/bin-skus", method = RequestMethod.GET)
    public List<BinSkuData> getBinSku()
    {
        return binSkuDto.select();
    }

    @ApiOperation(value = "Update BinSku by id")
    @RequestMapping(path = "/bin-skus/{id}", method = RequestMethod.PUT)
    public void updateBinSku(@PathVariable Long id, @RequestParam BinSkuUpdateForm binSkuUpdateForm)throws ApiException
    {
        binSkuDto.update(binSkuUpdateForm,id);
    }

}
