package org.linshy.saas.project.dto.req;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.Date;

@Data
public class ShortLinkUpdateReqDTO {

    /**
     * 完整短链接
     */
    private String fullShortUrl;
    /**
     * 分组标识
     */
    private String gid;

    /**
     * 原始分组标识
     */
    private String originGid;
    /**
     * 网页icon地址
     */
    private String favicon;

    /**
     * 启用标识 0：未启用 1：已启用
     */
    private Integer enableStatus;


    /**
     * 有效期类型 0：永久有效 1：用户自定义
     */
    private Integer validDateType;

    /**
     * 有效期
     */
    private Date validDate;

    /**
     * 描述
     */
    @TableField("`describe`")
    private String describe;

}
