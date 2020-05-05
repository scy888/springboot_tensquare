package com.tensquare.article.dao;

import com.tensquare.article.pingan.Image;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
}
