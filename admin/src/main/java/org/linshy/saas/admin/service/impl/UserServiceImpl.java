package org.linshy.saas.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.exception.ClientException;
import org.linshy.saas.admin.common.convention.exception.ServiceException;
import org.linshy.saas.admin.common.enums.UserErrorCodeEnum;
import org.linshy.saas.admin.dao.entity.UserDO;
import org.linshy.saas.admin.dao.mapper.UserMapper;
import org.linshy.saas.admin.dto.req.UserLoginReqDTO;
import org.linshy.saas.admin.dto.req.UserRegisterReqDTO;
import org.linshy.saas.admin.dto.req.UserUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.UserLoginRespDTO;
import org.linshy.saas.admin.dto.resp.UserRespDTO;
import org.linshy.saas.admin.service.GroupService;
import org.linshy.saas.admin.service.UserService;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.linshy.saas.admin.common.constant.RedisCacheConstant.LOCK_USER_REGISTER_KEY;
import static org.linshy.saas.admin.common.constant.RedisCacheConstant.USER_LOGIN_KEY;
import static org.linshy.saas.admin.common.enums.UserErrorCodeEnum.*;

/**
 * 用户接口实现层
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

    private final RBloomFilter<String> userRegisterCachePenetrationBloomFilter;
    private final RedissonClient redissonClient;
    private final StringRedisTemplate stringRedisTemplate;
    private final GroupService groupService;

    @Override
    public UserRespDTO getUserByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = Wrappers.lambdaQuery(UserDO.class).eq(UserDO::getUsername, username);
        UserDO userDO = baseMapper.selectOne(queryWrapper);
        // 抛异常
        if(userDO==null)
            throw new ServiceException(UserErrorCodeEnum.USER_NULL);
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
                try {
                    int insert = baseMapper.insert(BeanUtil.toBean(requestParam, UserDO.class));
                    if(insert<1)
                        throw new ClientException(USER_SAVE_ERROR);
                }
                catch (DuplicateKeyException e)
                {
                    throw new ClientException(USER_EXIST);
                }

                userRegisterCachePenetrationBloomFilter.add(requestParam.getUsername());
                // 用户注册自动创建默认分组
                groupService.save(requestParam.getUsername(),"默认分组");
                return;
            }
            throw new ClientException(USER_NAME_EXIST);
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * 更新用户信息
     */
    @Override
    public void update(UserUpdateReqDTO requestParam) {

        // TODO 验证当前用户名是否为登录用户
        LambdaUpdateWrapper<UserDO> wrapper = Wrappers.lambdaUpdate(UserDO.class).eq(UserDO::getUsername, requestParam.getUsername());
        baseMapper.update(BeanUtil.toBean(requestParam, UserDO.class), wrapper);

    }

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO requestParam) {
        LambdaQueryWrapper<UserDO> wrapper = Wrappers.lambdaQuery(UserDO.class)
                .eq(UserDO::getUsername, requestParam.getUsername())
                .eq(UserDO::getPassword, requestParam.getPassword())
                .eq(UserDO::getDelFlag, 0);
        UserDO userDO = baseMapper.selectOne(wrapper);
        if (userDO==null)
        {
            throw new ClientException("登录失败，检查用户名和密码是否有误");
        }
        /**
         * 判断用户是否已经登录
         */
        Map<Object ,Object> hasLoginMap = stringRedisTemplate.opsForHash().entries(USER_LOGIN_KEY + requestParam.getUsername());
        if (CollUtil.isNotEmpty(hasLoginMap)) {
            String token = hasLoginMap.keySet().stream()
                    .findFirst()
                    .map(Object::toString)
                    .orElseThrow(() -> new ClientException("用户登录错误"));
            return new UserLoginRespDTO(token);
        }

        String uuid = UUID.randomUUID().toString();

        stringRedisTemplate.opsForHash().put(USER_LOGIN_KEY+requestParam.getUsername(),uuid,JSON.toJSONString(userDO));
        // TODO 开发完成后，更改token失效时间为30分钟
        stringRedisTemplate.expire(USER_LOGIN_KEY+requestParam.getUsername(),30L,TimeUnit.DAYS);
        return  new UserLoginRespDTO(uuid);


    }

    @Override
    public Boolean checkLogin(String username, String token) {

        return stringRedisTemplate.opsForHash().hasKey(USER_LOGIN_KEY+username,token);
    }

    @Override
    public void logout(String username, String token) {
        if(checkLogin(username,token))
        {
            stringRedisTemplate.delete(USER_LOGIN_KEY+username);
            return;
        }

        throw new ClientException("用户未登录或者token不存在");
    }
}
