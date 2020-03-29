package com.tensquare.article.dao;

import com.tensquare.article.pojo.WeightSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-03-29 17:08:10
 * @describe:
 */
@Mapper
public interface WeightDao {//CURRENT_TIMESTAMP
     @Insert("insert into tb_weight(weight_id,weight_key,weight_value,create_date) values(#{weightId},#{weightKey},#{weightValue},CURRENT_TIMESTAMP)")
    void addWeight(WeightSetting weightSetting);
}
