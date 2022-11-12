package com.increff.Assure.dto.dtoHelper;

import com.increff.Assure.model.data.BinData;
import com.increff.exception.ApiException;
import com.increff.Assure.pojo.BinPojo;

import java.util.ArrayList;
import java.util.List;


public class BinDtoHelper
{

    private static final Long MAX_BIN_SIZE = 100L;

    public static List<BinData> convertBinPojoListtoBinDataList(List<BinPojo> binPojoList)
    {
        List<BinData> binDataList = new ArrayList<>();
        for(BinPojo binPojo : binPojoList)
        {
            binDataList.add(convertBinPojotoBinData(binPojo));
        }
        return binDataList;
    }

    public static BinData convertBinPojotoBinData(BinPojo binPojo)
    {
        BinData binData = new BinData();
        binData.setBinId(binPojo.getBinId());
        return binData;
    }

    public static void validate(Long numberOfBins)throws ApiException
    {
        if(numberOfBins > MAX_BIN_SIZE)
        {
            throw new ApiException("Number of Bins is greater than Max Limit, limit : " + MAX_BIN_SIZE);
        }
        if(numberOfBins<=0)
        {
            throw new ApiException("Number of Bins should be greater than 0");
        }
    }
}
