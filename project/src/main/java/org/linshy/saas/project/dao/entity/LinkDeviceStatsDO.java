package org.linshy.saas.project.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.linshy.saas.project.common.database.BaseDO;

import java.util.Date;

/**
 * 短链接用户操作系统对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("t_link_device_stats")
public class LinkDeviceStatsDO extends BaseDO {

    /**
     * id
     */
    private Long id;

    /**
     * 完整短链接
     */
    private String fullShortUrl;

    /**
     * 分组标识
     */
    private String gid;

    /**
     * 日期
     */
    private Date date;

    /**
     * 访问量
     */
    private Integer cnt;

    /**
     * 用户设备
     */
    private String device;


}
