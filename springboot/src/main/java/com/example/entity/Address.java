package com.example.entity;

import javax.persistence.*;

/**
 * 收货地址
 */
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 用户ID */
	@Column(name = "userId")
	private Integer userId;
	/** 用户角色 */
	@Column(name = "role")
	private Integer role;
	/** 联系地址 */
	@Column(name = "address")
	private String address;
	/** 联系电话 */
	@Column(name = "phone")
	private String phone;
	/** 收货人 */
	@Column(name = "username")
	private String username;
	/** 购买人 */
	@Transient
	private String user;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
