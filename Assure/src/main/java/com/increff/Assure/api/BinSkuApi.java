package com.increff.Assure.api;

import com.increff.Assure.dao.BinSkuDao;
import com.increff.exception.ApiException;
import com.increff.model.data.ErrorData;
import com.increff.Assure.pojo.BinSkuPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.increff.Assure.util.ValidationUtil.throwErrorIfNotEmpty;
import static java.lang.Math.min;
import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinSkuApi
{

    @Autowired
    private BinSkuDao binSkuDao;

    public void add(List<BinSkuPojo> binSkuPojoList)throws ApiException
    {
        checkDupicateGlobalSkuAndBinIdPair(binSkuPojoList);
        for(BinSkuPojo binSkuPojo : binSkuPojoList)
        {
            BinSkuPojo exists = binSkuDao.selectByGlobalSkuIdAndBinId(binSkuPojo.getBinId(),binSkuPojo.getGlobalSkuId());
            if(isNull(exists))
            {
                binSkuDao.add(binSkuPojo);
            }
            else
            {
                exists.setQuantity(exists.getQuantity()+binSkuPojo.getQuantity());
                binSkuDao.update();
            }
        }
    }

    public List<BinSkuPojo> selectAll()
    {
        return binSkuDao.selectAll();
    }

    public void update(BinSkuPojo binSkuPojo)throws ApiException
    {
        BinSkuPojo exists = binSkuDao.selectById(binSkuPojo.getId());
        if(isNull(exists))
        {
            throw new ApiException("Id does not exists, id : "+binSkuPojo.getId());
        }
        exists.setQuantity(binSkuPojo.getQuantity());
    }

    public void checkDupicateGlobalSkuAndBinIdPair(List<BinSkuPojo> binSkuItemPojoList)throws ApiException
    {
        HashMap<Long, Set<Long>> clientSkuIdtoBinIdMap = new HashMap<>();
        List<ErrorData> errorDataList = new ArrayList<>();
        Long row = 1L;

        for(BinSkuPojo binSkuPojo : binSkuItemPojoList)
        {
            if(clientSkuIdtoBinIdMap.getOrDefault(binSkuPojo.getGlobalSkuId(),new HashSet<>()).contains(binSkuPojo.getBinId()))
            {
                errorDataList.add(new ErrorData(row,"Dupliacate values of globalSkuId-binId pair"));
            }
            else
            {
                clientSkuIdtoBinIdMap.getOrDefault(binSkuPojo.getGlobalSkuId(),new HashSet<>()).add(binSkuPojo.getBinId());
            }
            row++;
        }
        throwErrorIfNotEmpty(errorDataList);
    }

    public void allocateQty(Long allocatedQty, Long globalSkuId)
    {
        List<BinSkuPojo> binSkuPojoList = binSkuDao.selectByGlobalSkuId(globalSkuId);
        Collections.sort(binSkuPojoList,Comparator.comparing(BinSkuPojo::getQuantity));
        Collections.reverse(binSkuPojoList);

        for(BinSkuPojo binSkuPojo : binSkuPojoList)
        {
            Long allocatedQtyInBin = min(allocatedQty,binSkuPojo.getQuantity());
            binSkuPojo.setQuantity(binSkuPojo.getQuantity()-allocatedQtyInBin);
            allocatedQty=allocatedQty-allocatedQtyInBin;

            if(allocatedQty == 0)
                break;
        }
    }
}
