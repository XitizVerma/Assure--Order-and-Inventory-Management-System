package com.increff.Assure.dao;

import com.increff.Assure.pojo.UserPojo;
import com.increff.Assure.util.UserType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class UserDao extends AbstractDao
{
    private final static String SELECT_BY_USER_ID = "select p from UserPojo p where id=:id";

    private final static String SELECT_BY_USER_NAME_USER_TYPE = "select p from UserPojo p where name=:name and userType=:userType";

    public List<UserPojo> selectAll()
    {
        return selectAll(UserPojo.class);
    }

    public void add(UserPojo userPojo)
    {
        addAbs(userPojo);
    }
    public UserPojo selectByUserId(Long id)
    {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_USER_ID,UserPojo.class);
        query.setParameter("id",id);
        return (UserPojo) getSingle(query);
    }

    public UserPojo selectByNameandUserType(String name, UserType userType)
    {
        TypedQuery<UserPojo> query = em().createQuery(SELECT_BY_USER_NAME_USER_TYPE,UserPojo.class);
        query.setParameter("name",name);
        query.setParameter("userType",userType);
        return (UserPojo) getSingle(query);
    }

}
