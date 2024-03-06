package org.linshy.saas.project.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linshy.saas.project.dto.req.ShortLinkGroupStatsReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkStatsAccessRecordReqDTO;
import org.linshy.saas.project.dto.req.ShortLinkStatsReqDTO;
import org.linshy.saas.project.dto.resp.ShortLinkStatsAccessRecordRespDTO;
import org.linshy.saas.project.dto.resp.ShortLinkStatsRespDTO;

/**
 * 短链接监控接口层
 */
public interface ShortLinkStatsService {

    /**
     * 获取单个短链接监控数据
     *
     * @param requestParam 获取短链接监控数据入参
     * @return 短链接监控数据
     */
    ShortLinkStatsRespDTO oneShortLinkStats(ShortLinkStatsReqDTO requestParam);

    /**
     * 获取单个短链接监控数据
     */
    IPage<ShortLinkStatsAccessRecordRespDTO> shortLinkStatsAccessRecord(ShortLinkStatsAccessRecordReqDTO requestParam);

    /**
     * 获取分组短链接监控数据
     *
     * @param requestParam 获取分组短链接监控数据入参
     * @return 分组短链接监控数据
     */
    ShortLinkStatsRespDTO groupShortLinkStats(ShortLinkGroupStatsReqDTO requestParam);
}
