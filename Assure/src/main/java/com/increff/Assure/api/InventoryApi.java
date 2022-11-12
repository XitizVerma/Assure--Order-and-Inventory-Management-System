package com.increff.Assure.api;

import com.increff.exception.ApiException;
import com.increff.Assure.dao.InventoryDao;
import com.increff.Assure.pojo.InventoryPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class InventoryApi
{

    @Autowired
    private InventoryDao inventoryDao;

    public void add(List<InventoryPojo> inventoryPojoList)
    {
        for(InventoryPojo inventoryPojo : inventoryPojoList)
        {
            InventoryPojo exists = inventoryDao.selectByGlobalSkuId(inventoryPojo.getGlobalSkuId());
            if(isNull(exists))
            {
                inventoryPojo.setFulfilledQuanity(0L);
                inventoryPojo.setAllocatedQuantity(0L);
                inventoryDao.add(inventoryPojo);
            }
            else
            {
                exists.setAvailableQuantity(exists.getAvailableQuantity()+inventoryPojo.getAvailableQuantity());
                inventoryDao.update();
            }
        }
    }

    public InventoryPojo selectByGlobalSkuId(Long globalSkuId)
    {
        return inventoryDao.selectByGlobalSkuId(globalSkuId);
    }

    public void allocateQty(Long allocateQty, Long globalSkuId)throws ApiException
    {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuID(globalSkuId);
        inventoryPojo.setAvailableQuantity(inventoryPojo.getAvailableQuantity()-allocateQty);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity()+allocateQty);
        inventoryDao.update();

    }

    public void fulfillQty(Long fulfilledQty,Long globalSkuId)throws ApiException
    {
        InventoryPojo inventoryPojo = getCheckByGlobalSkuID(globalSkuId);
        inventoryPojo.setAllocatedQuantity(inventoryPojo.getAllocatedQuantity()-fulfilledQty);
        inventoryPojo.setFulfilledQuanity(inventoryPojo.getFulfilledQuanity()+fulfilledQty);
        inventoryDao.update();

    }

    public InventoryPojo getCheckByGlobalSkuID(Long globalSkuId)throws ApiException
    {
        InventoryPojo inventoryPojo = selectByGlobalSkuId(globalSkuId);
        if(isNull(inventoryPojo))
        {
            throw new ApiException("Inventory with globalSkuId does not exist");
        }
        return inventoryPojo;
    }

}
