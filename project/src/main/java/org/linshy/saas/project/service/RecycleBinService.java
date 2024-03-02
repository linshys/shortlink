package org.linshy.saas.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dto.req.RecycleBinSaveReqDTO;

/**
 * 回收站管理服务接口层
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    /**
     * 新增短链接至回收站
     * @param recycleBinSaveReqDTO 请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO recycleBinSaveReqDTO);
}
