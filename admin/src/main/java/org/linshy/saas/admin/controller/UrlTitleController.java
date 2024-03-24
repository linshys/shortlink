package org.linshy.saas.admin.controller;

import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.remote.ShortLinkActualRemoteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * URL标题接口层
 */
@RestController(value = "urlTitleControllerByAdmin")
@RequiredArgsConstructor
public class UrlTitleController {
    private final ShortLinkActualRemoteService shortLinkActualRemoteService;
    /**
     * 根据url获取网页标题
     * @param url 网页链接
     * @return 网页标题
     */
    @GetMapping("/api/short-link/admin/v1/title")
    public Result<String> getTitleByUrl(@RequestParam("url") String url)
    {
        return shortLinkActualRemoteService.getTitleByUrl(url);
    }


}
