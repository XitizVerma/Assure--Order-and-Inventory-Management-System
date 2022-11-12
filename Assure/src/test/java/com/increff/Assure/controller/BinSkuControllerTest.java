package com.increff.Assure.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.increff.Assure.model.form.*;
import com.increff.Assure.pojo.InventoryPojo;
import com.increff.Assure.util.UserType;
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

import java.util.ArrayList;
import java.util.List;

import static com.increff.Assure.util.RandomUtil.getRandomNumberBetween1to100;
import static com.increff.Assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class BinSkuControllerTest
{

    @Resource
    private BinSkuController binSkuController;

    @Resource
    private BinController binController;

    @Resource
    private UserController userController;

    @Resource
    private ProductController productController;


    @Test
    public void addTest()throws ApiException
    {
        BinSkuForm binSkuForm = generateBinSkuForm();
        binSkuController.addBinSku(binSkuForm);
        Assert.assertEquals(1,binSkuController.getBinSku().size());

    }


    @Test
    public void selectAllTest()throws ApiException
    {
        BinSkuForm binSkuForm = generateBinSkuForm();
        binSkuController.addBinSku(binSkuForm);
        Assert.assertEquals(binSkuForm.getBinSkuItemFormList().get(0).getBinId(),binSkuController.getBinSku().get(0).getBinId());
        Assert.assertEquals(binSkuForm.getBinSkuItemFormList().get(0).getQuantity(),binSkuController.getBinSku().get(0).getQuantity());

    }

    @Test
    public void updateTest()throws ApiException
    {
        BinSkuForm binSkuForm = generateBinSkuForm();
        binSkuController.addBinSku(binSkuForm);
        Long binSkuId = binSkuController.getBinSku().get(0).getId();
        Long newQuantity = getRandomNumberBetween1to100();

        BinSkuUpdateForm binSkuUpdateForm = new BinSkuUpdateForm();
        binSkuUpdateForm.setQuantity(newQuantity);
        binSkuController.updateBinSku(binSkuId,binSkuUpdateForm);
        Long changedQuantity = binSkuController.getBinSku().get(0).getQuantity();
        Assert.assertEquals(newQuantity,changedQuantity);

        Long x = getRandomNumberBetween1to100();
        try{
            binSkuController.updateBinSku(x,binSkuUpdateForm);
        }
        catch (ApiException e)
        {
            Assert.assertEquals("Id does not exists, id : "+x,e.getMessage());
        }
    }

    @Test
    public void validationTest()throws ApiException
    {
        BinSkuForm binSkuForm = new BinSkuForm();

        userController.add(generateUserForm());
        Long clientId = userController.getAll().get(0).getId();
        binSkuForm.setClientId(clientId);

        try
        {
            binSkuController.addBinSku(binSkuForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("BinSkuItemForm List cannot be empty",e.getMessage());
        }


        BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        binSkuItemFormList.add(binSkuItemForm);
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = generateProductForm();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList,clientId);


        try
        {
            binSkuController.addBinSku(binSkuForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("ClientSkuId cannot be null or empty",e.getMessage());
        }


        binSkuItemFormList.get(0).setClientSkuId(productForm.getClientSkuId());
        try
        {
            binSkuController.addBinSku(binSkuForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("BinId cannot be null",e.getMessage());
        }

        binController.addBins(1L);
        binSkuItemFormList.get(0).setBinId(binController.getBins().get(0).getBinId());
        try
        {
            binSkuController.addBinSku(binSkuForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Quantity cannot be null or zero",e.getMessage());
        }
        binSkuItemFormList.get(0).setQuantity(getRandomNumberBetween1to100()+100);
        try
        {
            binSkuController.addBinSku(binSkuForm);
        }catch (ApiException e)
        {
            Assert.assertEquals(1,binSkuController.getBinSku().size());
        }
    }








    private BinSkuForm generateBinSkuForm()throws ApiException
    {
        BinSkuForm binSkuForm = new BinSkuForm();

        userController.add(generateUserForm());
        Long clientId = userController.getAll().get(0).getId();
        binSkuForm.setClientId(clientId);

        binController.addBins(1L);
        BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
        binSkuItemForm.setBinId(binController.getBins().get(0).getBinId());

        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = generateProductForm();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList,clientId);
        binSkuItemForm.setClientSkuId(productForm.getClientSkuId());
        binSkuItemForm.setQuantity(getRandomNumberBetween1to100()+100);

        List<BinSkuItemForm> binSkuItemFormList =new ArrayList<>();
        binSkuItemFormList.add(binSkuItemForm);
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);

        return binSkuForm;
    }

    private UserForm generateUserForm()
    {
        String name = getRandomString();
        UserType userType = UserType.CLIENT;

        UserForm userForm = new UserForm();
        userForm.setName(name);
        userForm.setUserType(userType);
        return userForm;
    }

    private ProductForm generateProductForm()throws ApiException
    {
        ProductForm productForm = new ProductForm();

        String clientSkuId = getRandomString();
        String name = getRandomString();
        String brandId = getRandomString();
        Double mrp = Math.random()*100;
        String description = getRandomString();

        productForm.setClientSkuId(clientSkuId);
        productForm.setName(name);
        productForm.setBrandId(brandId);
        productForm.setMrp(mrp);
        productForm.setDescription(description);

        return productForm;
    }

}
