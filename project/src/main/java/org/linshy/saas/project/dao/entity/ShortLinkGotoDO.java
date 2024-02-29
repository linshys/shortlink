package org.linshy.saas.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短链接跳转实体
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_link_goto")
public class ShortLinkGotoDO {
    /**
     * id
     */
    Integer id;
    /**
     * 分组标识
     */
    String gid;
    /**
     * 完整短链接uri
     */
    String full_short_url;
}
