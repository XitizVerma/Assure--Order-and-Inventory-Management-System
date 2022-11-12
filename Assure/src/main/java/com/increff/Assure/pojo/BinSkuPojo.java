package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class BinSkuPojo extends AbstractPojo
{
    @Id
    @TableGenerator(name = TableConstants.SEQ_BIN_SKU, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_BIN_SKU)
    private Long id;

    @Column(nullable = false)
    private Long binId;

    @Column(nullable = false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long quantity;

}
