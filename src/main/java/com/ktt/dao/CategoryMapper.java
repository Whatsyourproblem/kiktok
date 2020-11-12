package com.ktt.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryMapper {

    // 通过类别名称来查询类别id
    @Select("select category_id from category where categoryname = #{categoryName}")
    String selectCategoryIdByCategoryName(String categoryName);

    // 向视频类别中插入数据
    @Insert("insert into video_category (video_id,category_id) values (#{videoId},#{categoryId})")
    int insertVideoAndCategory(@Param("videoId") String videoId,@Param("categoryId") String categoryId);
}
