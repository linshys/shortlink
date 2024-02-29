package org.linshy.saas.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.linshy.saas.admin.common.biz.user.UserContext;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.dao.entity.GroupDO;
import org.linshy.saas.admin.dao.mapper.GroupMapper;
import org.linshy.saas.admin.dto.req.ShortLinkGroupSortReqDTO;
import org.linshy.saas.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.ShortLinkGroupRespDTO;
import org.linshy.saas.admin.remote.ShortLinkRemoteService;
import org.linshy.saas.admin.remote.dto.resp.ShortLinkCountQueryRespDTO;
import org.linshy.saas.admin.service.GroupService;
import org.linshy.saas.admin.toolkit.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短链接分组接口实现层
 */
@Slf4j
@Service
public class GroupServiceImpl extends ServiceImpl<GroupMapper, GroupDO> implements GroupService {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };
    /**
     * 新增分组
     * @param groupName 分组名称
     */
    @Override
    public void save(String groupName) {
        save(UserContext.getUsername(), groupName);
    }

    public void save(String username, String groupName) {
        String gid;
        do {
            gid = RandomStringGenerator.generateRandom();
        } while (!hasGid(gid));

        GroupDO groupDO = GroupDO.builder()
                .gid(gid)
                .sortOrder(0)
                .name(groupName)
                .username(username)
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

        // 判空
        if (list.isEmpty())
        {
            return null;
        }

        List<String> gidList = list.stream().map(GroupDO::getGid).toList();
        Result<List<ShortLinkCountQueryRespDTO>> listResult =
                shortLinkRemoteService.listGroupShortLinkCount(gidList);
        List<ShortLinkGroupRespDTO> shortLinkGroupRespDTOList = BeanUtil.copyToList(list, ShortLinkGroupRespDTO.class);

        // 将远程服务获取的结果转换为Map，以提高查找效率
        Map<String, Integer> gidToCountMap = listResult.getData().stream()
                .collect(Collectors.toMap(ShortLinkCountQueryRespDTO::getGid, ShortLinkCountQueryRespDTO::getShortLinkCount));

        // 直接使用Map更新shortLinkCount
        shortLinkGroupRespDTOList.forEach(each -> {
            Integer count = gidToCountMap.get(each.getGid());
            if (count != null) {
                each.setShortLinkCount(count);
            }
        });
        return shortLinkGroupRespDTOList;

    }

    /**
     * 更新分组名称
     * @param requestParam 参数
     */

    @Override
    public void update(ShortLinkGroupUpdateReqDTO requestParam) {
        LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, requestParam.getGid())
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = new GroupDO();
        groupDO.setName(requestParam.getName());
        baseMapper.update(groupDO,updateWrapper);
    }

    @Override
    public void deleteGroup(String gid) {
        LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGid, gid)
                .eq(GroupDO::getDelFlag, 0)
                .eq(GroupDO::getUsername, UserContext.getUsername());
        GroupDO groupDO = new GroupDO();
        groupDO.setDelFlag(1);
        baseMapper.update(groupDO,updateWrapper);
    }

    @Override
    public void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam) {
        requestParam.forEach(each -> {
            GroupDO groupDO = new GroupDO();
            groupDO.setSortOrder(each.getSortOrder());

            LambdaQueryWrapper<GroupDO> updateWrapper = Wrappers.lambdaQuery(GroupDO.class)
                    .eq(GroupDO::getGid, each.getGid())
                    .eq(GroupDO::getDelFlag, 0)
                    .eq(GroupDO::getUsername, UserContext.getUsername());

            baseMapper.update(groupDO, updateWrapper);
        });
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
                .eq(GroupDO::getDelFlag, 0);
        GroupDO groupDO = baseMapper.selectOne(queryWrapper);

        return groupDO == null;
    }
}
