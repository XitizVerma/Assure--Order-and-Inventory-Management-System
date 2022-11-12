package com.increff.Assure.api;

import com.increff.Assure.dao.BinDao;
import com.increff.exception.ApiException;
import com.increff.Assure.pojo.BinPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = ApiException.class)
public class BinApi
{

    @Autowired
    private BinDao binDao;

    public List<BinPojo> add(Long numberOfBins)
    {
        List<BinPojo> binPojoList = new ArrayList<>();
        for(int i = 0;i<numberOfBins;i++)
        {
            BinPojo binPojo = new BinPojo();
            binPojoList.add(binDao.add(binPojo));
        }
        return binPojoList;
    }

    public List<BinPojo>selectAll()
    {
        return binDao.selectAll();
    }

    public BinPojo selectById(Long id)
    {
        return binDao.selectById(id);
    }

}
