package com.tensquare.article.dao;

import com.tensquare.article.pingan.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-05-05 13:59:18
 * @describe:
 */
@Mapper
public interface ImageDao {
    void addImageList(@Param("imageList") List<Image> imageList);

    List<Map<String, Object>> getListImage(@Param("idList")String[] idList,@Param("startDate")String startDate,@Param("endDate")String endDate);

    List<String> getDateStrList();
}
