package com.increff.Assure.dto;

import com.increff.Assure.api.BinApi;
import com.increff.Assure.model.data.BinData;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.increff.Assure.dto.dtoHelper.BinDtoHelper.convertBinPojoListtoBinDataList;
import static com.increff.Assure.dto.dtoHelper.BinDtoHelper.validate;

@Service
public class BinDto
{

    @Autowired
    private BinApi binService;

    public List<BinData> add(Long numberOfBins)throws ApiException
    {
        validate(numberOfBins);
        return convertBinPojoListtoBinDataList(binService.add(numberOfBins));
    }

    public List<BinData> getAll()
    {
        return convertBinPojoListtoBinDataList(binService.selectAll());
    }


}
