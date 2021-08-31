package com.swlc.swlcexportmarketingservice.dto.response;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;
import com.swlc.swlcexportmarketingservice.entity.Product;

/**
 * @author hp
 */
public class FullOrderDetailsDTO {
    private Integer id;
    private Double qty;
    private Double price;
    private Double subTotal;
    private ProductDTO product;

    public FullOrderDetailsDTO() {
    }

    public FullOrderDetailsDTO(Integer id, Double qty, Double price, Double subTotal, ProductDTO product) {
        this.id = id;
        this.qty = qty;
        this.price = price;
        this.subTotal = subTotal;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "FullOrderDetailsDTO{" +
                "id=" + id +
                ", qty=" + qty +
                ", price=" + price +
                ", subTotal=" + subTotal +
                ", product=" + product +
                '}';
    }
}
