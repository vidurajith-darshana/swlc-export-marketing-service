package com.swlc.swlcexportmarketingservice.dto.response;

import com.swlc.swlcexportmarketingservice.entity.OrderDetail;
import com.swlc.swlcexportmarketingservice.entity.User;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author hp
 */
public class FullOrderDTO {
    private Integer id;
    private User fkUser;
    private Double total;
    private String message;
    private String orderRef;
    private String status;
    private Date createDate;
    private List<FullOrderDetailsDTO> fkOrder;

    public FullOrderDTO() {
    }

    public FullOrderDTO(Integer id, User fkUser, Double total, String message, String orderRef, String status, Date createDate, List<FullOrderDetailsDTO> fkOrder) {
        this.id = id;
        this.fkUser = fkUser;
        this.total = total;
        this.message = message;
        this.orderRef = orderRef;
        this.status = status;
        this.createDate = createDate;
        this.fkOrder = fkOrder;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getFkUser() {
        return fkUser;
    }

    public void setFkUser(User fkUser) {
        this.fkUser = fkUser;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<FullOrderDetailsDTO> getFkOrder() {
        return fkOrder;
    }

    public void setFkOrder(List<FullOrderDetailsDTO> fkOrder) {
        this.fkOrder = fkOrder;
    }

    @Override
    public String toString() {
        return "FullOrderDTO{" +
                "id=" + id +
                ", fkUser=" + fkUser +
                ", total=" + total +
                ", message='" + message + '\'' +
                ", orderRef='" + orderRef + '\'' +
                ", status='" + status + '\'' +
                ", createDate=" + createDate +
                ", fkOrder=" + fkOrder +
                '}';
    }
}
