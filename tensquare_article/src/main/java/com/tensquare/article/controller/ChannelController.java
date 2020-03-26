package com.tensquare.article.controller;

import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.PageInfo;
import com.tensquare.article.pojo.ListAndArray;
import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.tensquare.article.pojo.Channel;
import com.tensquare.article.service.ChannelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import utils.ValidatorUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/channel")
public class ChannelController {

    @Autowired
    private ChannelService channelService;
    private HttpServletRequest request;
    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);
    /**
     * 查询全部数据
     * @return
     */
    @RequestMapping(/*value = "/{pageNum}/{pageSize}",*//*method = RequestMethod.POST*/)
    public Result findAll(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam Integer pageSize
            /*@RequestBody String paramJson*/) {
       /* JSONObject jsonObject = JSON.parseObject(paramJson);
        Integer pageNum = jsonObject.getInteger("pageNum");
        Integer pageSize = jsonObject.getInteger("pageSize");*/
        PageInfo<Channel> pageInfo = channelService.findAll(pageNum, pageSize);
        return new Result(true, StatusCode.OK, "查询成功", pageInfo);
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
      /*  Map<String, Object> map = new HashMap<>();
        map.put("id", id);*/
        Channel channel = channelService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", channel);
    }

    @RequestMapping(value = "/finds", method = RequestMethod.POST)
    public Result findListByIds(@RequestBody Map<String, List<String>> map) {
        /** 根据id批量查询 */
        List<String> ids = map.get("idList");
        List<Channel> channelList = new ArrayList<>();
        for (String id : ids) {
            Channel channel = (Channel) this.findById(id).getData();
            channelList.add(channel);
        }
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    /**
     * 分页+多条件查询
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {

        Page<Channel> pageList = channelService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Channel>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", channelService.findSearch(searchMap));
    }

    @ApiOperation(value = "添加频道", notes = "添加频道")
    @ApiImplicitParam(name = "map", value = "集合map", dataType = "Map", paramType = "body")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(@RequestBody Map<String, Object> map) {
        Map<String, Object> paramMap = this.validator(map);
        if (!paramMap.isEmpty()) {
            logger.info("paramMap的值为：" + JSON.toJSONString(paramMap));
            return new Result(false, StatusCode.ERROR, "增加失败,参数不能为空", paramMap);
        }
        String s="[{\"height\":\"168cm\",\"weight\":\"55kg\"},{\"height\":\"170cm\",\"weight\":\"60kg\"}]";
        //[{"height":"168cm","weight":"55kg"},{"height":"170cm","weight":"60kg"}]
        Channel channel = JSON.parseObject(JSON.toJSONString(map), Channel.class);
        channelService.add(channel);
        logger.info(JSON.parseObject(JSON.toJSONString(map)).getString("state"));
        //logger.info(JSON.parseObject(JSON.toJSONString(map)).getJSONObject("state").getString("height"));
        logger.info(JSON.parseObject(JSON.toJSONString(map)).getJSONArray("state").getJSONObject(1).getString("weight"));
        return new Result(true, StatusCode.OK, "增加成功");
    }

    private Map<String, Object> validator(Map<String, Object> map) {
        Map<String, Object> returnMap = new HashMap<>();
        String name = (String) map.get("name");
        String state = (String) map.get("state");

        if (name == "" || name == null) {
            returnMap.put("name", "名称为空");
        }
        if (state == "" || state == null) {
            returnMap.put("state", "状态为空");
        }
        return returnMap;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody String channelJson, @PathVariable String id) {
        // Channel channel = JSON.parseObject(channelJson, Channel.class);
        JSONObject jsonObject = JSON.parseObject(channelJson);
        Channel channel = new Channel();
        channel.setId(id);
        channel.setName(jsonObject.getString("name"));
        channel.setState(jsonObject.getString("state"));
        channel.setIsHot(jsonObject.getString("isHot"));
        channel.setCreateDate(jsonObject.getDate("createDate"));
        channel.setThumbUp(jsonObject.getString("thumbUp"));
        //channel.setId(id);
        channelService.update(channel);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @RequestMapping(/*value = "/{id}",*/method = RequestMethod.DELETE)
    public Result delete(@RequestBody String idJson) {
        JSONObject jsonObject = JSON.parseObject(idJson);
        String id = jsonObject.getString("id");
        //String id = (String) map.get("id");
        channelService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping(value = "/validata", method = RequestMethod.POST)
    public Map<String, Object> validataStrResult(@RequestBody String paramsJson) {
        Map<String, Object> paramsMap = JSON.parseObject(paramsJson, Map.class);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap = validataResult(paramsMap);
        if (returnMap == null) {
            return returnMap;
        }
        return returnMap;
    }

    private Map<String, Object> validataResult(Map<String, Object> paramsMap) {
        if (paramsMap == null) {
            paramsMap.put("msg", "参数不能为空");
        }
        ValidatorUtils validatorUtils = new ValidatorUtils();
        validatorUtils.setMessage("参数不为空");
        validatorUtils.setPass(true);
        List<ValidatorUtils.ValidatorRule> validatorRules = new ArrayList<>();
        validatorRules.add(new ValidatorUtils.ValidatorRule("id", "id不为空", ValidatorUtils.ValidatorType.NOTNULL));
        validatorRules.add(new ValidatorUtils.ValidatorRule("name", "name不为空", ValidatorUtils.ValidatorType.NOTNULL));
        validatorRules.add(new ValidatorUtils.ValidatorRule("state", "state不为空", ValidatorUtils.ValidatorType.NOTNULL));
        validatorRules.add(new ValidatorUtils.ValidatorRule("name", "name不为空", ValidatorUtils.ValidatorType.NOTNULL, "北京卫视"));
        validatorRules.add(new ValidatorUtils.ValidatorRule("state", "state不为空", ValidatorUtils.ValidatorType.NOTNULL, "0"));
        validatorUtils.setValidatorRules(validatorRules);
        paramsMap.put("msg", validatorUtils.getMessage());
        paramsMap.put("isPass", validatorUtils.isPass());
        paramsMap.put("validatorRules", validatorUtils.getValidatorRules());
        return paramsMap;
    }

    @PostMapping("/addList")
    public Result addList(@RequestBody String paramJson) {
        ListAndArray listAndArray = JSON.parseObject(paramJson).toJavaObject(ListAndArray.class);
        channelService.addList(listAndArray);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @RequestMapping("/addArray")
    public Result addArray(@RequestBody Map<String, Object> map) {
        ListAndArray listAndArray = JSON.parseObject(JSON.toJSONString(map), ListAndArray.class);
        Channel[] channels = JSON.parseObject(JSON.toJSONString(map)).getJSONArray("channelList").toJavaObject(Channel[].class);
        //Channel[] channels = (Channel[]) jsonObject.get("channels");//转换异常
       // Channel[] channels = listAndArray.getChannels();
        channelService.addArray(channels);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @PostMapping("/addMap")
    public Result addMap(@RequestBody Map<String, List<Channel>> paramMap) {
        //List<Channel> channelList = paramMap.get("channels");
        channelService.addMap(paramMap);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @PostMapping("/addMapArray")
    public Result addMapArray(@RequestBody Map<String, Channel[]> paramMap) {
        Channel[] channelArray =  paramMap.get("channelList");
        channelService.addMapArray(channelArray);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    @RequestMapping("/delArray")
    public Result deleteByIds(@RequestBody ListAndArray listAndArray) {
        System.out.println(listAndArray.getIds().toString());
        listAndArray = JSON.parseObject(JSON.toJSONString(listAndArray), ListAndArray.class);
        for (String id : listAndArray.getIds()) {
            channelService.deleteById(id);
        }
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @PostMapping("/delList")
    public Result delByIds(@RequestBody Map<String, Object> map) {
       /* ListAndArray listAndArray = JSON.parseObject(JSON.toJSONString(map), ListAndArray.class);
        for (String id : listAndArray.getIdList()) {
            channelService.deleteById(id);
        }*/
        List<String> ids = JSON.parseObject(JSON.toJSONString(map)).getJSONArray("idList").toJavaList(String.class);
        for (String id : ids) {
            channelService.deleteById(id);
        }
        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping("/delString/{str}")
    public Result delByIdStr(/*@RequestParam(defaultValue = "1223852843916726272,1223852844357128192")String str*/
            /* @RequestBody Map<String,String> map*/ @PathVariable String str) {
        //String str = map.get("strs");

        String[] split = str.split(",");
        for (String id : split) {
            channelService.deleteById(id);
        }

        return new Result(true, StatusCode.OK, "删除成功");
    }

    @RequestMapping("/strList")
    public Result delById(@RequestBody String paramJson) {
        JSONObject jsonObject = JSON.parseObject(paramJson);
       // String ids = jsonObject.getString("ids");
        String ids = jsonObject.getObject("ids", String.class);
       // String ids = jsonObject.getJSONObject("ids").toJavaObject(String.class);报错
        String[] split = ids.split(",");
        for (String s : split) {
            channelService.delById(s);
        }
        return new Result(true, StatusCode.OK, "删除成功");

    }

    @RequestMapping(value = "/desc/{isHost}", method = RequestMethod.GET)
    public Result selectByIsHot(@PathVariable String isHost) {
        List<Channel> channelList = channelService.selectByIsHot(isHost);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    @RequestMapping(value = "/update/{id}")
    public Result updateByThumbUp(@PathVariable String id) {
        try {
            channelService.updateByThumbUp(id);
            return new Result(true, StatusCode.OK, "点赞成功");
        } catch (Exception e) {
            return new Result(true, StatusCode.OK, e.getMessage());
        }
    }

    @RequestMapping("/and/{state1}-{name1}")//带中文用谷歌浏览器
    public Result selectByNameAndState(@PathVariable String state1, @PathVariable String name1) {
        Channel channel = new Channel();
        channel.setState(state1);
        channel.setName(name1);
        logger.info("state:" + channel.getState() + ",name:" + channel.getName());
        List<Channel> channelList = channelService.selectByNameAndState(name1, state1);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    @RequestMapping("/and1")//带中文用谷歌浏览器
    public Result selectByStateAndName(@RequestParam(defaultValue = "2") String state, @RequestParam(defaultValue = "武汉") String name) {
        Channel channel = new Channel();
        channel.setState(state);
        channel.setName(name);
        logger.info("state:" + channel.getState() + ",name:" + channel.getName());
        List<Channel> channelList = channelService.selectByNameAndState(name, state);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    @RequestMapping("/and")
    public Result selectByStateAndName(@RequestBody String paramJson) {
        Channel channel = JSON.parseObject(paramJson).toJavaObject(Channel.class);
        String name1 = channel.getName();
        String state1 = channel.getState();
        Map<String, Object> map = JSON.parseObject(paramJson).toJavaObject(Map.class);
        String name2 = map.get("name").toString();
        String state2 = map.get("state").toString();
        JSONObject jsonObject = JSON.parseObject(paramJson);
        String name3 = jsonObject.getString("name");
        String state3 = jsonObject.getString("state");
        List<Channel> channelList = channelService.selectByStateAndName(name1, state1);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    @RequestMapping("/and/{createDate}/{isHot}")
    public Result selectByCreateDateAndIsHot(@PathVariable Date createDate, @PathVariable String isHot) {
        List<Channel> channelList = channelService.selectByCreateDateAndIsHot(createDate, isHot);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }

    public static void main(String[] args) {
        /** 数据库的date日期做参数传递时转换成date日期时要减去14个小时*/
        Date date = new Date(2020 - 1900, 1 - 1, 29, 0 - 14, 00, 00);
        System.out.println(date);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2020);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 29);
        calendar.setTime(new Date(2020 - 1900, 1 - 1, 29, 15 - 14, 06, 27));
        System.out.println(calendar.getTime());
        //Wed Jan 29 20:00:00 CST 2020
    }

    @RequestMapping("/and2")//date时间参数postman(key,value)形式提交时间对不上查询为空
    public Result selectByCreateDateAndIsHot2(@RequestParam(defaultValue = "Wed Jan 29 01:06:27 CST 2020") Date createDate,
                                              @RequestParam(defaultValue = "0") String isHot) {
        /*JSONObject jsonObject = JSON.parseObject(paramJson);
        Date createDate = jsonObject.getDate("createDate");
        String isHot = jsonObject.getString("isHot");*/
        Channel channel = new Channel();
        channel.setCreateDate(createDate);
        logger.info("createDate:" + createDate);
        List<Channel> channelList = channelService.selectByCreateDateAndIsHot(createDate, isHot);
        return new Result(true, StatusCode.OK, "查询成功", channelList);
    }
    @RequestMapping("/nameList")
    public Result selectByNameList(@RequestBody String paramJson/*,@RequestBody Map<String,String[]> map*/){
        JSONObject jsonObject = JSON.parseObject(paramJson);
       // List<String> nameList = jsonObject.getJSONArray("nameList").toJavaList(String.class);
         String[] nameList = jsonObject.getJSONArray("nameList").toJavaObject(String[].class);
        //String[] nameList= jsonObject.getObject("nameList", String[].class);
         //List<String> nameList= jsonObject.getObject("nameList", List.class);

        //String[] nameList= (String[]) jsonObject.get("nameList");报错
        //String[] nameList = jsonObject.getJSONObject("nameList").toJavaObject(String[].class);转换失败
        //List<String> nameList= (List<String>) jsonObject.get("nameList");建议不要这样写

        //List<String> nameList = (List<String>) jsonObject.toJavaObject(Map.class).get("nameList");
        //List<String> nameList = (List<String>) map.get("nameList");
        //String[] nameList =  map.get("nameList");
        List<Channel> channelList=new ArrayList<>();
        for (String name : nameList) {
            List<Channel>  list= channelService.selectByName(name);
            channelList.addAll(list);
        }
        logger.info("channelList的长度size:"+channelList.size());
        return new Result(true, StatusCode.OK, "查询成功",channelList);
    }
}