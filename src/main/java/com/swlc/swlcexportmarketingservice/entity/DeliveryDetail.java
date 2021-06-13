package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "DELIVERY_DETAIL")
public class DeliveryDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "FK_USER")
    private User user;

    @NotNull
    @Column(name = "DELIVERY_ADDRESS")
    private String deliveryAddress;

    @Column(name = "BUYER_ADDRESS")
    private String buyerAddress;

    @NotNull
    @Column(name = "MOBILE")
    private String mobile;

    @Column(name = "TELEPHONE")
    private String telephone;

    @NotNull
    @Column(name = "WHATSAPP")
    private String whatsapp;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();
}
