package com.increff.Assure.controller;

import com.increff.Assure.model.data.UserData;
import com.increff.Assure.model.form.UserForm;
import com.increff.Assure.dto.UserDto;
import com.increff.exception.ApiException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
public class UserController
{
    //TODO : Refactor all modules to smallcase

    @Autowired
    private UserDto userDto;

    @ApiOperation(value = "Get All Users")
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<UserData> getAll()
    {
        return userDto.getAll();
    }

    @ApiOperation(value = "Get User By Id")
    @RequestMapping(path = "/users/{id}", method = RequestMethod.GET)
    public UserData getUserById(@PathVariable Long id)throws ApiException
    {
        return userDto.get(id);
    }

    @ApiOperation(value = "Add an User(Client/Customer)")
    @RequestMapping(path = "/users", method =RequestMethod.POST)
    public void add(@RequestBody UserForm userForm)throws ApiException
    {
        userDto.add(userForm);
    }
}
