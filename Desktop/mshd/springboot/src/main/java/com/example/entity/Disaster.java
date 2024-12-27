package com.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Disaster {
    private String id; // 主键
    private String province; // 省份
    private String city; // 市区
    private String country; // 县区
    private String town; // 镇区
    private String village; // 村庄
    private String category; // 房屋破坏类别
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "CST")
    private Date date; // 事件发生日期与时间
    private String location; // 具体位置
    private String detail; // 详细描述
    private String reportingUnit; // 报告单位

    // 新增字段
    private String sourceCategory; // 来源大类
    private String sourceSubCategory; // 来源子类
    private String carrierForm; // 载体形式
    private String link; // 链接
    private String disasterMajorCategory; // 灾情大类
    private String disasterSubCategory; // 灾情子类
    private String disasterIndicator; // 灾情指标
private String disastercode;
    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getDate() {
        System.out.println("Getting date: " + date);
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getDisastercode() {
        return disastercode;
    }

    public void setDisastercode(String disastercode) {
        this.disastercode = disastercode;
    }
    public String getReportingUnit() {
        return reportingUnit;
    }

    public void setReportingUnit(String reportingUnit) {
        this.reportingUnit = reportingUnit;
    }

    public String getSourceCategory() {
        return sourceCategory;
    }

    public void setSourceCategory(String sourceCategory) {
        this.sourceCategory = sourceCategory;
    }

    public String getSourceSubCategory() {
        return sourceSubCategory;
    }

    public void setSourceSubCategory(String sourceSubCategory) {
        this.sourceSubCategory = sourceSubCategory;
    }

    public String getCarrierForm() {
        return carrierForm;
    }

    public void setCarrierForm(String carrierForm) {
        this.carrierForm = carrierForm;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDisasterMajorCategory() {
        return disasterMajorCategory;
    }

    public void setDisasterMajorCategory(String disasterMajorCategory) {
        this.disasterMajorCategory = disasterMajorCategory;
    }

    public String getDisasterSubCategory() {
        return disasterSubCategory;
    }

    public void setDisasterSubCategory(String disasterSubCategory) {
        this.disasterSubCategory = disasterSubCategory;
    }

    public String getDisasterIndicator() {
        return disasterIndicator;
    }

    public void setDisasterIndicator(String disasterIndicator) {
        this.disasterIndicator = disasterIndicator;
    }

    // toString 方法
    @Override
    public String toString() {
        return "DisasterInfo{" +
                "id='" + id + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", category='" + category + '\'' +
                ", date=" + date +
                ", location='" + location + '\'' +
                ", detail='" + detail + '\'' +
                ", reportingUnit='" + reportingUnit + '\'' +
                ", sourceCategory='" + sourceCategory + '\'' +
                ", sourceSubCategory='" + sourceSubCategory + '\'' +
                ", carrierForm='" + carrierForm + '\'' +
                ", link='" + link + '\'' +
                ", disasterMajorCategory='" + disasterMajorCategory + '\'' +
                ", disasterSubCategory='" + disasterSubCategory + '\'' +
                ", disasterIndicator='" + disasterIndicator + '\'' +
                '}';
    }
}
