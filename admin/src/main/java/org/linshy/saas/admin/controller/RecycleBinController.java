package org.linshy.saas.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.common.convention.result.Results;
import org.linshy.saas.admin.dto.req.RecycleBinSaveReqDTO;
import org.linshy.saas.admin.remote.ShortLinkRemoteService;
import org.linshy.saas.admin.remote.dto.req.RecycleBinRecoverReqDTO;
import org.linshy.saas.admin.remote.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.linshy.saas.admin.remote.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.admin.service.RecycleBinService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回收站管理控制层
 */
@RestController
@RequiredArgsConstructor
public class RecycleBinController {
    ShortLinkRemoteService shortLinkRemoteService = new ShortLinkRemoteService() {
    };
    public final RecycleBinService recycleBinService;

    /**
     * 短链接移入回收站
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO)
    {
        shortLinkRemoteService.saveRecycleBin(recycleBinSaveReqDTO);
        return Results.success();
    }

    /**
     * 回收站分页展示
     */
    @GetMapping("/api/short-link/admin/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam)
    {

        return recycleBinService.pageShortLink(requestParam);
    }

    /**
     * 恢复回收站短链接
     * @param requestParam 请求参数{gid,fullShortUrl}
     */
    @PostMapping("/api/short-link/admin/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam)
    {
        shortLinkRemoteService.recoverRecycleBin(requestParam);
        return Results.success();
    }



}
