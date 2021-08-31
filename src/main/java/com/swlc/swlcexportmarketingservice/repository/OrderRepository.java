package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    Order findOrderByOrderRef(String ref);

    @Query(value = "SELECT * FROM ORDERS o WHERE YEAR(o.CREATE_DATE) = ?1 AND (?2 = 0 OR MONTH(o.CREATE_DATE) = ?2) " +
            "ORDER BY o.TOTAL desc LIMIT 10", nativeQuery = true)
    List<Order> getTop10OrdersByYearAndMonth(int yr, int mth);


    @Query(value = "SELECT * FROM ORDERS o WHERE o.FK_USER = ?1", nativeQuery = true)
    List<Order> getOrdersByUserId(int id);

    List<Order> findByFkUser(User user);

    @Query(value = "SELECT COUNT(*) FROM ORDERS o", nativeQuery = true)
    int getCountOfOrders();

    @Query(value = "SELECT * FROM ORDERS o WHERE o.ORDER_REF LIKE ?1%", nativeQuery = true)
    List<Order> getAllOrdersWithRef(String ref);


}
