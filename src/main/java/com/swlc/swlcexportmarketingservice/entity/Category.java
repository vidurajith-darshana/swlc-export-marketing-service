package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "CATEGORY")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    @NotNull
    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATE_DATE")
    private Date createDate = new Date();

    @OneToMany(mappedBy = "fkProduct",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<ProductCategory> fkProductCategories;
}
