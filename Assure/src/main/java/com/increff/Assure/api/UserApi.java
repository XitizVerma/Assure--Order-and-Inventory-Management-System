package com.increff.Assure.api;

import com.increff.Assure.pojo.UserPojo;
import com.increff.Assure.util.UserType;
import com.increff.Assure.dao.UserDao;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Transactional(rollbackFor = ApiException.class)
public class UserApi
{

    @Autowired
    private UserDao userDao;

    @Transactional(readOnly = true)
    public List<UserPojo> selectAll()
    {
        return userDao.selectAll();
    }

    public void add(UserPojo userPojo)throws ApiException
    {//TODO:use meaning names in pojos ..dont use 2,3
        UserPojo userPojo2 = selectByNameandUserType(userPojo.getName(),userPojo.getUserType());
        if(isNull(userPojo2)==false)
        {
            throw new ApiException("User Name and User Type Combination already exists");
        }
        userDao.add(userPojo);
    }

    public UserPojo selectByUserId(Long id)throws ApiException
    {
        return getCheck(id);
    }

    public UserPojo getCheck(Long id)throws  ApiException
    {
        UserPojo userPojo = userDao.selectByUserId(id);
        if(isNull(userPojo))
        {
            throw new ApiException("User does not exist, userID : "+id);
        }
        return userPojo;
    }

    public UserPojo selectByNameandUserType(String name, UserType userType)
    {
        return userDao.selectByNameandUserType(name,userType);
    }

    public Long checkByIdAndType(Long id, UserType userType)throws ApiException
    {
        UserPojo userPojo = selectByUserId(id);
        if(isNull(userPojo))
        {
            throw new ApiException("User does not exist for given id");
        }
        if(userPojo.getUserType()!=userType)
        {
            throw new ApiException(userType.toString()+ " does not exist");
        }
        return id;
    }
}
