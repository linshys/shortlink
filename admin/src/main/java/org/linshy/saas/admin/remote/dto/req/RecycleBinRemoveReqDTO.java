package org.linshy.saas.admin.remote.dto.req;

import lombok.Data;

/**
 * 复原回收站请求参数
 */
@Data
public class RecycleBinRemoveReqDTO {

    /**
     * 分组标识
     */
    private String gid;
    /**
     * 完整短链接
     */
    private String fullShortUrl;
}
