package com.increff.Assure.controller;

import com.increff.exception.ApiException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

import static com.increff.Assure.util.RandomUtil.getRandomNumberBetween1to100;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinControllerTest
{
    @Resource
    private BinController binController;

    //TODO: Put each test into different Test Methods
    @Test
    public void addTest()throws ApiException
    {
        Long noOfBins = getRandomNumberBetween1to100();
        binController.addBins(noOfBins);
        Long getbins = (long) binController.getBins().size();
        Assert.assertEquals(noOfBins,getbins);
    }

    @Test
    public void binSizeTest()throws ApiException
    {
        Long noOfBins = getRandomNumberBetween1to100()+100;
        try {
            binController.addBins(noOfBins);
        }catch (ApiException e)
        {
            Assert.assertEquals("Number of Bins is greater than Max Limit, limit : " +(100),e.getMessage());
        }
    }

    @Test
    public void validBinTest()throws ApiException
    {
        Long nofBins = -1L;
        try{
            binController.addBins(nofBins);
        }catch (ApiException e)
        {
            Assert.assertEquals("Number of Bins should be greater than 0",e.getMessage());
        }
    }


}
