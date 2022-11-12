package com.increff.Assure.model.data;

import com.increff.Assure.util.UserType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData
{
    private Long id;
    private String name;
    private UserType userType;

}
