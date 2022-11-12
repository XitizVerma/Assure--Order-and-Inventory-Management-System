package com.increff.Assure.pojo;

import com.increff.Assure.util.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "channelOrderId")})
public class OrderPojo extends AbstractPojo
{

    @Id
    @TableGenerator(name = TableConstants.SEQ_CHANNEL , initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_CHANNEL)
    private Long id;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelOrderId;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private String invoiceUrl;

}

