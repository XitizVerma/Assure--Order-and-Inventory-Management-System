package com.increff.Assure.util;

import com.increff.exception.ApiException;
import com.increff.model.data.ErrorData;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ValidationUtil
{
    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    public static <T> void validateList(String name, List<T>formList, Long maxListSize)throws ApiException
    {
        validateListSize(name,formList,maxListSize);
        List<ErrorData> errorFormList = new ArrayList<>();
        Long row = 1L;
        for(T form : formList)
        {
            Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(form);
            for(ConstraintViolation<T> constraintViolation : constraintViolationSet)
            {
                errorFormList.add(new ErrorData(row, constraintViolation.getPropertyPath().toString() + " " + constraintViolation.getMessage()));

            }
            row++;
        }
        throwErrorIfNotEmpty(errorFormList);
    }

    public static void throwErrorIfNotEmpty(List<ErrorData> errorFormList) throws ApiException
    {
        if(CollectionUtils.isEmpty(errorFormList)==false)
        {
            throw new ApiException(errorFormList);
        }
    }
    public static <T> void validateListSize(String name,List<T>formList,Long maxListSize)throws ApiException
    {
        if(CollectionUtils.isEmpty(formList))
        {
            throw new ApiException(name + " List cannot be empty");
        }
        if(formList.size()>maxListSize)
        {
            throw new ApiException(name + " List size is more than limit, limit : " + maxListSize);
        }
    }
}
