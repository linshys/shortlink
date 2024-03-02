package org.linshy.saas.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.biz.user.UserContext;
import org.linshy.saas.admin.common.convention.exception.ClientException;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.dao.entity.GroupDO;
import org.linshy.saas.admin.dao.mapper.GroupMapper;
import org.linshy.saas.admin.remote.ShortLinkRemoteService;
import org.linshy.saas.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.linshy.saas.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.admin.service.RecycleBinService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 回收站管理服务实现层
 */
@Service
@RequiredArgsConstructor
public class RecycleBinServiceImpl implements RecycleBinService {

    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };
    private final GroupMapper groupMapper;


    @Override
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam)
    {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0);
        List<GroupDO> list = groupMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(list))
        {
            throw new ClientException("用户分组信息不存在");
        }
        requestParam.setGidList(list.stream().map(GroupDO::getGid).toList());
        return shortLinkRemoteService.pageRecycleBinShortLink(requestParam);
    }

}
