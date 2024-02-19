package org.linshy.saas.admin.dto.req;

import lombok.Data;

/**
 * 短链接新增分组参数
 */
@Data
public class ShortLinkGroupSaveReqDTO {
    /**
     * 短链接分组 名称
     */
    private String name;
}
