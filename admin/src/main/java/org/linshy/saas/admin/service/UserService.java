package org.linshy.saas.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.admin.dao.entity.UserDO;
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
}
