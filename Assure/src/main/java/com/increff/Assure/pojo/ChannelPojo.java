package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;
import com.increff.Assure.util.InvoiceType;

import javax.persistence.*;

import static com.increff.Assure.pojo.TableConstants.SEQ_CHANNEL;
import static com.increff.Assure.pojo.TableConstants.SEQ_INITIAL_VALUE;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ChannelPojo extends AbstractPojo
{
    @Id
    @TableGenerator(name = SEQ_CHANNEL, initialValue = SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_CHANNEL)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private InvoiceType invoiceType;

}
