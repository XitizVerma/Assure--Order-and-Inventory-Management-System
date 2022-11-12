package com.increff.Assure.controller;

import com.increff.Assure.dao.ChannelListingDao;
import com.increff.Assure.model.form.*;
import com.increff.Assure.util.InvoiceType;
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

import static com.increff.Assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelListingControllerTest {

    @Resource
    private ChannelListingController channelListingController;

    @Resource
    private UserController userController;

    @Resource
    private ProductController productController;

    @Resource
    private ChannelController channelController;


    private static Long clientId;
    @Test
    public void add()throws ApiException
    {
        clientId = generateClientId();
        ChannelListingUploadForm channelListingUploadForm = generateChannelListingUploadForm();
        channelListingController.addChannelListings(channelListingUploadForm);
    }


    private ChannelListingUploadForm generateChannelListingUploadForm()throws ApiException
    {
        ChannelListingUploadForm channelListingUploadForm = new ChannelListingUploadForm();

        channelListingUploadForm.setClientId(clientId);
        channelListingUploadForm.setChannelId(generateChannelId());

        channelListingUploadForm.setChannelListingFormList(generateChannelListingFormList());
        return channelListingUploadForm;
    }

    private List<ChannelListingForm> generateChannelListingFormList()throws ApiException
    {
        ChannelListingForm channelListingForm = new ChannelListingForm();

        ProductForm productForm = generateProductForm();
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList,clientId);

        channelListingForm.setChannelSkuId(getRandomString());
        channelListingForm.setClientSkuId(productForm.getClientSkuId());

        List<ChannelListingForm> channelListingFormList = new ArrayList<>();
        channelListingFormList.add(channelListingForm);
        return channelListingFormList;
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

    private Long generateChannelId()throws ApiException
    {
        ChannelForm channelForm = new ChannelForm();
        String name = getRandomString();
        InvoiceType invoiceType = InvoiceType.CHANNEL;

        channelForm.setName(name);
        channelForm.setInvoiceType(invoiceType);
        channelController.addChannel(channelForm);
        return channelController.getChannels().get(0).getId();
    }
}
