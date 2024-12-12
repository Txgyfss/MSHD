package com.example.dao;

import com.example.entity.Advertising;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface AdvertisingDao extends Mapper<Advertising> {

	List<Advertising> findBySearch(@Param("search") Advertising search);

	@Select("select * from advertising where goodsId = #{id}")
    List<Advertising> findByGoodsId(@Param("id") Integer id);

}
