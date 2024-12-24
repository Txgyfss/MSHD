package com.example.entity;

import javax.persistence.*;

/**
 * 广告位展示管理
 */
@Table(name = "advertising")
public class Advertising {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 广告位展示图片
     */
    @Column(name = "img")
    private String img;
    /**
     * 广告位主标题
     */
    @Column(name = "title")
    private String title;

    /**
     * 广告位简介
     */
    @Column(name = "introducing")
    private String introducing;
    /**
     * 描述
     */
    @Column(name = "description")
    private String description;
    /**
     * 商品Id
     */
    @Column(name = "goodsId")
    private Integer goodsId;
    /**
     * 商品名称
     */
    @Column(name = "carrier")
    private String carrier;
    /**
     * 数据载体
     */
    @Column(name = "category")
    private String category;
    /**
     * 分类
     */
    @Column(name = "label")
    private String label;
    /**
     * 分类
     */
    @Transient
    private String goodName;

    @Transient
    private String goodLocation;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroducing() {
        return introducing;
    }

    public void setIntroducing(String introducing) {
        this.introducing = introducing;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {this.goodName = goodName;}

    public String getGoodLocation() {
        return goodLocation;
    }

    public void setGoodLocation(String goodLocation) {this.goodLocation = goodLocation;}
}
