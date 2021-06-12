package com.swlc.swlcexportmarketingservice.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private int qty;
    @NotNull
    private int price;
    @NotNull
    private int subTotal;
    @Column(name="create_date",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createDate = new Date();

    @ManyToOne
    @JoinColumn(name = "productId", referencedColumnName = "productId")
    private Product product;
}
