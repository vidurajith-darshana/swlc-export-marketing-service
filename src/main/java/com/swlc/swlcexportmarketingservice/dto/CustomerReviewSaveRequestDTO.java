package com.swlc.swlcexportmarketingservice.dto;

import com.swlc.swlcexportmarketingservice.enums.CustomerReviewType;

/**
 * @author hp
 */
public class CustomerReviewSaveRequestDTO {
    private CustomerReviewType customerReviewType;
    private String customerReviewComment;
    private Integer userId;

    public CustomerReviewSaveRequestDTO() {
    }

    public CustomerReviewSaveRequestDTO(CustomerReviewType customerReviewType, String customerReviewComment, Integer userId) {
        this.customerReviewType = customerReviewType;
        this.customerReviewComment = customerReviewComment;
        this.userId = userId;
    }

    public CustomerReviewType getCustomerReviewType() {
        return customerReviewType;
    }

    public void setCustomerReviewType(CustomerReviewType customerReviewType) {
        this.customerReviewType = customerReviewType;
    }

    public String getCustomerReviewComment() {
        return customerReviewComment;
    }

    public void setCustomerReviewComment(String customerReviewComment) {
        this.customerReviewComment = customerReviewComment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "CustomerReviewSaveRequestDTO{" +
                "customerReviewType=" + customerReviewType +
                ", customerReviewComment='" + customerReviewComment + '\'' +
                ", userId=" + userId +
                '}';
    }

}
