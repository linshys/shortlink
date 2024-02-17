package org.linshy.saas.admin.dto.req;

import lombok.Data;

/**
 * 用户登录请求参数
 */
@Data
public class UserLoginReqDTO {
    private String username;
    private String password;
}
