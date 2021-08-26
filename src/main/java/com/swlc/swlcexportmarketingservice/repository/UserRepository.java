package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.Order;
import com.swlc.swlcexportmarketingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserByEmail(String email);

    User findUserById(int id);

    @Query(value = "SELECT COUNT(*) FROM USER", nativeQuery = true)
    int getCountOfUser();
}
