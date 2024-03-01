package org.linshy.saas.project.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.linshy.saas.project.common.convention.exception.ServiceException;
import org.linshy.saas.project.common.enums.VaildDataTypeEnum;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dao.entity.ShortLinkGotoDO;
import org.linshy.saas.project.dao.mapper.ShortLinkGotoMapper;
import org.linshy.saas.project.dao.mapper.ShortLinkMapper;
import org.linshy.saas.project.dto.req.ShortLInkCreateReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkPageReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkUpdateReqDTO;
import org.linshy.saas.project.dto.resp.ShortLinkCountQueryRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkCreateRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.project.service.ShortLinkService;
import org.linshy.saas.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.linshy.saas.project.common.constant.RedisKeyConstant.GOTO_SHORT_LINK_KEY;
import static org.linshy.saas.project.common.constant.RedisKeyConstant.LOCK_GOTO_SHORT_LINK_KEY;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    private final ShortLinkGotoMapper shortLinkGotoMapper;

    private final StringRedisTemplate stringRedisTemplate;

    private final RedissonClient redissonClient;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLInkCreateReqDTO requestParam) {

        ShortLinkDO shortLinkDO = BeanUtil.toBean(requestParam, ShortLinkDO.class);
        String shortLink = this.generateSuffix(requestParam);
        shortLinkDO.setShortUri(shortLink);
        shortLinkDO.setEnableStatus(0);
        shortLinkDO.setFullShortUrl(requestParam.getDomain()+'/'+ shortLink);

        try {
            baseMapper.insert(shortLinkDO);
        }
        catch (DuplicateKeyException ex)
        {
            shortUriCreateCachePenetrationBloomFilter.add(requestParam.getDomain()+'/'+ shortLink);
            log.warn("短链接: {} 重复入库",requestParam.getDomain()+'/'+ shortLink);
            throw new ServiceException("短链接生成重复");
        }
        shortUriCreateCachePenetrationBloomFilter.add(requestParam.getDomain()+'/'+ shortLink);

        /**
         * 将短链接信息加入跳转表
         */
        ShortLinkGotoDO shortLinkGotoDO = ShortLinkGotoDO.builder()
                .full_short_url(requestParam.getDomain() + '/' + shortLink)
                .gid(requestParam.getGid())
                .build();
        shortLinkGotoMapper.insert(shortLinkGotoDO);


        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .gid(requestParam.getGid())
                .originUrl(requestParam.getOriginUrl())
                .build();
    }

    @Override
    public IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkPageReqDTO requestParam) {
        LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getEnableStatus, 0)
                .eq(ShortLinkDO::getDelFlag, 0)
                .orderByDesc(ShortLinkDO::getCreateTime);
        IPage<ShortLinkDO> resultPage = baseMapper.selectPage(requestParam, wrapper);
        return resultPage.convert(each -> {
                    ShortLinkPageRespDTO result = BeanUtil.toBean(each, ShortLinkPageRespDTO.class);
                    result.setDomain("http://" + result.getDomain());
                    return result;
                });
    }

    @Override
    public List<ShortLinkCountQueryRespDTO> listGroupShortLinkCount(List<String> requestParam) {
        QueryWrapper<ShortLinkDO> queryWrapper = Wrappers.query(new ShortLinkDO())
                .select("gid as gid, count(*) as shortLinkCount")
                .in("gid",requestParam)
                .eq("enable_status", 0)
                .groupBy("gid");
        List<Map<String, Object>> mapList = baseMapper.selectMaps(queryWrapper);
        return BeanUtil.copyToList(mapList,ShortLinkCountQueryRespDTO.class);
    }

    @Override
    public void updateShortLink(ShortLinkUpdateReqDTO requestParam) {
        // TODO 更改gid
        LambdaUpdateWrapper<ShortLinkDO> updateWrapper = Wrappers.lambdaUpdate(ShortLinkDO.class)
                .eq(ShortLinkDO::getGid, requestParam.getGid())
                .eq(ShortLinkDO::getFullShortUrl, requestParam.getFullShortUrl())
                .eq(ShortLinkDO::getDelFlag, 0)
                .eq(ShortLinkDO::getEnableStatus, 0)
                .set(Objects.equals(requestParam.getValidDateType(),
                        VaildDataTypeEnum.PERMANENT.getType()), ShortLinkDO::getValidDate, null);
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .favicon(requestParam.getFavicon())
                .describe(requestParam.getDescribe())
                .enableStatus(requestParam.getEnableStatus())
                .validDate(requestParam.getValidDate())
                .validDateType(requestParam.getValidDateType())
                .build();

        baseMapper.update(shortLinkDO, updateWrapper);
        return;
    }

    @SneakyThrows
    @Override
    public void restoreUrl(String shortUri, ServletRequest request, ServletResponse response) {
        String serverName = request.getServerName();
        String fullShortUrl = serverName + "/" + shortUri;
        // 1. 检查redis缓存中是否存在
        String originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
        if (StrUtil.isNotBlank(originalLink))
        {
            ((HttpServletResponse)response).sendRedirect(originalLink);
            return;
        }

        // 2. 不存在上锁查数据库
        RLock lock = redissonClient.getLock(String.format(LOCK_GOTO_SHORT_LINK_KEY, fullShortUrl));
        lock.lock();
        try {
            // 3. 双检加锁策略，再次检查redis，防止多人拿到锁
            originalLink = stringRedisTemplate.opsForValue().get(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl));
            if (StrUtil.isNotBlank(originalLink))
            {
                ((HttpServletResponse)response).sendRedirect(originalLink);
                return;
            }

            // 4. 最后查数据库

            // 查询goto表找到gid
            LambdaQueryWrapper<ShortLinkGotoDO> queryWrapper = Wrappers.lambdaQuery(ShortLinkGotoDO.class)
                    .eq(ShortLinkGotoDO::getFull_short_url, fullShortUrl);
            ShortLinkGotoDO shortLinkGotoDO = shortLinkGotoMapper.selectOne(queryWrapper);
            if(shortLinkGotoDO==null)
            {
                // 需要更严谨的处理方法
                return;
            }



            LambdaQueryWrapper<ShortLinkDO> wrapper = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getGid, shortLinkGotoDO.getGid())
                    .eq(ShortLinkDO::getEnableStatus, 0)
                    .eq(ShortLinkDO::getDelFlag, 0)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortUrl);

            ShortLinkDO shortLinkDO = baseMapper.selectOne(wrapper);
            if (shortLinkDO!=null) {
                String originUrl = shortLinkDO.getOriginUrl();
                stringRedisTemplate.opsForValue().set(String.format(GOTO_SHORT_LINK_KEY, fullShortUrl), originUrl);
                ((HttpServletResponse)response).sendRedirect(originUrl);
            }

        }
        finally {
            lock.unlock();
        }
    }

    private String generateSuffix(ShortLInkCreateReqDTO requestParam)
    {
        String originUrl = requestParam.getOriginUrl();
        int customGenerateCount = 0;
        String shortUri;
        while(true)
        {
            if (customGenerateCount>10)
            {
                throw new ServiceException("短链接创建次数过多，请稍后重试");
            }
            originUrl += System.currentTimeMillis();
            shortUri = HashUtil.hashToBase62(originUrl);
            if (!shortUriCreateCachePenetrationBloomFilter.contains(requestParam.getDomain()+'/'+ shortUri))
            {
                break;
            }
            ++customGenerateCount;
        }
        return shortUri;
    }
}
