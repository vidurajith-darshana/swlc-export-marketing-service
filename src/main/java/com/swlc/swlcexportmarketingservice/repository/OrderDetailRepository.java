package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,Integer> {
    @Query("SELECT od FROM OrderDetail od WHERE od.id = :id")
    List<OrderDetail> getOrderDetailByOrderId(int id);
}
