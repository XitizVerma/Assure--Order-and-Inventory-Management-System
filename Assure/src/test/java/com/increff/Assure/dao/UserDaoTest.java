package com.increff.Assure.dao;

import com.increff.Assure.controller.QAConfig;
import com.increff.Assure.pojo.UserPojo;
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

import java.util.List;

import static com.increff.Assure.util.RandomUtil.getRandomNumber;
import static com.increff.Assure.util.RandomUtil.getRandomString;
import static org.junit.Assert.assertSame;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = QAConfig.class,loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("src/test/webapp")
@Transactional
public class UserDaoTest
{

    @Resource
    private UserDao userDao;

    @Test
    public void add()
    {
        UserPojo userPojo = new UserPojo();
        String name = getRandomString();
        userPojo.setName(name);
        userPojo.setUserType(UserType.CLIENT);
        userDao.add(userPojo);
    }

    @Test
    public void selectAll()
    {
        for(int i=0;i<5;i++)
        {
            add();
        }
        List<UserPojo> userPojoList = userDao.selectAll();
        assertSame(userPojoList.size(),5);
    }

    @Test
    public void selectById()
    {

    }

    @Test
    public void uniqueCheck()
    {
        String name = getRandomString();
        UserType userType = UserType.CUSTOMER;

        Assert.assertNull(userDao.selectByNameandUserType(name,userType));

        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setUserType(userType);
        userDao.add(userPojo);

        Assert.assertNotNull(userDao.selectByNameandUserType(userPojo.getName(),userPojo.getUserType()));
    }

    @Test
    public void checkId()
    {
        Long id = (long)getRandomNumber();
        Assert.assertNull(userDao.selectByUserId(id));
        UserPojo userPojo = daoInsertHelper();
        Assert.assertNotNull(userPojo.getId());

    }


    @Test
    public void update()
    {
        userDao.update();
    }

    private UserPojo daoInsertHelper()
    {
        String name = getRandomString();
        UserType userType = UserType.CUSTOMER;
        UserPojo userPojo = new UserPojo();
        userPojo.setName(name);
        userPojo.setUserType(userType);
        userDao.add(userPojo);
        return userPojo;
    }


}
