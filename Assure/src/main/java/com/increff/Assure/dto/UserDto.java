package com.increff.Assure.dto;

import com.increff.Assure.api.UserApi;
import com.increff.Assure.dto.dtoHelper.UserDtoHelper;
import com.increff.Assure.model.data.UserData;
import com.increff.Assure.model.form.UserForm;
import com.increff.Assure.pojo.UserPojo;
import com.increff.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDto
{

    @Autowired
    private UserApi userService;

    public List<UserData> getAll()
    {
        return UserDtoHelper.convertUserPojoListtoUserDataList(userService.selectAll());
    }

    public UserData get(Long id)throws ApiException
    {
        UserPojo userPojo = userService.selectByUserId(id);
        return UserDtoHelper.convertUserPojotoUserData(userPojo);
    }

    public void add(UserForm userForm)throws ApiException
    {

        UserDtoHelper.validate(userForm);
        userForm= UserDtoHelper.normalize(userForm);
        userService.add(UserDtoHelper.convertUserFormtoUserPojo(userForm));
    }

}
