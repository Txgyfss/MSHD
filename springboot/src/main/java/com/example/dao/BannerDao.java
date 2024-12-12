package com.example.dao;

import com.example.entity.Banner;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface BannerDao extends Mapper<Banner> {

	List<Banner> findBySearch(@Param("search") Banner search);

	@Select("select * from banner where goodsId = #{id}")
    List<Banner> findByGoodsId(@Param("id") Integer id);

}
