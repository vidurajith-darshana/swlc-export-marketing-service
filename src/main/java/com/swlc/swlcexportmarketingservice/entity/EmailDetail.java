package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "EMAIL_DETAIL")
public class EmailDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "DESCRIPTION",length = 20000)
    private String description;

    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User fkUser;

    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT")
    private Product fkProduct;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();
}
