package org.linshy.saas.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.linshy.saas.project.dao.entity.ShortLinkDO;

/**
 * 短链接分页请求参数
 */
@Data
public class ShortLinkPageReqDTO extends Page<ShortLinkDO> {

    /**
     * 分组标识
     */
    String gid;


    /**
     * 排序标识
     */
    private String orderTag;

}
