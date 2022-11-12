package com.increff.Assure.dto;

import com.increff.Assure.api.ProductApi;
import com.increff.Assure.api.UserApi;
import com.increff.Assure.dto.dtoHelper.ProductDtoHelper;
import com.increff.Assure.model.data.ProductData;
import com.increff.Assure.model.form.ProductForm;
import com.increff.Assure.pojo.TableConstants;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDto
{
    //TODO:Make a flow api

    @Autowired
    private ProductApi productService;

    @Autowired
    private UserApi userService;

    public List<ProductData>getAll()
    {
        return ProductDtoHelper.convertProductPojoListtoProductDataList(productService.selectAll());
    }

    public ProductData getProductById(Long id)throws ApiException
    {
        return ProductDtoHelper.convertProductPojotoProductData(productService.selectById(id));
    }

    public void bulkAdd(List<ProductForm> productFormList, Long clientId)throws ApiException
    {
        ProductDtoHelper.validateList(productFormList, TableConstants.MAX_LIST_SIZE);
        ProductDtoHelper.normalizeList(productFormList);

        ProductDtoHelper.checkDuplicateProducts(productFormList);
        userService.getCheck(clientId);
        productService.bulkAdd(ProductDtoHelper.convertProductFormListtoProductPojoList(productFormList,clientId));
    }

    public void update(ProductForm productForm,Long  globalSkuId)throws ApiException
    {
        ProductDtoHelper.validate(productForm);
        productForm = ProductDtoHelper.normalize(productForm);
        Long clientId = productService.selectById(globalSkuId).getClientId();
        productService.update(ProductDtoHelper.convertProductFormtoProductPojo(productForm,clientId),globalSkuId);

    }



}
