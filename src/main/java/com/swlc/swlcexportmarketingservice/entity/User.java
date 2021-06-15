package com.swlc.swlcexportmarketingservice.entity;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "USER")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @NotNull
    @Column(name = "FIRST_NAME")
    private String firstName;

    @NotNull
    @Column(name = "LAST_NAME")
    private String lastName;

    @NotNull
    @Column(name = "EMAIL")
    private String email;

    @NotNull
    @Column(name = "PASSWORD")
    private String password;

    @NotNull
    @Column(name = "ROLE")
    private String role;

    @Column(name = "PREFERRED_LANGUAGE")
    private String preferredLanguage;

    @Column(name = "VERIFY_CODE")
    private String verifyCode;

    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate = new Date();

    @OneToMany(mappedBy = "fkUser",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Order> fkOrders;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }
}
