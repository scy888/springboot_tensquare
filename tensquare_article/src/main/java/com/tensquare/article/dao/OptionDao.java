package com.tensquare.article.dao;

import com.tensquare.article.pojo.Option;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.dao
 * @date: 2020-02-06 14:38:24
 * @describe:
 */
@Mapper
public interface OptionDao {


    void addOptionList(@Param("optionList") List<Option> optionList);

    void deleteOptionByRelationId(@Param("idList")List<String> idList);

    void deleteOptionBySpectionId(String spectionId);

    List<Map<String, Object>> selectOptionListBySpectionId(String spection_id);

    List<Option> findSpectionListById(String spectionId);

    void addOption(Map<String,Object> map);

    void deleteOptionBySpectionIds(@Param("ids") String[] ids);

    void updateOption(Option option);

    List<Option> selectAllOption();

    Integer getTatolCount();

    List<Option> selectPage(@Param("index")Integer index,@Param("pageSize")Integer pageSize);

    List<Option> getOptionListByStatus(@Param("optionStatusList") List<String> spection_code_list);

    void updateOptionList(@Param("optionList") List<Option> optionList);
}
