package org.linshy.saas.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.admin.dao.entity.UserDO;
import org.linshy.saas.admin.dto.req.UserLoginReqDTO;
import org.linshy.saas.admin.dto.req.UserRegisterReqDTO;
import org.linshy.saas.admin.dto.req.UserUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.UserLoginRespDTO;
import org.linshy.saas.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);

    /**
     *
     * @param username 用户名
     * @return Ture：用户名不存在， False：用户名存在
     */
    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO requestParam);

    void update(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     * @param requestParam 用户登录请求参数
     * @return  用户登录返回参数 Token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);

    void logout(String username, String token);
}
