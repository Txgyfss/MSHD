package com.example.entity;

import javax.persistence.*;

/**
 * 系统公告
 */
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 公告标题 */
	@Column(name = "name")
	private String name;
	/** 公告内容 */
	@Column(name = "content")
	private String content;
	/** 发布时间 */
	@Column(name = "time")
	private String time;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
