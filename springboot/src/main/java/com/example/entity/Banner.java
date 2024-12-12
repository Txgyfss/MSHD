package com.example.entity;

import javax.persistence.*;

/**
 * 广告位展示管理
 */
@Table(name = "banner")
public class Banner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 图片 */
	@Column(name = "img")
	private String img;
	/** 商品Id */
	@Column(name = "goodsId")
	private Integer goodsId;
	/** 商品名称 */
	@Transient
	private String goodName;

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



	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
}
