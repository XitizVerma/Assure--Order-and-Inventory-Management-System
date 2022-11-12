package com.increff.Assure.pojo;

import com.increff.Assure.util.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
//TODO: change table unique constraints
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class UserPojo extends AbstractPojo
{
    @Id
    @TableGenerator(name = TableConstants.SEQ_USER, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = TableConstants.SEQ_USER)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private UserType userType;
}
