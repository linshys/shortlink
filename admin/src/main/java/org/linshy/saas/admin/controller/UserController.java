package org.linshy.saas.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.common.convention.result.Results;
import org.linshy.saas.admin.dto.req.UserRegisterReqDTO;
import org.linshy.saas.admin.dto.resp.UserActualRespDTO;
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
    @GetMapping("/api/short-link/v1/user/{username}")
    public Result<UserRespDTO> getUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByUsername(username);

        return Results.success(result);
    }

    /**
     * 根据用户名查询用户无脱敏信息
     */
    @GetMapping("/api/short-link/v1/actual/user/{username}")
    public Result<UserActualRespDTO> getActualUserByUsername(@PathVariable("username") String username){
        UserRespDTO result = userService.getUserByUsername(username);

        return Results.success(BeanUtil.toBean(result, UserActualRespDTO.class));
    }



    /**
     * 判断用户名是否存在
     */
    @GetMapping("/api/short-link/v1/user/has-Username")
    public Result<Boolean> hasUsername(@RequestParam("username") String username){
        Boolean b = userService.hasUsername(username);
        return Results.success(b);
    }

    @PostMapping("/api/short-link/v1/user")
    public Result<Void> register(@RequestBody UserRegisterReqDTO requestParam){
        userService.register(requestParam);
        return Results.success();
    }



}
