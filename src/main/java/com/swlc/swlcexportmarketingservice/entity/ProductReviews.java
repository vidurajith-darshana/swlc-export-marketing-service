package com.swlc.swlcexportmarketingservice.entity;

import com.swlc.swlcexportmarketingservice.enums.ProductReviewStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * @author hp
 */
@Entity
@NoArgsConstructor
@Data
@Table(name="PRODUCT_REVIEWS", uniqueConstraints = {@UniqueConstraint(columnNames = {"FK_USER", "FK_PRODUCT"})})
public class ProductReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @ManyToOne
    @JoinColumn(name = "FK_USER")
    private User user;
    @ManyToOne
    @JoinColumn(name = "FK_PRODUCT")
    private Product product;
    @Column(name = "CREATED_DATE")
    private Date date;
    @Enumerated(EnumType.STRING)
    private ProductReviewStatus status;

    public ProductReviews(User user, Product product, Date date, ProductReviewStatus status) {
        this.user = user;
        this.product = product;
        this.date = date;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ProductReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ProductReviewStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ProductReviews{" +
                "id=" + id +
                ", user=" + user +
                ", product=" + product +
                ", date=" + date +
                ", status=" + status +
                '}';
    }
}
