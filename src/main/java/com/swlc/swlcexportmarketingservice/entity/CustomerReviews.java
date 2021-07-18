package com.swlc.swlcexportmarketingservice.entity;

import com.swlc.swlcexportmarketingservice.enums.CustomerReviewType;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Data
@Table(name = "CUSTOMER_REVIEWS")
public class CustomerReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "CUSTOMER_REVIEW_TYPE")
    private CustomerReviewType customerReviewType;
    @Column(name = "CUSTOMER_REVIEW_COMMENT")
    private String customerReviewComment;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CUSTOMER_REVIEW_DATE")
    private Date customerReviewDate;
    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User user;

    public CustomerReviews(CustomerReviewType customerReviewType, String customerReviewComment, Date customerReviewDate, User user) {
        this.customerReviewType = customerReviewType;
        this.customerReviewComment = customerReviewComment;
        this.customerReviewDate = customerReviewDate;
        this.user = user;
    }
}
