package com.example.entity;

import java.util.List;

public class OrderDto {

    private List<Cart> list;
    private Integer addressId;

    public List<Cart> getList() {
        return list;
    }

    public void setList(List<Cart> list) {
        this.list = list;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }
}
