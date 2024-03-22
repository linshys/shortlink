package org.linshy.saas.project.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 跳转域名白名单配置文件
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：link）获取项目资料
 */
@Data
@Component
@ConfigurationProperties(prefix = "short-link.goto-domain.white-list")
public class GotoDomainWhiteListConfiguration {

    /**
     * 是否开启跳转原始链接域名白名单验证
     */
    private Boolean enable;

    /**
     * 跳转原始域名白名单网站名称集合
     */
    private String names;

    /**
     * 可跳转的原始链接域名
     */
    private List<String> details;
}