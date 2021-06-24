package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "PRODUCT_CATEGORY")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT")
    private Product fkProduct;

    @ManyToOne
    @JoinColumn(name = "FK_CATEGORY")
    private Category fkCategory;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();
}
