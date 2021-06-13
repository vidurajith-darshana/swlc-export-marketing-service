package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Data
@Table(name = "ORDERS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User fkUser;

    @NotNull
    @Column(name = "TOTAL")
    private Double total;

    @NotNull
    @Column(name = "MESSAGE")
    private String message;

    @NotNull
    @Column(name = "STATUS")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();
}
