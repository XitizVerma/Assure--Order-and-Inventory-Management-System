package com.increff.Assure.model.form;

import com.increff.Assure.util.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserForm
{
    @NotBlank
    private String name;
    @NotNull
    private UserType userType;
}
