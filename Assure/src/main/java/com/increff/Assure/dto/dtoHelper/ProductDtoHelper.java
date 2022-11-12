package com.increff.Assure.dto.dtoHelper;

import com.increff.model.data.ErrorData;
import com.increff.Assure.model.data.ProductData;
import com.increff.Assure.model.form.ProductForm;
import com.increff.Assure.pojo.ProductPojo;
import com.increff.Assure.util.ValidationUtil;
import com.increff.exception.ApiException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static java.util.Objects.isNull;

public class ProductDtoHelper
{
    public static DecimalFormat df = new DecimalFormat("#.##");

    public static List<ProductData> convertProductPojoListtoProductDataList(List<ProductPojo> productPojoList)
    {
        List<ProductData> productDataList = new ArrayList<>();
        for(ProductPojo productPojo : productPojoList)
        {
            productDataList.add(convertProductPojotoProductData(productPojo));
        }
        return productDataList;
    }

    public static ProductData convertProductPojotoProductData(ProductPojo productPojo)
    {
        ProductData productData = new ProductData();
        productData.setGlobalSkuId(productPojo.getGlobalSkuId());
        productData.setClientSkuId(productPojo.getClientSkuId());
        productData.setClientId(productPojo.getClientId());
        productData.setName(productPojo.getName());
        productData.setBrandId(productPojo.getBrandId());
        productData.setMrp(productPojo.getMrp());
        productData.setDescription(productPojo.getDescription());
        return productData;
    }



    public static List<ProductPojo> convertProductFormListtoProductPojoList(List<ProductForm> productFormList, Long clientId)
    {
        List<ProductPojo> productPojoList = new ArrayList<>();
        for(ProductForm productForm : productFormList)
        {
            productPojoList.add(convertProductFormtoProductPojo(productForm,clientId));
        }
        return productPojoList;
    }

    public static ProductPojo convertProductFormtoProductPojo(ProductForm productForm,Long clientId)
    {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setClientId(clientId);
        productPojo.setClientSkuId(productForm.getClientSkuId());
        productPojo.setName(productForm.getName());
        productPojo.setBrandId(productForm.getBrandId());
        productPojo.setMrp(productForm.getMrp());
        productPojo.setDescription(productForm.getDescription());
        return productPojo;
    }

    public static void checkDuplicateProducts(List<ProductForm> productFormList)throws ApiException
    {
        //Comparing bulkUploads via storing clientSkuid in a HashSet
        HashSet<String> hashSet = new HashSet<>();
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;
        for(ProductForm productForm : productFormList)
        {
            if(hashSet.contains(productForm.getClientSkuId()))
            {
                errorDataList.add(new ErrorData(row,"Duplicate values of ClientSkuid " + productForm.getClientSkuId()));
            }
            hashSet.add(productForm.getClientSkuId());
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);
    }


    public static void validateList(List<ProductForm> productFormList,Long MAX_LIST_SIZE)throws ApiException
    {
        ValidationUtil.validateListSize("ProductForm",productFormList,MAX_LIST_SIZE);
        for(ProductForm productForm : productFormList)
        {
            validate(productForm);
        }
    }

    public static void normalizeList(List<ProductForm> productFormList)
    {
        for(ProductForm productForm : productFormList)
        {
            normalize(productForm);
        }
    }
    public static void validate(ProductForm productForm)throws ApiException
    {
        if(isNull(productForm.getClientSkuId()) || productForm.getClientSkuId().isEmpty())
        {
            throw new ApiException("ClientSkuId cannot be null or empty");
        }
        if(isNull(productForm.getName()) || productForm.getName().isEmpty())
        {
            throw new ApiException("Productname cannot be null or empty");
        }
        if(isNull(productForm.getBrandId()) || productForm.getBrandId().isEmpty())
        {
            throw new ApiException("BrandId cannot be null or empty");
        }
        if(isNull(productForm.getMrp()) || !(productForm.getMrp()>0))
        {
            throw new ApiException("Mrp cannot be null or less than 0.0");
        }
        if(isNull(productForm.getDescription()) || productForm.getDescription().isEmpty())
        {
            throw new ApiException("Description cannot be null or empty");
        }


    }

    public static ProductForm normalize(ProductForm productForm)
    {
        productForm.setClientSkuId(productForm.getClientSkuId().trim().toLowerCase());
        productForm.setName(productForm.getName().trim().toLowerCase());
        productForm.setBrandId(productForm.getBrandId().trim().toLowerCase());
        productForm.setDescription(productForm.getDescription().trim().toLowerCase());
        productForm.setMrp(Double.valueOf(df.format(productForm.getMrp())));
        return productForm;
    }


}
