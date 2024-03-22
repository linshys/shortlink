package org.linshy.saas.project.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import lombok.RequiredArgsConstructor;
import org.linshy.saas.project.common.convention.result.Result;
import org.linshy.saas.project.common.convention.result.Results;
import org.linshy.saas.project.dto.req.ShortLinkBatchCreateReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkCreateReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkPageReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkUpdateReqDTO;
import org.linshy.saas.project.dto.resp.ShortLinkBatchCreateRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkCountQueryRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkCreateRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkPageRespDTO;
import org.linshy.saas.project.handler.CustomBlockHandler;
import org.linshy.saas.project.service.ShortLinkService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @SentinelResource(
            value = "create_short-link",
            blockHandler = "createShortLinkBlockHandlerMethod",
            blockHandlerClass = CustomBlockHandler.class
    )
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam)
    {
        return Results.success(shortLinkService.createShortLink(requestParam));
    }

    /**
     * 批量创建短链接
     */
    @PostMapping("/api/short-link/v1/create/batch")
    public Result<ShortLinkBatchCreateRespDTO> batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam) {
        return Results.success(shortLinkService.batchCreateShortLink(requestParam));
    }


    /**
     * 短链接重定向
     * @param shortUri 短链接
     * @param request http请求
     * @param response http响应
     */
    @GetMapping("/{short_uri}")
    public void restoreUrl(@PathVariable("short_uri") String shortUri, ServletRequest request, ServletResponse response)
    {
        shortLinkService.restoreUrl(shortUri, request, response);
        return;
    }


    /**
     * 短链接分页查询
     */
    @GetMapping("/api/short-link/v1/page")
    public Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam)
    {
        return Results.success(shortLinkService.pageShortLink(requestParam));
    }

    /**
     * 查询分组下短链接数量
     * @param requestParam 短链接数量请求参数
     * @return 短链接数量响应参数
     */
    @GetMapping("/api/short-link/v1/count")
    public Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(@RequestParam("requestParam") List<String> requestParam)
    {
        return Results.success(shortLinkService.listGroupShortLinkCount(requestParam));
    }

    /**
     * 修改短链接信息
     */
    @PostMapping("/api/short-link/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam)
    {
        shortLinkService.updateShortLink(requestParam);
        return Results.success(null);

    }

}
