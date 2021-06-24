package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.DeliveryDetail;
import com.swlc.swlcexportmarketingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<DeliveryDetail,Integer> {

    DeliveryDetail findDeliveryDetailByUser(User user);
}
