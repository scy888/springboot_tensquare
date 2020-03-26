package com.tensquare.article.jiekou;

import com.tensquare.article.pojo.Province;

import java.util.List;
import java.util.Map;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.article.jiekou
 * @date: 2020-02-04 23:16:11
 * @describe:
 */
public interface ProvinceServince {
    List<Province> findAll();

    void addProvince(String paramJson);

    void updateById(Province province);

    void delectByIds(List<String> idList);

    List<Map<String, Object>> selectByNameAndCode(String provinceName, String provinceCode);
}
