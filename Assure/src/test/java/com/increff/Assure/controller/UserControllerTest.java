package com.increff.Assure.controller;

import com.increff.exception.ApiException;
import com.increff.Assure.model.data.UserData;
import com.increff.Assure.model.form.UserForm;
import com.increff.Assure.util.UserType;
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
public class UserControllerTest {

    @Resource
    private UserController userController;

    @Test
    public void addTest() throws ApiException
    {
        UserForm userForm = generateUserForm();
        userController.add(userForm);

        List<UserData> userDataList = userController.getAll();
        UserData userData = userDataList.get(0);
        Assert.assertEquals(userForm.getName(),userData.getName());
        Assert.assertEquals(userForm.getUserType(),userData.getUserType());
    }

    @Test
    public void selectAllUsersTest()throws ApiException
    {
        List<UserForm> userFormList = new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            UserForm userForm = generateUserForm();
            userController.add(userForm);
            userFormList.add(userForm);
        }


        List<UserData> userDataList = userController.getAll();
        Assert.assertEquals(5,userDataList.size());
    }




    @Test
    public void selectIdTest()throws ApiException
    {
        UserForm userForm = generateUserForm();
        userController.add(userForm);

        List<UserData> userDataList = userController.getAll();
        UserData userData = userDataList.get(0);
        Long id = userData.getId();

        Assert.assertEquals(userForm.getName(),userController.getUserById(id).getName());
        Assert.assertEquals(userForm.getUserType(),userController.getUserById(id).getUserType());

        try
        {
            userController.getUserById(id+1);
        }catch (ApiException e)
        {
            Assert.assertEquals("User does not exist, userID : "+(id+1),e.getMessage());
        }

    }

    @Test
    public void validationTest()throws ApiException
    {
        UserForm userForm = new UserForm();
        userForm.setName(null);

        try
        {
            userController.add(userForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Username cannot be null or empty",e.getMessage());
        }

        userForm.setName(getRandomString());
        try
        {
            userController.add(userForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("UserType cannot be null or empty",e.getMessage());
        }
    }

    @Test
    public void emptyTest()throws ApiException
    {
        UserForm userForm = new UserForm();
        userForm.setName("");

        try
        {
            userController.add(userForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("Username cannot be null or empty",e.getMessage());
        }

        userForm.setName(getRandomString());
        try
        {
            userController.add(userForm);
        }catch (ApiException e)
        {
            Assert.assertEquals("UserType cannot be null or empty",e.getMessage());
        }
    }

    @Test
    public void normalizeTest()throws ApiException
    {

        UserForm userForm = generateUserForm();
        userForm.setName(" "+userForm.getName()+" ");
        userForm.setName(userForm.getName().toUpperCase());
        userController.add(userForm);
        UserData userData = userController.getAll().get(0);
        Assert.assertEquals(userForm.getName(),userData.getName());

    }

    @Test
    public void userAlreadyPresentTest()throws ApiException
    {

        UserForm userForm = generateUserForm();
        userController.add(userForm);

        try
        {
            userController.add(userForm);
        }
        catch (ApiException e)
        {
            Assert.assertEquals("User Name and User Type Combination already exists",e.getMessage());
        }
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
}
