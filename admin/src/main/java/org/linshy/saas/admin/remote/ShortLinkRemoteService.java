package org.linshy.saas.admin.remote;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.linshy.saas.admin.common.convention.result.Result;
import org.linshy.saas.admin.dto.req.RecycleBinSaveReqDTO;
import org.linshy.saas.admin.remote.dto.req.*;
import org.linshy.saas.admin.remote.dto.resp.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 短链接中台远程调用服务
 */
public interface ShortLinkRemoteService {

    /**
     * 创建短链接
     * @return 短链接信息
     */
    default Result<ShortLInkCreateRespDTO> createShortLink(@RequestBody ShortLInkCreateReqDTO requestParam)
    {
        String resultBodyStr = HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/create",JSON.toJSONString(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }


    /**
     * 短链接分页查询
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam)
    {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gid", requestParam.getGid());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/page",requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }

    /**
     * 分组短链接数量查询
     */
    default Result<List<ShortLinkCountQueryRespDTO>> listGroupShortLinkCount(List<String> requestParam)
    {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("requestParam", requestParam);
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/count",requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }

    /**
     * 修改短链接信息
     * @param requestParam 需要修改的短链接信息
     */
    default void updateShortLink(ShortLinkUpdateReqDTO requestParam)
    {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/update",JSON.toJSONString(requestParam));
        return;
    }

    /**
     * 根据url获取网页标题
     * @param url 网页链接
     * @return 网页标题
     */
    default Result<String> getTitleByUrl(String url)
    {
        String resultStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/title?url="+url);
        return JSON.parseObject(resultStr, new TypeReference<>() {
        });
    }

    /**
     * 短链接移入回收站
     * @param recycleBinSaveReqDTO
     */
    default void saveRecycleBin(RecycleBinSaveReqDTO recycleBinSaveReqDTO)
    {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/save",JSON.toJSONString(recycleBinSaveReqDTO));
        return;
    };

    /**
     * 回收站分页查询
     */
    default Result<IPage<ShortLinkPageRespDTO>> pageRecycleBinShortLink(ShortLinkRecycleBinPageReqDTO requestParam)
    {
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("gidList", requestParam.getGidList());
        requestMap.put("current", requestParam.getCurrent());
        requestMap.put("size", requestParam.getSize());
        String resultPageStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/page",requestMap);
        return JSON.parseObject(resultPageStr, new TypeReference<>() {
        });
    }

    /**
     * 移出回收站
     */
    default void recoverRecycleBin(RecycleBinRecoverReqDTO requestParam)
    {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/recover",JSON.toJSONString(requestParam));
        return;
    }

    /**
     * 彻底删除短链接
     */
    default void removeRecycleBin(RecycleBinRemoveReqDTO requestParam)
    {
        HttpUtil.post("http://127.0.0.1:8001/api/short-link/v1/recycle-bin/remove",JSON.toJSONString(requestParam));
        return;
    }

    /**
     * 访问单个短链接指定时间内监控数据
     *
     * @param requestParam 访问短链接监控请求参数
     * @return 短链接监控信息
     */
    default Result<ShortLinkStatsRespDTO> oneShortLinkStats(ShortLinkStatsReqDTO requestParam) {
        String resultBodyStr = HttpUtil.get("http://127.0.0.1:8001/api/short-link/v1/stats", BeanUtil.beanToMap(requestParam));
        return JSON.parseObject(resultBodyStr, new TypeReference<>() {
        });
    }
}
