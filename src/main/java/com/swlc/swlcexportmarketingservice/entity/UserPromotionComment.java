package com.swlc.swlcexportmarketingservice.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@Entity
@NoArgsConstructor
@Data
@Table(name = "USER_PROMOTION_COMMENT")
public class UserPromotionComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User fkUser;

    @ManyToOne
    @JoinColumn(name = "FK_PROMOTION")
    private Promotion fkPromotion;

    @Column(name="CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_date;

    @NotNull
    @Column(name = "COMMENT")
    private String comment;

    public UserPromotionComment(User fkUser, Promotion fkPromotion, Date created_date, @NotNull String comment) {
        this.fkUser = fkUser;
        this.fkPromotion = fkPromotion;
        this.created_date = created_date;
        this.comment = comment;
    }
}
