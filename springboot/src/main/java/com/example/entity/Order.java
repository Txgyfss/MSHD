package com.example.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 订单
 */
@Table(name = "orderInfo")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 订单id */
	@Column(name = "orderId")
	private String orderId;
	/** 用户ID */
	@Column(name = "userId")
	private Integer userId;
	/** 商品ID */
	@Column(name = "goodsId")
	private Integer goodsId;
	/** 地址ID */
	@Column(name = "addressId")
	private Integer addressId;
	/** 数量 */
	@Column(name = "num")
	private Integer num;
	/** 下单时间 */
	@Column(name = "time")
	private String time;
	/** 订单总价格 */
	@Column(name = "price")
	private Double price;
	@Column(name = "status")
	private String status;
	@Transient
	private String userName;
	@Transient
	private Goods goods;
	@Transient
	private String name;
	@Transient
	private String address;
	@Transient
	private String phone;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
