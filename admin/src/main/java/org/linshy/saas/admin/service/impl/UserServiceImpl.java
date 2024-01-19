package org.linshy.saas.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linshy.saas.admin.dao.entity.UserDO;
import org.linshy.saas.admin.dao.mapper.UserMapper;
import org.linshy.saas.admin.dto.resp.UserRespDTO;
import org.linshy.saas.admin.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * 用户接口实现层
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {


    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        UserRespDTO result = new UserRespDTO();
        if(userDO != null){
            BeanUtils.copyProperties(userDO, result);       // 此方法需要判空才可以，否则会报错
            return result;
        } else {
            return null;
        }

    }
}
