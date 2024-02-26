package org.linshy.saas.admin.dto.req;

import lombok.Data;

/**
 * 短链接新增分组参数
 */
@Data
public class ShortLinkGroupUpdateReqDTO {

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 短链接分组更改后名称
     */
    private String name;
}
