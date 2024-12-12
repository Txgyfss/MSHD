package com.example.entity;

import javax.persistence.*;

/**
 * 商品管理
 */
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 商品图片 */
	@Column(name = "img")
	private String img;
	/** 商品名称 */
	@Column(name = "name")
	private String name;
	/** 商品分类 */
	@Transient
	private String type;
	/** 商品地区 */
	@Column(name = "location")
	private String location;
	/** 是否热门 */
	@Column(name = "ishot")
	private String ishot;
	/** 商品价格 */
	@Column(name = "price")
	private Double price;
	/** 商品原价 */
	@Column(name = "originalprice")
	private Double originalprice;
	/** 商品型号 */
	@Column(name = "goodsmodel")
	private String goodsmodel;
	/** 商品介绍 */
	@Column(name = "description")
	private String description;
	/** 商品分类Id */
	@Column(name = "typeId")
	private Integer typeId;

	/** 库存 */
	@Column(name = "goodsnum")
	private Integer goodsnum;
	@Transient
	private Integer num;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getGoodsnum() {
		return goodsnum;
	}

	public void setGoodsnum(Integer goodsnum) {
		this.goodsnum = goodsnum;
	}

	public String getIshot() {
		return ishot;
	}

	public void setIshot(String ishot) {
		this.ishot = ishot;
	}

	public String getGoodsmodel() {
		return goodsmodel;
	}

	public void setGoodsmodel(String goodsmodel) {
		this.goodsmodel = goodsmodel;
	}

	public Double getOriginalprice() {
		return originalprice;
	}

	public void setOriginalprice(Double originalprice) {
		this.originalprice = originalprice;
	}
}
