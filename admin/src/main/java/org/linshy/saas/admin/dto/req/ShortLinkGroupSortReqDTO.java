package org.linshy.saas.admin.dto.req;

import lombok.Data;

/**
 * 短链接新增分组参数
 */
@Data
public class ShortLinkGroupSortReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 排序序号
     */
    private Integer sortOrder;
}
