package com.example.entity;

import javax.persistence.*;

/**
 * 商品分类
 */
@Table(name = "type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 分类名称 */
	@Column(name = "name")
	private String name;
	/** 分类描述 */
	@Column(name = "description")
	private String description;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
