package org.linshy.saas.project.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.linshy.saas.project.dao.entity.ShortLinkDO;
import org.linshy.saas.project.dto.req.ShortLinkPageReqDTO;

public interface ShortLinkMapper extends BaseMapper<ShortLinkDO> {

    /**
     * 短链接删除
     */
    @Update("update t_link set del_time = #{delTime}, del_flag = 1 where gid = #{gid} and full_short_url = #{fullShortUrl} and del_flag = 0 and enable_status = 0 and del_time = 0")
    void delete(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("delTime") Long delTime
    );

    /**
     * 短链接访问统计自增
     */
    @Update("update t_link set total_pv = total_pv + #{totalPv}, total_uv = total_uv + #{totalUv}, total_uip = total_uip + #{totalUip} where gid = #{gid} and full_short_url = #{fullShortUrl}")
    void incrementStats(
            @Param("gid") String gid,
            @Param("fullShortUrl") String fullShortUrl,
            @Param("totalPv") Integer totalPv,
            @Param("totalUv") Integer totalUv,
            @Param("totalUip") Integer totalUip
    );

    /**
     * 分页统计短链接
     */
    IPage<ShortLinkDO> pageLink(ShortLinkPageReqDTO requestParam);
}
