package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("label")
public class LabelController {
    @Autowired
    private LabelService labelService;
    /**
     * 查询全部
     */
     @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
    List<Label> labelList= labelService.findAll();
    return new Result(true, StatusCode.OK,"查询成功",labelList);
     }
    /**
     * 添加
     */
      @RequestMapping(method = RequestMethod.POST)
      public Result add(@RequestBody Label label){
      labelService.add(label);
      return new Result(true, StatusCode.OK, "添加成功");
      }
    /**
     * 通过编号查询
     */
      @RequestMapping(value = "/{labelId}",method = RequestMethod.GET)
    public Result findById(@PathVariable String labelId){
        Label label=  labelService.findById(labelId);
        return new Result(true, StatusCode.OK, "查询成功",label);
      }
    /**
     * 修改
     */
     @RequestMapping(value = "{labelId}",method = RequestMethod.PUT)
    public Result update(@PathVariable String labelId,@RequestBody Label label){
         label.setId(labelId);
         labelService.update(label);
         return new Result(true, StatusCode.OK, "修改成功");
     }
    /**
     * 删除
     */
    @RequestMapping(value = "{labelId}",method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String labelId){
        labelService.deleteById(labelId);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 条件拼接查询(动态语句查询)
     */
    @PostMapping("/search")
    public Result findSearch(@RequestBody Map<String,Object> map){
       List<Label> labels= labelService.findSearch(map);
       return Result.success("查询成功", labels);
    }
    /**
     * 条件拼接查询(动态语句查询) ,分页
     */
    @PostMapping("/search/{page}/{size}")
    public Result findPage(@RequestBody Map<String,Object> map,
                             @PathVariable int page,
                             @PathVariable int size){
       Page<Label> labelPage= labelService.findPage(map,page,size);
     return Result.success("查询成功",new PageResult<Label>(labelPage.getTotalElements(),labelPage.getContent()));
    }
}
