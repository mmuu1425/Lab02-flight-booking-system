package com.flight.user.dto;

import java.util.UUID;

public class UserDTO {
    private UUID userUid;
    private String username;
    private String name;
    private String email;

    // 默认构造函数
    public UserDTO() {}

    // 从实体转换的构造函数
    public UserDTO(UUID userUid, String username, String name, String email) {
        this.userUid = userUid;
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