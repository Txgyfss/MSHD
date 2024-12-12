package com.example.entity;

import javax.persistence.*;
import java.util.List;

/**
 * 评论
 */
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	/** 评论内容 */
	@Column(name = "content")
	private String content;
	/** 评论时间 */
	@Column(name = "time")
	private String time;
	/** 评论人id */
	@Column(name = "userId")
	private Integer userId;
	/** 评论人角色 */
	@Column(name = "role")
	private Integer role;
	/** 关联id */
	@Column(name = "relId")
	private Integer relId;
	/** 父ID */
	@Column(name = "parentId")
	private Integer parentId;
	@Transient
	private String userName;
	@Transient
	private List<Comment> children;
	@Transient
	private String avatar;

	@Transient
	private String goodName;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Comment> getChildren() {
		return children;
	}

	public void setChildren(List<Comment> children) {
		this.children = children;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getRelId() {
		return relId;
	}

	public void setRelId(Integer relId) {
		this.relId = relId;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
}
