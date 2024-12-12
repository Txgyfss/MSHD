package com.example.dao;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AdminDao extends Mapper<Admin> {

	@Select("select * from admin where `username` = #{name}")
	Admin findByUsername(@Param("name") String name);

	List<Admin> findBySearch(@Param("search") Admin search);

}
