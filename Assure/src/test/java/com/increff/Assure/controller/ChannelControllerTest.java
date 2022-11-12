package com.increff.Assure.controller;

import com.increff.Assure.model.data.ChannelData;
import com.increff.Assure.model.form.ChannelForm;
import com.increff.Assure.util.InvoiceType;
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

import static com.increff.Assure.util.RandomUtil.getRandomString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class ChannelControllerTest
{

    @Resource
    private ChannelController channelController;

    @Test
    public void addChannelTest()throws ApiException
    {
        ChannelForm channelForm = generateChannelForm();
        channelController.addChannel(channelForm);

        ChannelData channelData = channelController.getChannels().get(0);
        Assert.assertEquals(channelForm.getName(),channelData.getName());
        Assert.assertEquals(channelForm.getInvoiceType(),channelData.getInvoiceType());

    }

    @Test
    public void selectAllChannelsTest()throws ApiException
    {
        for(int i=0;i<5;i++)
        {
            channelController.addChannel(generateChannelForm());
        }
        Assert.assertEquals(5,channelController.getChannels().size());
    }

    @Test
    public void validationTest()throws ApiException
    {
        ChannelForm channelForm = new ChannelForm();

        try {
            channelController.addChannel(channelForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Channel Name cannot be Empty or Null",e.getMessage());
        }

        channelForm.setName(getRandomString());
        try {
            channelController.addChannel(channelForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Channel Invoice Type Cannot be Empty or Null",e.getMessage());
        }
    }

    @Test
    public void emptyTest()throws ApiException
    {
        ChannelForm channelForm = new ChannelForm();
        channelForm.setName("");

        try {
            channelController.addChannel(channelForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Channel Name cannot be Empty or Null",e.getMessage());
        }

        channelForm.setName(getRandomString());
        try {
            channelController.addChannel(channelForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Channel Invoice Type Cannot be Empty or Null",e.getMessage());
        }
    }

    @Test
    public void normalizeTest()throws ApiException
    {
        ChannelForm channelForm = generateChannelForm();
        channelForm.setName(" " + channelForm.getName() + " ");
        channelForm.setName(channelForm.getName().toUpperCase());
        channelController.addChannel(channelForm);
        ChannelData channelData = channelController.getChannels().get(0);
        Assert.assertEquals(channelForm.getName(),channelData.getName());
    }

    @Test
    public void channelAlreadyPresent()throws ApiException
    {
        ChannelForm channelForm = generateChannelForm();
        channelController.addChannel(channelForm);
        try {
            channelController.addChannel(channelForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Channel Already exists, channel = " + channelForm.getName(),e.getMessage());
        }
    }

    private ChannelForm generateChannelForm()
    {
        ChannelForm channelForm = new ChannelForm();
        String name = getRandomString();
        InvoiceType invoiceType = InvoiceType.CHANNEL;

        channelForm.setName(name);
        channelForm.setInvoiceType(invoiceType);
        return channelForm;
    }
}
