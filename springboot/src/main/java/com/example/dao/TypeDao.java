package com.example.dao;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface TypeDao extends Mapper<Type> {

	List<Type> findBySearch(@Param("search") Type search);

}
