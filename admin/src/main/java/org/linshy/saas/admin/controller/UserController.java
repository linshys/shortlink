package org.linshy.saas.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.common.convention.result.Results;
import org.linshy.saas.admin.dto.req.UserLoginReqDTO;
import org.linshy.saas.admin.dto.req.UserRegisterReqDTO;
import org.linshy.saas.admin.dto.req.UserUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.UserActualRespDTO;
import org.linshy.saas.admin.dto.resp.UserLoginRespDTO;
import org.linshy.saas.admin.dto.resp.UserRespDTO;
import org.linshy.saas.admin.service.UserService;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制层
 */

@RestController
@RequiredArgsConstructor
public class UserController {

//    @Autowired
//    private UserService userService;
    private final UserService userService;

    /**
     * 根据用户名查询用户信息
     */
    @GetMapping("/api/short-link/admin/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByUsername(username);

        return Results.success(result);
    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
    @GetMapping("/api/short-link/admin/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByUsername(username);

        return Results.success(BeanUtil.toBean(result, UserActualRespDTO.class));
    }



    /**
     * 判断用户名是否存在
     */
    @GetMapping("/api/short-link/admin/v1/user/has-username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        Boolean b = userService.hasUsername(username);
        return Results.success(b);
    }

    /**
     * 注册用户
     */

    @PostMapping("/api/short-link/admin/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }

    /**
     * 修改用户
     */
    @PutMapping("/api/short-link/admin/v1/user")
    public Result<Void> update(@RequestBody UserUpdateReqDTO requestParam)
    {
        userService.update(requestParam);
        return Results.success();
    }

    /**
     * 用户登录
     */
    @PostMapping("/api/short-link/admin/v1/user/login")
    public Result<UserLoginRespDTO>login(@RequestBody UserLoginReqDTO requestParam)
    {
        return Results.success(userService.login(requestParam));
    }


    /**
     * 检查用户是否已经登录
     * @param username 用户名
     * @param token 用户令牌
     * @return  ture or false
     */
    @GetMapping("/api/short-link/admin/v1/user/check-login")
    public Result<Boolean>checkLogin(@RequestParam("username") String username, @RequestParam("token")String token){

        return Results.success(userService.checkLogin(username,token));
    }

    /**
     * 用户登出
     * @param username 用户名
     * @param token 用户令牌
     * @return 空
     */
    @DeleteMapping("/api/short-link/admin/v1/user/logout")
    public Result<Void>logout(@RequestParam("username") String username, @RequestParam("token")String token)
    {
        userService.logout(username,token);
        return Results.success();
    }



}
