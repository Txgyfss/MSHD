package com.example.dao;

import com.example.entity.Cart;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CartDao extends Mapper<Cart> {

	List<Cart> findBySearch(@Param("search") Cart search);

	@Select("select * from cart where userId = #{id}")
	List<Cart> findByUserId(@Param("id") Integer id);
}
