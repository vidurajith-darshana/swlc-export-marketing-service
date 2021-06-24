package com.swlc.swlcexportmarketingservice.repository;

import com.swlc.swlcexportmarketingservice.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findUserByEmail(String email);

    User findUserById(int id);
}
