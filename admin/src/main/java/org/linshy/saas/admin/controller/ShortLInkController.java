package org.linshy.saas.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.remote.ShortLinkRemoteService;
import org.linshy.saas.admin.remote.dto.req.ShortLInkCreateReqDTO;
import org.linshy.saas.admin.remote.dto.req.ShortLinkPageReqDTO;
import org.linshy.saas.admin.remote.dto.resp.ShortLInkCreateRespDTO;
import org.linshy.saas.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接后台管理控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLInkController {
    // TODO 后续用Feign调用重构
     ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
     };

    /**
     * 创建短链接
     * @return 短链接信息
     */
    @PostMapping("/api/short-link/admin/v1/create")
    public Result<ShortLInkCreateRespDTO> createShortLink(@RequestBody ShortLInkCreateReqDTO requestParam)
    {
        return shortLinkRemoteService.createShortLink(requestParam);
    }


    /**
     * 短链接分页查询
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam)
    {
        return shortLinkRemoteService.pageShortLink(requestParam);
    }


}
