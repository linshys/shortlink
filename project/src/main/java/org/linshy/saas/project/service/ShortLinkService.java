package org.linshy.saas.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dto.req.ShortLInkCreateReqDTO;
import org.linshy.saas.project.dto.resp.ShortLInkCreateRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     * @param requestParam 请求参数
     * @return 短链接创建信息
     */
    ShortLInkCreateRespDTO createShortLink(ShortLInkCreateReqDTO requestParam);

}
