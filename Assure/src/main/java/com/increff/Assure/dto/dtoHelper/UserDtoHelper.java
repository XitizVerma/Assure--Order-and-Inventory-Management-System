package com.increff.Assure.dto.dtoHelper;

import com.increff.Assure.model.data.UserData;
import com.increff.Assure.model.form.UserForm;
import com.increff.Assure.pojo.UserPojo;
import com.increff.Assure.util.UserType;
import com.increff.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class UserDtoHelper
{
    public static UserData convertUserPojotoUserData(UserPojo userPojo)
    {
        UserData userData = new UserData();
        userData.setName(userPojo.getName());
        userData.setUserType(userPojo.getUserType());
        userData.setId(userPojo.getId());
        return userData;
    }

    public static List<UserData> convertUserPojoListtoUserDataList(List<UserPojo> userPojoList)
    {
        List<UserData> userDataList = new ArrayList<>();
        for(UserPojo userPojo : userPojoList)
        {
            userDataList.add(convertUserPojotoUserData(userPojo));
        }
        return userDataList;
    }


    public static UserPojo convertUserFormtoUserPojo(UserForm userForm)
    {
        UserPojo userPojo = new UserPojo();
        userPojo.setName(userForm.getName());
        userPojo.setUserType(userForm.getUserType());
        return userPojo;
    }

    public static void validate(UserForm userForm)throws ApiException
    {
        //TODO:Check for isNull(userform)
        if(isNull(userForm.getName()) || userForm.getName().isEmpty())
        {
            throw new ApiException("Username cannot be null or empty");
        }
        if(isNull(userForm.getUserType()))
        {
            throw new ApiException("UserType cannot be null or empty");
        }
        if(!(userForm.getUserType()== UserType.CLIENT||userForm.getUserType()==UserType.CUSTOMER))
        {
            throw new ApiException("UserType can only be CLIENT or CUSTOMER");
        }
    }

    public static UserForm normalize(UserForm userForm)
    {
        userForm.setName(userForm.getName().trim().toLowerCase());
        userForm.setUserType(userForm.getUserType());
        return userForm;
    }
}
