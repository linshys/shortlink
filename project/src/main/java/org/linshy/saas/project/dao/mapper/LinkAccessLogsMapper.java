package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.linshy.saas.project.dao.entity.LinkAccessLogsDO;

public interface LinkAccessLogsMapper extends BaseMapper<LinkAccessLogsDO> {
    @Insert("INSERT INTO t_link_access_logs (full_short_url,gid,user,browser,os,ip,create_time,update_time,del_flag)\n" +
            "VALUES(#{linkAccessLogs.fullShortUrl}, #{linkAccessLogs.gid},#{linkAccessLogs.user},#{linkAccessLogs.browser},#{linkAccessLogs.os},#{linkAccessLogs.ip},NOW(),NOW(), 0);")
    void shortLinkAccessLogs(@Param("linkAccessLogs") LinkAccessLogsDO linkAccessLogsDO);
}
