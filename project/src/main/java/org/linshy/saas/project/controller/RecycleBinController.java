package org.linshy.saas.project.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.project.common.convention.result.Result;
import org.linshy.saas.project.common.convention.result.Results;
import org.linshy.saas.project.dto.req.RecycleBinRecoverReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkRecycleBinPageReqDTO;
import org.linshy.saas.project.dto.req.RecycleBinSaveReqDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.project.service.RecycleBinService;
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
    private final RecycleBinService recycleBinService;


    /**
     * 将短链接移入回收站
     */
    @PostMapping("/api/short-link/v1/recycle-bin/save")
    public Result<Void> saveRecycleBin(@RequestBody RecycleBinSaveReqDTO recycleBinSaveReqDTO)
    {
        recycleBinService.saveRecycleBin(recycleBinSaveReqDTO);
        return Results.success();
    }

    /**
     * 查询回收站分页
     */
    @GetMapping("/api/short-link/v1/recycle-bin/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkRecycleBinPageReqDTO requestParam)
    {
        return Results.success(recycleBinService.pageShortLink(requestParam));
    }

    /**
     * 恢复回收站短链接
     * @param requestParam 请求参数{gid,fullShortUrl}
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public Result<Void> recoverRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam)
    {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }

    /**
     * 彻底删除回收站短链接
     * @param requestParam 请求参数{gid,fullShortUrl}
     */
    @PostMapping("/api/short-link/v1/recycle-bin/recover")
    public Result<Void> removeRecycleBin(@RequestBody RecycleBinRecoverReqDTO requestParam)
    {
        recycleBinService.recoverRecycleBin(requestParam);
        return Results.success();
    }




}
