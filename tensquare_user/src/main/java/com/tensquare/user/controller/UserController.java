package com.tensquare.user.controller;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utils.JwtUtil;

import javax.net.ssl.SNIMatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	protected HttpServletRequest request;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){

		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}
	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id ){
	/*	//获取前端传过来的token
		String authorization = request.getHeader("Authorization");
		//我们约定好的格式:"Bearer "+token
		if (authorization==null||!authorization.startsWith("Bearer " )){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		//提取token
		String token = authorization.substring(7);
		//解析token
		Claims claims = jwtUtil.parseJWT(token);
		if (claims==null){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}
		String roles = (String) claims.get("roles");
		if (StringUtils.isEmpty(roles)||!"admin".equals(roles)){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足");
		}*/
		Claims claims = (Claims) request.getAttribute("admin");
		if (claims==null){
			return new Result(false,StatusCode.ACCESSERROR, "没有权限");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	/**
	 * 发送验证码
	 */
	@PostMapping("/sendsms/{mobile}")
	public Result sendSmsCode(@PathVariable String mobile){
		userService.createSmsCode(mobile);
		return Result.success("发送验证码成功");
	}
	/**
	 * 用户注册
	 * /register/{code}
	 */
	@PostMapping("/register/{code}")
	public Result register(@RequestBody User user,@PathVariable String code){
		userService.register(user,code);
		return Result.success("注册成功");
	}
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
		String mobile = user.getMobile();
		String password = user.getPassword();
		user=userService.findByMobileAndPassword(mobile,password);
      if (user==null){
          return new Result(false, StatusCode.LOGINERROR, "用户名或密码错误");
      }
		//第一个参数是明文密码,没加密的,第二个参数是加密后的密码
		/*if (user == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
			return null;
		}*/
      //签发token
		String token = jwtUtil.createJWT(user.getId(), user.getNickname(), "user");
         Map<String,Object> tokenMap=new HashMap<>();
         tokenMap.put("token",token);
         tokenMap.put("name",user.getNickname());
         tokenMap.put("avatar",user.getAvatar());
		return Result.success("成功登录",tokenMap);
    }
	/**
	 * 	更改fanscount(粉丝)数方法,供添加好友微服务掉用
	 * 	"/fanscount/{num}"
	 */
	@PostMapping("/fanscount/{userId}/{num}")
   public void updateFansCount(@PathVariable String userId,@PathVariable int num){
		userService.updateFansCount(userId,num);
	}
	/**
	 * 	更改followcount(关注)数方法,供添加好友微服务掉用
	 * 	"/fanscount/{num}"
	 */
	@PostMapping("/followcount/{userId}/{num}")
	public void updateFollowCount(@PathVariable String userId,@PathVariable int num){
		userService.updateFollowCount(userId,num);
	}
}
