package org.linshy.saas.admin.common.biz.user;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
public class UserTransmitFilter implements Filter {
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = httpServletRequest.getHeader("username");
        String token = httpServletRequest.getHeader("token");
        Object userInfoJsonStr = null;
        if (username!=null && token != null)
            userInfoJsonStr = stringRedisTemplate.opsForHash().get("login_" + username, token);

        if (userInfoJsonStr != null)
        {
            UserInfoDTO userInfoDTO = JSON.parseObject(userInfoJsonStr.toString(), UserInfoDTO.class);
            userInfoDTO.setToken(token);
            UserContext.setUser(userInfoDTO);
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }
}
