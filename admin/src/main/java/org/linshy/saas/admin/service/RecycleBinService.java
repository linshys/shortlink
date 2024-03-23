package org.linshy.saas.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.linshy.saas.admin.remote.dto.resp.ShortLinkPageRespDTO;


/**
 * 回收站管理服务接口层
 */
public interface RecycleBinService{
    /**
     * 查询回收站分页
     * @param requestParam 请求参数
     * @return 返回参数
     */
    Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam);



}
