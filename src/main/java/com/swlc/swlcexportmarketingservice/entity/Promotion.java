package com.swlc.swlcexportmarketingservice.entity;

import com.swlc.swlcexportmarketingservice.enums.ProductStatus;
import com.swlc.swlcexportmarketingservice.enums.PromotionStatus;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "PROMOTION")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @OneToMany(mappedBy = "fkUserPromotion",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<UserPromotion> fkUserPromotion;

    @NotNull
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private PromotionStatus status;

    @NotNull
    @Column(name = "IMAGE")
    private String image;

    @NotNull
    @Column(name = "DESCRIPTION")
    private String description;

    @NotNull
    @Column(name = "HEADING")
    private String heading;

    @Column(name="CREATE_DATE")
    @CreationTimestamp
    private Date createDate = new Date();

}
