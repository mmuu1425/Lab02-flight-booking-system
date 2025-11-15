package com.flight.user.entity;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    private UUID userUid;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    private String email;

    // 默认构造函数（JPA要求）
    public User() {}

    // 全参构造函数
    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public UUID getUserUid() {
        return userUid;
    }

    public void setUserUid(UUID userUid) {
        this.userUid = userUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}