package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.linshy.saas.project.dao.entity.LinkOsStatsDO;

public interface LinkOsStatsMapper extends BaseMapper<LinkOsStatsDO> {
    @Insert("INSERT INTO t_link_os_stats (full_short_url,gid,date,cnt,os,create_time,update_time,del_flag)\n" +
            "VALUES(#{linkOsStats.fullShortUrl}, #{linkOsStats.gid},#{linkOsStats.date},#{linkOsStats.cnt},#{linkOsStats.os},NOW(),NOW(), 0) \n" +
            "ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  cnt = cnt + #{linkOsStats.cnt};")
    void shortLinkOsStats(@Param("linkOsStats") LinkOsStatsDO linkOsStatsDO);
}
