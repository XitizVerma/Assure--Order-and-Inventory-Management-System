package com.increff.Assure.controller;

import com.increff.Assure.dto.UserDto;
import com.increff.exception.ApiException;
import com.increff.Assure.model.data.ProductData;
import com.increff.Assure.model.form.ProductForm;
import com.increff.Assure.model.form.UserForm;
import com.increff.Assure.util.UserType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static com.increff.Assure.util.RandomUtil.getRandomNumber;
import static com.increff.Assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ProductControllerTest {

    @Resource
    private ProductController productController;

    @Resource
    private UserController userController;

    @Test
    public void bulkAddTest()throws ApiException
    {

        Long clientId = generateClientId();
        List<ProductForm> productFormList = new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            productFormList.add(generateProductForm());
        }
        productController.bulkAdd(productFormList,clientId);

        Assert.assertEquals(5,productController.getAll().size());

    }


    @Test
    public void selectByIdTest()throws ApiException
    {
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(generateProductForm());
        productController.bulkAdd(productFormList,generateClientId());

        ProductData productData = productController.getAll().get(0);
        Long globalSkuId = productData.getGlobalSkuId();

        Assert.assertEquals(productController.getProductById(globalSkuId).getClientSkuId(),productData.getClientSkuId());

        Long randomGlobalSkuId = Long.valueOf(getRandomNumber());
        try{
            productController.getProductById((long)randomGlobalSkuId);
        }catch (ApiException e)
        {
            Assert.assertEquals("GlobalSkuid does not exist " + randomGlobalSkuId,e.getMessage());
        }
    }

    @Test
    public void updateTest()throws ApiException
    {
        Long clientId = generateClientId();
        List<ProductForm> productFormList = new ArrayList<>();
        ProductForm productForm = generateProductForm();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList,clientId);

        Long globalSkuId = productController.getAll().get(0).getGlobalSkuId();
        productController.update(productForm,globalSkuId);
        ProductData productData = productController.getAll().get(0);
        Assert.assertEquals(productForm.getName().trim().toLowerCase(),productData.getName());
        Assert.assertEquals(productForm.getDescription().trim().toLowerCase(),productData.getDescription());
        Assert.assertEquals(productForm.getMrp(),productData.getMrp());


    }

    @Test
    public void validationTest()throws ApiException
    {
        Long clientId = generateClientId();
        ProductForm productForm = new ProductForm();
        List<ProductForm> productFormList = new ArrayList<>();

        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("ProductForm List cannot be empty",e.getMessage());
        }
        productFormList.add(productForm);
        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("ClientSkuId cannot be null or empty",e.getMessage());
        }

        productForm.setClientSkuId(getRandomString());
        productFormList.add(productForm);
        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("Productname cannot be null or empty",e.getMessage());
        }

        productForm.setName(getRandomString());
        productFormList.add(productForm);
        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("BrandId cannot be null or empty",e.getMessage());
        }

        productForm.setBrandId(getRandomString());
        productFormList.add(productForm);
        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("Mrp cannot be null or less than 0.0",e.getMessage());
        }

        productForm.setMrp((double) getRandomNumber());
        productFormList.add(productForm);
        try
        {
            productController.bulkAdd(productFormList,clientId);
        }catch (ApiException e)
        {
            Assert.assertEquals("Description cannot be null or empty",e.getMessage());
        }

    }

    private ProductForm generateProductForm()throws ApiException
    {
        ProductForm productForm = new ProductForm();

        String clientSkuId = getRandomString();
        String name = getRandomString();
        String brandId = getRandomString();
        Double mrp = Math.random()*100%(20);
        String description = getRandomString();

        productForm.setClientSkuId(clientSkuId);
        productForm.setName(name);
        productForm.setBrandId(brandId);
        productForm.setMrp(mrp);
        productForm.setDescription(description);

        return productForm;
    }


    private Long generateClientId() throws ApiException {
        String name = getRandomString();
        UserType userType = UserType.CLIENT;

        UserForm userForm = new UserForm();
        userForm.setName(name);
        userForm.setUserType(userType);
        userController.add(userForm);
        Long id = (userController.getAll()).get(0).getId();
        return id;
    }
}
