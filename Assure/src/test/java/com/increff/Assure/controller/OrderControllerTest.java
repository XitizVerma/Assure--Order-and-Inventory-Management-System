package com.increff.Assure.controller;

import com.increff.Assure.dao.OrderDao;
import com.increff.Assure.model.form.*;
import com.increff.Assure.util.InvoiceType;
import com.increff.Assure.util.OrderStatus;
import com.increff.Assure.util.UserType;
import com.increff.exception.ApiException;
import com.increff.model.form.OrderForm;
import com.increff.model.form.OrderItemForm;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.increff.Assure.util.RandomUtil.getRandomNumberBetween1to100;
import static com.increff.Assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class OrderControllerTest extends AbstractUnitTest {
    @Resource
    private UserController userController;

    @Resource
    private ProductController productController;

    @Resource
    private BinController binController;

    @Resource
    private BinSkuController binSkuController;

    @Resource
    private OrderController orderController;

    @Resource
    private ChannelController channelController;

    @Resource
    private OrderDao orderDao;


    @Test
    public void addTest() throws ApiException {
        addOrderHelper();
        Assert.assertEquals(1,orderDao.selectAll().size());
    }

    @Test
    public void allocationTest()throws ApiException{

        addOrderHelper();

        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderDao.selectAll().get(0).getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.ALLOCATED);
        orderController.updateStatus(orderStatusUpdateForm);

        Assert.assertEquals(OrderStatus.ALLOCATED,orderDao.selectAll().get(0).getStatus());

    }

    @Test
    public void invoiceGenerationTest() throws ApiException, IOException, TransformerException {

        addOrderHelper();

        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderDao.selectAll().get(0).getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.ALLOCATED);
        orderController.updateStatus(orderStatusUpdateForm);

        orderController.getInvoice(orderDao.selectAll().get(0).getId());
        Assert.assertEquals(OrderStatus.FULFILLED,orderDao.selectAll().get(0).getStatus());

    }

    @Test
    public void orderFulfillmentTest() throws ApiException, IOException, TransformerException {

        addOrderHelper();

        OrderStatusUpdateForm orderStatusUpdateForm = new OrderStatusUpdateForm();
        orderStatusUpdateForm.setOrderId(orderDao.selectAll().get(0).getId());
        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.ALLOCATED);
        orderController.updateStatus(orderStatusUpdateForm);

        orderStatusUpdateForm.setUpdateStatusTo(OrderStatus.FULFILLED);
        orderController.updateStatus(orderStatusUpdateForm);
        Assert.assertEquals(OrderStatus.FULFILLED,orderDao.selectAll().get(0).getStatus());

    }

    @Test
    public void paramValueCheck()throws ApiException
    {
        Long userId = generateClientId();
        Long channelId = generateChannelId();

        ProductForm productForm = generateProductForm();
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList, userId);

        BinSkuForm binSkuForm = generateBinSkuForm(userId, productForm);
        binSkuController.addBinSku(binSkuForm);
        OrderForm orderForm = generateOrderForm(userId, channelId, productForm);
        orderController.addOrder(orderForm);

        Assert.assertEquals(orderDao.selectAll().get(0).getClientId(),userId);
        Assert.assertEquals(orderDao.selectAll().get(0).getChannelId(),channelId);
        Assert.assertEquals(orderDao.selectAll().get(0).getCustomerId(),orderForm.getCustomerId());
        Assert.assertEquals(orderDao.selectAll().get(0).getChannelOrderId(),orderForm.getChannelOrderId());
    }
    @Test
    public void nullTest()throws ApiException
    {
        OrderForm orderForm = new OrderForm();
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("ClientId cannot be null or empty",e.getMessage());
        }

        Long userId = generateClientId();
        orderForm.setClientId(userId);
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("ChannelOrderId cannot be null or empty",e.getMessage());
        }

        orderForm.setChannelOrderId(getRandomString());
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("CustomerId cannot be null or empty",e.getMessage());
        }

        orderForm.setCustomerId(100000L);
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("ChannelId cannot be null or empty",e.getMessage());
        }

        orderForm.setChannelId(generateChannelId());

        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Order Item List List cannot be empty",e.getMessage());
        }

        OrderItemForm orderItemForm = new OrderItemForm();
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        orderItemFormList.add(orderItemForm);
        orderForm.setOrderItemFormList(orderItemFormList);

        orderItemFormList.get(0).setClientSkuId(getRandomString());
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("SellingPricePerUnit cannot be null",e.getMessage());
        }
        orderItemFormList.get(0).setSellingPricePerUnit(10.00);
        try
        {
            orderController.addOrder(orderForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Quantity cannot be null or zero",e.getMessage());
        }


    }




    private void addOrderHelper()throws ApiException
    {
        Long userId = generateClientId();
        Long channelId = generateChannelId();

        ProductForm productForm = generateProductForm();
        List<ProductForm> productFormList = new ArrayList<>();
        productFormList.add(productForm);
        productController.bulkAdd(productFormList, userId);

        BinSkuForm binSkuForm = generateBinSkuForm(userId, productForm);
        binSkuController.addBinSku(binSkuForm);
        OrderForm orderForm = generateOrderForm(userId, channelId, productForm);
        orderController.addOrder(orderForm);

    }
    private OrderForm generateOrderForm(Long clientId, Long channelId, ProductForm productForm) {
        OrderForm orderForm = new OrderForm();

        orderForm.setClientId(clientId);
        orderForm.setChannelId(channelId);
        orderForm.setCustomerId(100001L);
        String channelOrderId = getRandomString();
        orderForm.setChannelOrderId(channelOrderId);
        orderForm.setOrderItemFormList(generateOrderItemFormList(productForm));

        return orderForm;
    }

    private List<OrderItemForm> generateOrderItemFormList(ProductForm productForm) {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();

        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setClientSkuId(productForm.getClientSkuId());
        orderItemForm.setQuantity(getRandomNumberBetween1to100());
        orderItemForm.setSellingPricePerUnit((double) getRandomNumberBetween1to100());
        orderItemFormList.add(orderItemForm);

        return orderItemFormList;
    }

    private Long generateChannelId() throws ApiException {
        ChannelForm channelForm = new ChannelForm();
        String name = "INTERNAL";
        InvoiceType invoiceType = InvoiceType.CHANNEL;
        channelForm.setName(name);
        channelForm.setInvoiceType(invoiceType);
        channelController.addChannel(channelForm);
        return channelController.getChannels().get(0).getId();
    }


    private BinSkuForm generateBinSkuForm(Long userId, ProductForm productForm) throws ApiException {
        BinSkuForm binSkuForm = new BinSkuForm();

        Long clientId = userId;
        binSkuForm.setClientId(clientId);

        binController.addBins(1L);
        BinSkuItemForm binSkuItemForm = new BinSkuItemForm();
        binSkuItemForm.setBinId(binController.getBins().get(0).getBinId());

        binSkuItemForm.setClientSkuId(productForm.getClientSkuId());
        binSkuItemForm.setQuantity(getRandomNumberBetween1to100() + 100);

        List<BinSkuItemForm> binSkuItemFormList = new ArrayList<>();
        binSkuItemFormList.add(binSkuItemForm);
        binSkuForm.setBinSkuItemFormList(binSkuItemFormList);

        return binSkuForm;
    }

    private ProductForm generateProductForm() throws ApiException {
        ProductForm productForm = new ProductForm();

        String clientSkuId = getRandomString();
        String name = getRandomString();
        String brandId = getRandomString();
        Double mrp = Math.random() * 100 % (20);
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
