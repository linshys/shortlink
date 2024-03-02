package org.linshy.saas.project.dto.req;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.linshy.saas.project.dao.entity.ShortLinkDO;

import java.util.List;

/**
 * 回收站分页返回参数
 */
@Data
public class ShortLinkRecycleBinPageReqDTO extends Page<ShortLinkDO> {

    /**
     * 分组标识集合
     */
    List<String> gidList;

}
