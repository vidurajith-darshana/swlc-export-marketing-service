package com.swlc.swlcexportmarketingservice.entity;

import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
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
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @NotNull
    @Column(name = "TOTAL_QTY")
    private Integer totalQty;

    @NotNull
    @Column(name = "CURRENT_QTY")
    private Integer currentQty;

    @Column(name="CREATE_DATE")
    @CreationTimestamp
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
