package com.example.entity;

import javax.persistence.*;

/**
 * 角色用户父类
 */
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /** 用户名 */
    @Column(name = "username")
    private String username;
    /** 密码 */
    @Column(name = "password")
    private String password;
    /** 角色标识 */
    @Column(name = "role")
    private Integer role;
    /** 新密码 */
    @Transient
    private String nmm;
    /** 头像 */
    @Column(name = "avatar")
    private String avatar;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getNmm() {
        return nmm;
    }

    public void setNmm(String nmm) {
        this.nmm = nmm;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
