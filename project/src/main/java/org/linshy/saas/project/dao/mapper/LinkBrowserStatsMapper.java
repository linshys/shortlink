package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.linshy.saas.project.dao.entity.LinkBrowserStatsDO;

public interface LinkBrowserStatsMapper extends BaseMapper<LinkBrowserStatsDO> {
    @Insert("INSERT INTO t_link_browser_stats (full_short_url,gid,date,cnt,browser,create_time,update_time,del_flag)\n" +
            "VALUES(#{linkBrowserStats.fullShortUrl}, #{linkBrowserStats.gid},#{linkBrowserStats.date},#{linkBrowserStats.cnt},#{linkBrowserStats.browser},NOW(),NOW(), 0) \n" +
            "ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  cnt = cnt + #{linkBrowserStats.cnt};")
    void shortLinkBrowserStats(@Param("linkBrowserStats") LinkBrowserStatsDO linkBrowserStatsDO);
}
