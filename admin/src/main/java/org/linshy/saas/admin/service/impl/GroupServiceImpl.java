package org.linshy.saas.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.linshy.saas.admin.common.biz.user.UserContext;
import org.linshy.saas.admin.dao.entity.GroupDO;
import org.linshy.saas.admin.dao.mapper.GroupMapper;
import org.linshy.saas.admin.dto.resp.ShortLinkGroupRespDTO;
import org.linshy.saas.admin.service.GroupService;
import org.linshy.saas.admin.toolkit.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    /**
     * 新增分组
     * @param groupName 分组名称
     */
    @Override
    public void save(String groupName) {
        String gid;
        do {
            gid = RandomStringGenerator.generateRandom();
        } while (!hasGid(gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupName)
                .username(UserContext.getUsername())
                .build();

        baseMapper.insert(groupDO);
    }

    /**
     * 查询用户分组列表
     * @return
     */
    @Override
    public List<ShortLinkGroupRespDTO> listGroup() {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getUsername, UserContext.getUsername())
                .eq(GroupDO::getDelFlag, 0)
                .orderByDesc(GroupDO::getSortOrder, GroupDO::getUsername);

        List<GroupDO> list = baseMapper.selectList(queryWrapper);

        return BeanUtil.copyToList(list, ShortLinkGroupRespDTO.class);

    }

    /**
     * 检查group id是否可用
     * @param gid
     * @return
     */

    private boolean hasGid(String gid)
    {
        LambdaQueryWrapper<GroupDO> queryWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = baseMapper.selectOne(queryWrapper);

        return groupDO == null;
    }
}