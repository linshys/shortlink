package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.linshy.saas.project.dao.entity.LinkLocaleStatsDO;

public interface LinkLocaleStatsMapper extends BaseMapper<LinkLocaleStatsDO> {
    @Insert("INSERT INTO t_link_locale_stats (full_short_url,gid,date,cnt,province,city,adcode,country,create_time,update_time,del_flag)\n" +
            "VALUES(#{linkLocaleStats.fullShortUrl}, #{linkLocaleStats.gid},#{linkLocaleStats.date},#{linkLocaleStats.cnt},#{linkLocaleStats.province},#{linkLocaleStats.city},#{linkLocaleStats.adcode},#{linkLocaleStats.country},NOW(),NOW(), 0) \n" +
            "ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  cnt = cnt + #{linkLocaleStats.cnt};")
    void shortLinkLocaleStats(@Param("linkLocaleStats") LinkLocaleStatsDO linkLocaleStatsDO);
}
