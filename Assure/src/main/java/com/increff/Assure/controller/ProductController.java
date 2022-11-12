package com.increff.Assure.controller;

import com.increff.Assure.model.data.ProductData;
import com.increff.Assure.dto.ProductDto;
import com.increff.exception.ApiException;
import com.increff.Assure.model.form.ProductForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class ProductController
{
    @Autowired
    private ProductDto productDto;

    @ApiOperation(value = "Gets All Product Details")
    @RequestMapping(path = "/products",method = RequestMethod.GET)
    public List<ProductData> getAll()
    {
        return productDto.getAll();
    }

    @ApiOperation(value = "Get Product by GlobalSkuId")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.GET)
    public ProductData getProductById(@PathVariable Long globalSkuId)throws ApiException
    {
        return productDto.getProductById(globalSkuId);
    }

    @ApiOperation(value = "Add Bulk Product Details")
    @RequestMapping(path = "/products", method = RequestMethod.POST)
    public void bulkAdd(@RequestBody List<ProductForm> productFormList, @RequestParam Long clientId)throws ApiException
    {
        productDto.bulkAdd(productFormList,clientId);
    }

    @ApiOperation(value = "Update Product Details")
    @RequestMapping(path = "/products/{globalSkuId}", method = RequestMethod.PUT)
    public void update(@RequestBody ProductForm productForm, @PathVariable Long globalSkuId)throws ApiException
    {
        productDto.update(productForm,globalSkuId);
    }
}
