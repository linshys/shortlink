package org.linshy.saas.project.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.linshy.saas.project.dao.entity.LinkStatsTodayDO;
import org.linshy.saas.project.dao.mapper.LinkStatsTodayMapper;
import org.linshy.saas.project.service.LinkStatsTodayService;
import org.springframework.stereotype.Service;

/**
 * 短链接今日统计接口实现层
 */
@Service
public class LinkStatsTodayServiceImpl extends ServiceImpl<LinkStatsTodayMapper, LinkStatsTodayDO> implements LinkStatsTodayService {
}
