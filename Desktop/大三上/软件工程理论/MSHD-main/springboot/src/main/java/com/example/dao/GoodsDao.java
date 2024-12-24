package com.example.dao;

import com.example.entity.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface GoodsDao extends Mapper<Goods> {

	List<Goods> findBySearch(@Param("search") Goods search);

	@Select("select * from goods where typeId = #{id}")
    List<Goods> findByTypeId(@Param("id") Integer id);

	@Select("select * from goods where name like concat ('%', #{name}, '%')")
    List<Goods> findByName(@Param("name") String name);

    List<Goods> findByHot();
}
