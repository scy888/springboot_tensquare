package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public void add(Label label) {
        label.setId(idWorker.nextId()+"");
        String count = label.getCount();
        String s1 = count.substring(0, 3);
        String s2 = count.substring(7);
        String countend=s1+"****"+s2;
        label.setCount(countend);
        labelDao.save(label);
    }

    public Label findById(String labelId) {
        Label label = labelDao.findById(labelId).get();
       String count = label.getCount();
        String s1 = count.substring(0, 3);
        String s2 = count.substring(7);
        String countend=s1+"****"+s2;
        label.setCount(countend);
        return label;
    }
    public void update(Label label) {
        //label.setId(label.getId());
        labelDao.save(label);
    }

    public void deleteById(String labelId) {
      labelDao.deleteById(labelId);
    }

    /**
     * 条件查询
     */
    public List<Label> findSearch(Map<String, Object> map) {
        //设置查询条件
         List<Predicate> predicates=new ArrayList<>();
        Specification<Label> specification=new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                //标签名称 like
                if (!StringUtils.isEmpty((String)map.get("labelname"))){
                    predicates.add(cb.like(root.get( "labelname").as(String.class),"%"+(String)map.get("labelname")+"%"));
                }
                //状态 =
                if (!StringUtils.isEmpty((String)map.get("state"))){
                 predicates.add(cb.equal(root.get("state").as(String.class),(String)map.get("state")));
                }
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        List<Label> labels = labelDao.findAll(specification);
        return labels;
    }

    public Page<Label> findPage(Map<String, Object> map, int page, int size) {
        Specification<Label> specification=new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate p1=null;
                Predicate p2=null;
                if (!StringUtils.isEmpty(map.get("labelname"))){
                     p1 = criteriaBuilder.like(root.get("labelname").as(String.class), "%" + map.get("labelname") + "%");
                }
                if (!StringUtils.isEmpty(map.get("state"))){
                     p2 = criteriaBuilder.equal(root.get("state").as(String.class), map.get("state"));
                }
                return criteriaBuilder.and(p1,p2);
            }
        };
        //Sort sort=new Sort(Sort.Direction.DESC,"id");
        //Pageable pageable=new PageRequest(page-1, size, sort);
        Page<Label> labelPage = labelDao.findAll(specification, PageRequest.of(page-1, size, Sort.Direction.DESC,"id"));
        return labelPage;
    }
}
