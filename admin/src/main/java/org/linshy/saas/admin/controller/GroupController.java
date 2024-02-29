package org.linshy.saas.admin.controller;

import lombok.RequiredArgsConstructor;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.common.convention.result.Results;
import org.linshy.saas.admin.dto.req.ShortLinkGroupSaveReqDTO;
import org.linshy.saas.admin.dto.req.ShortLinkGroupSortReqDTO;
import org.linshy.saas.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import org.linshy.saas.admin.dto.resp.ShortLinkGroupRespDTO;
import org.linshy.saas.admin.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *短链接分组控制层
 */
@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;


    /**
     * 新增短链接分组
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam)
    {
        groupService.save(requestParam.getName());
        return Results.success();
    }

    /**
     * 查询用户短链接分组
     * @return  该用户所有分组信息
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup()
    {
        return Results.success(groupService.listGroup());
    }


    /**
     * 修改分组名称
     * @param requestParam gid，name
     * @return 空
     */
    @PutMapping("/api/short-link/admin/v1/group")
    public Result<Void> update(@RequestBody ShortLinkGroupUpdateReqDTO requestParam)
    {
        groupService.update(requestParam);
        return Results.success(null);

    }


    /**
     * 删除用户分组
     * @param gid 分组id
     * @return void
     */
    @DeleteMapping("/api/short-link/admin/v1/group")
    public Result<Void> deleteGroup(@RequestParam String gid)
    {
        groupService.deleteGroup(gid);
        return Results.success(null);

    }

    /**
     * 更改分组的排序
     * @param requestParam 每个分组的排序参数
     * @return void
     */
    @PostMapping("/api/short-link/admin/v1/group/sort")
    public Result<Void> sortGroup(@RequestBody List<ShortLinkGroupSortReqDTO> requestParam)
    {
        groupService.sortGroup(requestParam);
        return Results.success(null);

    }


}
