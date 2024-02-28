package org.linshy.saas.project.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.linshy.saas.project.common.convention.exception.ServiceException;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dao.mapper.ShortLinkMapper;
import org.linshy.saas.project.dto.req.ShortLInkCreateReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkPageReqDTO;
import org.linshy.saas.project.dto.resp.ShortLInkCreateRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.project.service.ShortLinkService;
import org.linshy.saas.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;
    @Override
    public ShortLInkCreateRespDTO createShortLink(ShortLInkCreateReqDTO requestParam) {

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

        return ShortLInkCreateRespDTO.builder()
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
        return resultPage.convert(each -> BeanUtil.toBean(each, ShortLinkPageRespDTO.class));
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
