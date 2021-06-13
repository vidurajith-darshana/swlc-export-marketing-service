package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@Table(name = "ORDER_DETAIL")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "QTY")
    private Double qty;

    @NotNull
    @Column(name = "PRICE")
    private Double price;

    @NotNull
    @Column(name = "SUB_TOTAL")
    private Double subTotal;

    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT")
    private Product fkProduct;
}
