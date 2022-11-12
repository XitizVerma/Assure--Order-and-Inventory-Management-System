package com.increff.Assure.api;

import com.increff.model.data.ErrorData;
import com.increff.Assure.pojo.ProductPojo;
import com.increff.Assure.util.ValidationUtil;
import com.increff.Assure.dao.ProductDao;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class ProductApi {

    @Autowired
    private ProductDao productDao;

    @Transactional(readOnly = true)
    public List<ProductPojo> selectAll()
    {
        return productDao.selectAll();
    }

    public ProductPojo selectById(Long id)throws ApiException
    {
        return getCheckId(id);
    }

    public ProductPojo getCheckId(Long id)throws ApiException
    {
        ProductPojo productPojo = productDao.selectById(id);
        if(isNull(productPojo))
        {
            throw new ApiException("GlobalSkuid does not exist " + id);
        }
        return productPojo;
    }

    public void bulkAdd(List<ProductPojo> productPojoList)throws ApiException
    {
        Long clientId = productPojoList.get(0).getClientId();
        List<ErrorData> errorDataList = new ArrayList<>();
        List<ProductPojo> productPojoByClientList = productDao.selectByClientId(clientId);
        Set<String> clientSkuIdSet = new HashSet<>();
        for(ProductPojo productPojo : productPojoByClientList)
        {
            clientSkuIdSet.add(productPojo.getClientSkuId());
        }
        Long row =1L;
        for(ProductPojo productPojo : productPojoList)
        {
            if(clientSkuIdSet.contains(productPojo.getClientSkuId()))
            {
                errorDataList.add(new ErrorData(row,"ClientSkuId - ClientId pair already exists ClientID : " + clientId + " , ClientSkuId " + productPojo.getClientSkuId()));
            }
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);

        for(ProductPojo productPojo : productPojoList)
        {
            productDao.add(productPojo);
        }
    }

    public void update(ProductPojo productPojo,Long globalSkuId)throws ApiException
    {
        ProductPojo productPojo2 = getCheckId(globalSkuId);
        productPojo2.setClientSkuId(productPojo.getClientSkuId());
        productPojo2.setClientId(productPojo.getClientId());
        productPojo2.setName(productPojo.getName());
        productPojo2.setBrandId(productPojo.getBrandId());
        productPojo2.setMrp(productPojo.getMrp());
        productPojo2.setDescription(productPojo.getDescription());
    }

    public ProductPojo selectByClientSkuIdandClientId(String clientSkuId, Long clientId)
    {
        return productDao.selectByClientSkuIdandClientId(clientSkuId,clientId);
    }

}
