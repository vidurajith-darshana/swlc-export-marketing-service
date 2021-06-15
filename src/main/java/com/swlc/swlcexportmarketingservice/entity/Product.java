package com.swlc.swlcexportmarketingservice.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "PRODUCT")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "CODE")
    private String code;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @NotNull
    @Column(name = "PRICE")
    private Double price;

    @NotNull
    @Column(name = "STATUS")
    private String status;

    @NotNull
    @Column(name = "TOTAL_QTY")
    private Integer totalQty;

    @NotNull
    @Column(name = "CURRENT_QTY")
    private Integer currentQty;

    @Column(name="CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @OneToMany(mappedBy = "fkProduct",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<OrderDetail> fkOrderDetails;

    @OneToMany(mappedBy = "fkCategory",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ProductCategory> fkProductCategories;

    public Product() {
    }

    public Product(Integer id) {
        this.id = id;
    }
}
