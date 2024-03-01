package org.linshy.saas.project.service;

/**
 * URL标题接口层
 */
public interface UrlTitleService{

    /**
     * 根据url获取网页标题
     * @param url 网页链接
     * @return 网页标题
     */
    String getTitleByUrl(String url);
}
