package org.linshy.saas.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.project.common.convention.result.Result;
import org.linshy.saas.project.common.convention.result.Results;
import org.linshy.saas.project.dto.req.ShortLInkCreateReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkPageReqDTO;
import org.linshy.saas.project.dto.resp.ShortLInkCreateRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.project.service.ShortLinkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 短链接管理控制层
 */
@RestController
@RequiredArgsConstructor
public class ShortLInkController {

    private final ShortLinkService shortLinkService;


    /**
     * 创建短链接
     * @return 短链接信息
     */
    @PostMapping("/api/short-link/v1/create")
    public Result<ShortLInkCreateRespDTO> createShortLink(@RequestBody ShortLInkCreateReqDTO requestParam)
    {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }


    /**
     * 短链接分页查询
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam)
    {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }


}
