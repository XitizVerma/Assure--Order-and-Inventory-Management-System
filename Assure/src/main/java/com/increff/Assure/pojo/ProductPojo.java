package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"globalSkuId","clientSkuId"})})
public class ProductPojo
{
    //TODO:SEarch indexes in MYSQL
    @Id
    @TableGenerator(name = TableConstants.SEQ_PRODUCT , initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_PRODUCT)
    private Long globalSkuId;

    @Column(nullable = false)
    private String clientSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brandId;

    @Column(nullable = false)
    private Double mrp;

    @Column(nullable = false)
    private String description;//TODO:chanege nullable
}
