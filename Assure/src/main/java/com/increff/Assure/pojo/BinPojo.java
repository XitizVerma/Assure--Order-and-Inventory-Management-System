package com.increff.Assure.pojo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = "binId")})
public class BinPojo extends AbstractPojo
{
    @Id
    @TableGenerator(name = TableConstants.SEQ_BIN, initialValue = TableConstants.SEQ_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TableConstants.SEQ_BIN)
    private Long binId;
}
