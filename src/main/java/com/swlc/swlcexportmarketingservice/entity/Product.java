package com.swlc.swlcexportmarketingservice.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    @NotNull
    private String productName;
    @NotNull
    private String productCode;
    @NotNull
    private String productThumbnail;
    @NotNull
    private Double productPrice;
    @NotNull
    private String productStatus;
    @NotNull
    private int productTotalQty;
    @NotNull
    private int productCurrentQty;
    @Column(name="create_date",insertable = false,updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createDate = new Date();

    @OneToMany(mappedBy = "product",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public Product(int productId) {
        this.productId = productId;
    }
}
