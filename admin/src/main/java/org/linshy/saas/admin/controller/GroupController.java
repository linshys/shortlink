package org.linshy.saas.admin.controller;

import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.common.convention.result.Results;
import org.linshy.saas.admin.dto.req.ShortLinkGroupSaveReqDTO;
import org.linshy.saas.admin.service.GroupService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping("/api/short-link/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam)
    {
        groupService.save(requestParam.getName());
        return Results.success();
    }
}
