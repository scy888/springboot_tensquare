package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private HttpServletRequest request;
    /**
     * 查询所有吐糟列表
     */
    @GetMapping
    public Result fidAll(){
     List<Spit> spitList= spitService.findAll();
     return Result.success("查询成功", spitList);
    }
    /**
     * 通过id查询
     */
    @GetMapping("{spitId}")
    public Result findById(@PathVariable String spitId){
      Spit spit= spitService.findById(spitId);
        return Result.success("查询成功", spit);
    }
    /**
     * 添加吐糟
     */
    @PostMapping
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return Result.success("添加成功");
    }
    /**
     * 更改
     */
    @PutMapping("{spitId}")
    public Result update(@PathVariable String spitId,@RequestBody Spit spit){
       // spit.set_id(spitId);
        spitService.update(spit,spitId);
        return Result.success("修改成功");
    }
    /**
     * 删除
     */
    @DeleteMapping("{spitId}")
    public Result deleteById(@PathVariable String spitId){
        spitService.deleteById(spitId);
        return Result.success("删除成功");
    }
    /**
     * 根据动态条件搜索,分页
     * /search/{page}/{size}
     */
    @GetMapping("/search/{content}/{page}/{size}")
    public Result searchPage(@PathVariable String content,@PathVariable int page,@PathVariable int size){
      Page<Spit> spitPage= spitService.searchPage(content,page,size);
      return Result.success("查找成功",spitPage);
    }
    /**
     * 根据上级ID查询吐槽数据（分页）
     * /comment/{parentid}/{page}/{size}
     */
    @GetMapping("comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
       Page<Spit> spitPage= spitService.findByParentid(parentid,page,size);
       return Result.success("查询成功", spitPage);
    }
    /**
     * 吐糟点赞
     * /thumbup/{spitId}
     */
    @PutMapping("/thumbup/{spitId}")
    public Result updatethumbup(@PathVariable String spitId){
        Claims claims = (Claims) request.getAttribute("user");
        if (claims==null){
            return new Result(false, StatusCode.ACCESSERROR,"权限不足");
        }
        String userId = claims.getId();
        spitService.updatethumbup(spitId,userId);
        return Result.success("点赞成功");
    }
}
