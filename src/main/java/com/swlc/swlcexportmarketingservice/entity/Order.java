package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @Column(name = "ORDER_REF")
    private String orderRef;

    @Column(name = "STATUS")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();

    @OneToMany(mappedBy = "fkOrder",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetail> fkOrder;

    public Order(Integer id) {
        this.id = id;
    }

    public Order(Integer id, User fkUser, @NotNull Double total, @NotNull String message, @NotNull String status) {
        this.id = id;
        this.fkUser = fkUser;
        this.total = total;
        this.message = message;
        this.status = status;
    }
}
