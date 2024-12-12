package com.example.entity;

import javax.persistence.*;

/**
 * 购物车
 */
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 商品id */
	@Column(name = "goodsId")
	private Integer goodsId;
	/** 用户ID */
	@Column(name = "userId")
	private Integer userId;
	/** 数量 */
	@Column(name = "num")
	private Integer num;
	/** 商品名称 */
	@Transient
	private String goodsName;
	/** 商品图片 */
	@Transient
	private String img;
	@Transient
	private Double price;


	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
