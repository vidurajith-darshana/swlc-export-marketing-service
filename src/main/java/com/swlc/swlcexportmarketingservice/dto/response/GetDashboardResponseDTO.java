package com.swlc.swlcexportmarketingservice.dto.response;

/**
 * @author hp
 */
public class GetDashboardResponseDTO {

    private int totalCustomers;
    private int totalProducts;
    private int totalCategories;
    private int totalOrders;

    public GetDashboardResponseDTO() {
    }

    public GetDashboardResponseDTO(int totalCustomers, int totalProducts, int totalCategories, int totalOrders) {
        this.totalCustomers = totalCustomers;
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.totalOrders = totalOrders;
    }

    public int getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(int totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public int getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(int totalCategories) {
        this.totalCategories = totalCategories;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    @Override
    public String toString() {
        return "GetDashboardResponseDTO{" +
                "totalCustomers=" + totalCustomers +
                ", totalProducts=" + totalProducts +
                ", totalCategories=" + totalCategories +
                ", totalOrders=" + totalOrders +
                '}';
    }
}
