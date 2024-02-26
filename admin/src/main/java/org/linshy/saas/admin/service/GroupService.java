package org.linshy.saas.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.admin.dao.entity.GroupDO;
import org.linshy.saas.admin.dto.req.ShortLinkGroupSortReqDTO;
import org.linshy.saas.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

/**
 * 短链接分组接口层
 */
public interface GroupService extends IService<GroupDO> {

    /**
     * 新增短链接分组
     * @param groupName 分组名称
     */
    void save(String groupName);


    /**
     * 查询用户短链接分组集合
     */
    List<ShortLinkGroupRespDTO> listGroup();

    void update(ShortLinkGroupUpdateReqDTO requestParam);

    void deleteGroup(String gid);

    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);

}
