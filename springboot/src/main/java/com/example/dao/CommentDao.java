package com.example.dao;

import com.example.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CommentDao extends Mapper<Comment> {

	List<Comment> findBySearch(@Param("search") Comment search, @Param("userId") Integer userId, @Param("role") Integer role);

	@Select("select * from comment where relId = #{relId} and parentId = #{parentId}")
	List<Comment> findByGoodsIdAndParentId(@Param("relId") Integer goodsId, @Param("parentId") Integer parentId);

}
