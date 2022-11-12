package com.increff.Assure.dto;

import com.increff.Assure.api.*;
import com.increff.Assure.model.data.BinSkuData;
import com.increff.model.data.ErrorData;
import com.increff.Assure.model.form.BinSkuUpdateForm;
import com.increff.Assure.pojo.ProductPojo;
import com.increff.Assure.util.ValidationUtil;
import com.increff.exception.ApiException;
import com.increff.Assure.model.form.BinSkuForm;
import com.increff.Assure.model.form.BinSkuItemForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.increff.Assure.dto.dtoHelper.BinSkuDtoHelper.*;
import static java.util.Objects.isNull;

@Service
public class BinSkuDto
{
    private static final Long MAX_BIN_LIMIT = 100L;

    @Autowired
    private BinSkuApi binSkuApi;

    @Autowired
    private ProductApi productApi;

    @Autowired
    private BinApi binApi;
    @Autowired
    private UserApi userApi;

    @Autowired
    private InventoryApi inventoryApi;



    @Transactional(rollbackFor = ApiException.class)
    public Integer add(BinSkuForm binSkuForm)throws ApiException
    {
        List<BinSkuItemForm> binSkuItemFormList = binSkuForm.getBinSkuItemFormList();
        validateList(binSkuItemFormList,MAX_BIN_LIMIT);
        binSkuItemFormList=normalizeList(binSkuItemFormList);

        checkBinIdExists(binSkuItemFormList);
        Long clientId = userApi.getCheck(binSkuForm.getClientId()).getId();

        HashMap<String,Long> clientToGlobalSkuIdMap = getClientToGlobalSkuIdMap(binSkuItemFormList,clientId);
        checkClientSkuIdExist(clientToGlobalSkuIdMap,binSkuItemFormList);
        binSkuApi.add(convertBinSkuItemFormListToBinSkuItemPojo(binSkuItemFormList,clientToGlobalSkuIdMap));
        inventoryApi.add(convertBinSkuItemFormListToInventoryPojo(binSkuItemFormList,clientToGlobalSkuIdMap));
        return binSkuItemFormList.size();

    }

    public List<BinSkuData> select()
    {
        return convertBinSkuPojoListtoBinSkuData(binSkuApi.selectAll());
    }

    public void update(BinSkuUpdateForm binSkuUpdateForm, Long id) throws ApiException {
        binSkuApi.update(convertBinSkuUpdateFormtoBinSkuPojo(binSkuUpdateForm,id));
    }

    public HashMap<String,Long> getClientToGlobalSkuIdMap(List<BinSkuItemForm> binSkuItemFormList,Long clientId)
    {
        List<String> clientSkuIdList = binSkuItemFormList.stream().map(BinSkuItemForm::getClientSkuId).collect(Collectors.toList());

        HashMap<String,Long> clientToGlobalSkuIdMap = new HashMap<>();
        for(String clientSkuId : clientSkuIdList)
        {
            ProductPojo productPojo = productApi.selectByClientSkuIdandClientId(clientSkuId,clientId);
            if(!isNull(productPojo))
            {
                clientToGlobalSkuIdMap.put(productPojo.getClientSkuId(),productPojo.getGlobalSkuId());
            }
        }
        return clientToGlobalSkuIdMap;
    }

    private void checkBinIdExists(List<BinSkuItemForm> binSkuItemFormList)throws ApiException
    {
        Long row = 1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        for(BinSkuItemForm binSkuItemForm : binSkuItemFormList)
        {
            if(isNull(binApi.selectById(binSkuItemForm.getBinId())))
            {
                errorDataList.add(new ErrorData(row, "binId does not exist, binId : "+ binSkuItemForm.getBinId()));
            }
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);
    }

    public HashMap<String, Long> checkClientSkuIdExist(HashMap<String,Long> clientToGlobalSkuIdMap, List<BinSkuItemForm> binSkuItemFormList)throws ApiException
    {
        Long row=1L;
        List<ErrorData> errorDataList = new ArrayList<>();
        for(BinSkuItemForm binSkuItemForm : binSkuItemFormList)
        {
            if(!clientToGlobalSkuIdMap.containsKey(binSkuItemForm.getClientSkuId()))
            {
                errorDataList.add(new ErrorData(row,"ClientSkuId does not exist, clientSkuId :" + binSkuItemForm.getClientSkuId()));
            }
            row++;
        }
        ValidationUtil.throwErrorIfNotEmpty(errorDataList);
        return clientToGlobalSkuIdMap;
    }


}
