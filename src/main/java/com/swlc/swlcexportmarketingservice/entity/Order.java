package com.swlc.swlcexportmarketingservice.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private double total;
    @NotNull
    private String message;
    @NotNull
    private String status;
    @Column(name="create_date",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createDate = new Date();

    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productId",referencedColumnName = "productId")
    private Product product;
}
