package com.swlc.swlcexportmarketingservice.dto.response;

import com.swlc.swlcexportmarketingservice.dto.ProductDTO;

/**
 * @author hp
 */
public class Top10ProductsResponseDTO {

    private ProductDTO product;
    private int soldQty;

    public Top10ProductsResponseDTO() {
    }

    public Top10ProductsResponseDTO(ProductDTO product, int soldQty) {
        this.product = product;
        this.soldQty = soldQty;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getSoldQty() {
        return soldQty;
    }

    public void setSoldQty(int soldQty) {
        this.soldQty = soldQty;
    }

    @Override
    public String toString() {
        return "Top10ProductsResponseDTO{" +
                "product=" + product +
                ", soldQty=" + soldQty +
                '}';
    }
}
