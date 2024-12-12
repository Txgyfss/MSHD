package com.example.dao;

import com.example.entity.Address;
import com.example.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AddressDao extends Mapper<Address> {

	List<Address> findBySearch(@Param("search") Address search, @Param("userId") Integer userId, @Param("role") Integer role);

	@Select("select * from address where userId = #{userId} and role = #{role}")
	List<Address> findByUserIdAndRole(@Param("userId") Integer userId, @Param("role") Integer role);

}
