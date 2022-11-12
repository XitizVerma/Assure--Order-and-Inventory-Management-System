package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity//TODO:check for uniqueness
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"channelSkuId","channelId"})})
public class ChannelListingPojo extends AbstractPojo{

    @Id
    @TableGenerator(name = TableConstants.SEQ_CHANNEL_LISTING, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_CHANNEL_LISTING)
    private Long id;

    @Column(nullable = false)
    private Long channelId;

    @Column(nullable = false)
    private String channelSkuId;

    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long globalSkuId;
}
