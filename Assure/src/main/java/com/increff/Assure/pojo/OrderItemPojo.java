package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static com.increff.Assure.pojo.TableConstants.SEQ_INITIAL_VALUE;
import static com.increff.Assure.pojo.TableConstants.SEQ_ORDER_ITEM;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"orderId","globalSkuId"})})
public class OrderItemPojo extends AbstractPojo{

    @Id
    @TableGenerator(name = SEQ_ORDER_ITEM, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_ORDER_ITEM)
    private Long id;

    @Column(nullable =false)
    private Long orderId;

    @Column(nullable=false)
    private Long globalSkuId;

    @Column(nullable = false)
    private Long orderedQuantity;

    @Column(nullable = false)
    private Long allocatedQuantity;

    @Column(nullable=false)
    private long fulfilledQuantity;

    @Column(nullable = false)
    private Double sellingPricePerUnit;
}
