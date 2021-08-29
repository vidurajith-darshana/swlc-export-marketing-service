package com.swlc.swlcexportmarketingservice.entity;

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

    @Override
    public String toString() {
        return "ProductReviews{" +
                "id=" + id +
                ", user=" + user +
                ", product=" + product +
                ", date=" + date +
                '}';
    }
}
