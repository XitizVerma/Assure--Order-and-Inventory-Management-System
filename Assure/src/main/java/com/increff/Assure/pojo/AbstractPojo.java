package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass//So that separate table aint created
public class AbstractPojo
{
    @Version
    private Integer version;
    //Hibernate will automatically pickup the annotation and it will create a "version" column in your (in my case MySql) table.
    // Every time a record gets updated, hibernate will increment the counter with 1.
    //Now why is this something you want? Well the reason why you might wanna use this is because it decreases the chance that your
    // clients are working with stale data. Whenever a client retrieves information from you a version is provided with the data he requested.

    @CreationTimestamp
    private ZonedDateTime createdAt;

    @UpdateTimestamp
    private ZonedDateTime updatedAt;

}
