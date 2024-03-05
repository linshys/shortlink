package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.linshy.saas.project.dao.entity.LinkBrowserStatsDO;
import org.linshy.saas.project.dto.req.ShortLinkStatsReqDTO;

import java.util.HashMap;
import java.util.List;

public interface LinkBrowserStatsMapper extends BaseMapper<LinkBrowserStatsDO> {
    @Insert("INSERT INTO t_link_browser_stats (full_short_url,gid,date,cnt,browser,create_time,update_time,del_flag)\n" +
            "VALUES(#{linkBrowserStats.fullShortUrl}, #{linkBrowserStats.gid},#{linkBrowserStats.date},#{linkBrowserStats.cnt},#{linkBrowserStats.browser},NOW(),NOW(), 0) \n" +
            "ON DUPLICATE KEY\n" +
            "UPDATE\n" +
            "  cnt = cnt + #{linkBrowserStats.cnt};")
    void shortLinkBrowserStats(@Param("linkBrowserStats") LinkBrowserStatsDO linkBrowserStatsDO);


    /**
     * 根据短链接获取指定日期内浏览器监控数据
     */
    @Select("SELECT " +
            "    browser, " +
            "    SUM(cnt) AS count " +
            "FROM " +
            "    t_link_browser_stats " +
            "WHERE " +
            "    full_short_url = #{param.fullShortUrl} " +
            "    AND gid = #{param.gid} " +
            "    AND date BETWEEN #{param.startDate} and #{param.endDate} " +
            "GROUP BY " +
            "    full_short_url, gid, date, browser;")
    List<HashMap<String, Object>> listBrowserStatsByShortLink(@Param("param") ShortLinkStatsReqDTO requestParam);

}
