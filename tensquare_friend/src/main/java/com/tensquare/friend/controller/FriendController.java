package com.tensquare.friend.controller;

import com.tensquare.friend.client.User2Client;
import com.tensquare.friend.serivce.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RequestMapping("/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;
    @Autowired
    private User2Client user2Client;
    /**
     * /like/{friendid}/{type}
     * 添加好友或非好友
     */
    @PutMapping("/like/{friendid}/{type}")
    public Result like(@PathVariable String friendid, @PathVariable String type, HttpServletRequest req){
        // 从token获取登陆用户的id
        Claims claims = (Claims) req.getAttribute("user_claims");
        if(null == claims){
            return new Result(false, StatusCode.ACCESSERROR, "没有权限");
        }
        String userId = claims.getId();

        // 判断是添加好友还是非好友, 好友type=1, 非好友：type=2
        if("1".equals(type)){
            friendService.like(userId,friendid);
            user2Client.updateFollowCount(userId,1);
            user2Client.updateFansCount(friendid,1);
        }else{
            // 添加非好友
            friendService.addNoFriend(userId,friendid);
        }
        return Result.success("操作成功");
    }
    //删除好友
    @DeleteMapping("/{friendId}")
    public Result deleteFriend(@PathVariable String friendId,HttpServletRequest req){
        Claims claims = (Claims) req.getAttribute("user_claims");
        if(null == claims){
            return new Result(false, StatusCode.ACCESSERROR, "没有权限");
        }
        String userId = claims.getId();

        friendService.deleteFriend(userId,friendId);
        user2Client.updateFansCount(friendId,-1);
        user2Client.updateFollowCount(userId,-1);
        return Result.success("删除好友成功...");
    }
}
