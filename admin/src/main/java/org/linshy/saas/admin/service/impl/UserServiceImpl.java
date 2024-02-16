package org.linshy.saas.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.exception.ClientException;
import org.linshy.saas.admin.common.enums.UserErrorCodeEnum;
import org.linshy.saas.admin.dao.entity.UserDO;
import org.linshy.saas.admin.dao.mapper.UserMapper;
import org.linshy.saas.admin.dto.req.UserRegisterReqDTO;
import org.linshy.saas.admin.dto.resp.UserRespDTO;
import org.linshy.saas.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import static org.linshy.saas.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static org.linshy.saas.admin.common.enums.UserErrorCodeEnum.USER_NAME_EXIST;
import static org.linshy.saas.admin.common.enums.UserErrorCodeEnum.USER_SAVE_ERROR;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        // 抛异常
        if(userDO==null)
            throw new ClientException(UserErrorCodeEnum.USER_NULL);
        UserRespDTO result = new UserRespDTO();
        if(userDO != null){
            BeanUtils.copyProperties(userDO, result);       // 此方法需要判空才可以，否则会报错
            return result;
        } else {
            return null;
        }

    }

    @Override
    public Boolean hasUsername(String username) {
        return !userRegisterCachePenetrationBloomFilter.contains(username);
    }

    @Override
    public void register(UserRegisterReqDTO requestParam) {
        if(!hasUsername(requestParam.getUsername()))
        {
            throw new ClientException(USER_NAME_EXIST);
        }
        // 分布式锁防止恶意请求
        RLock lock = redissonClient.getLock(LOCK_USER_REGISTER_KEY + requestParam.getUsername());
        try {
            if (lock.tryLock()) {
                //持久层
                int insert = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                if(insert<1)
                    throw new ClientException(USER_SAVE_ERROR);
                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }
        finally {
            lock.unlock();
        }





    }
}
