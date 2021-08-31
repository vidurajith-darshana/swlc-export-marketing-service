package com.swlc.swlcexportmarketingservice.entity;


import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Data
@Table(name = "USER_PROMOTION", uniqueConstraints = {@UniqueConstraint(columnNames = {"FK_USER", "FK_PROMOTION"})})
public class UserPromotion {

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

    @NotNull
    @Column(name = "LIKE_STATUS")
    private Integer likeStatus;

    public UserPromotion(User fkUser, Promotion fkPromotion, @NotNull Integer likeStatus) {
        this.fkUser = fkUser;
        this.fkPromotion = fkPromotion;
        this.likeStatus = likeStatus;
    }
}
