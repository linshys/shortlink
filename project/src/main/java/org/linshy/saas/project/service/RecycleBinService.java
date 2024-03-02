package org.linshy.saas.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.linshy.saas.project.dto.req.RecycleBinSaveReqDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;

/**
 * 回收站管理服务接口层
 */
public interface RecycleBinService extends IService<ShortLinkDO> {
    /**
     * 新增短链接至回收站
     * @param recycleBinSaveReqDTO 请求参数
     */
    void saveRecycleBin(RecycleBinSaveReqDTO recycleBinSaveReqDTO);


    /**
     * 查询回收站分页
     * @param requestParam 请求参数
     * @return 返回参数
     */
    IPage<ShortLinkPageRespDTO> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);



}
