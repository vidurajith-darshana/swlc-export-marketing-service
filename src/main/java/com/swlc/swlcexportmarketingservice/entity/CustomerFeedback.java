package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "CUSTOMER_FEEDBACK")
public class CustomerFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "MESSAGE")
    private String message;

    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User fkUser;

    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT")
    private Product fkProduct;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();
}
