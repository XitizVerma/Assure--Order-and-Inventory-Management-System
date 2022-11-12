package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class InventoryPojo extends AbstractPojo
{

    @Id
    @TableGenerator(name = TableConstants.SEQ_INVENTORY, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_INVENTORY)
    private Long id;

    @Column(nullable = false,unique = true)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long availableQuantity;

    @Column(nullable = false)
    private Long allocatedQuantity;

    @Column(nullable = false)
    private Long fulfilledQuanity;

}
