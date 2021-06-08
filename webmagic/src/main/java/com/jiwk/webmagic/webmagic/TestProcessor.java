
package com.jiwk.webmagic.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.processor.PageProcessor;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class TestProcessor implements PageProcessor {

    /**
     * 部分一：抓去网站的相关配置，包括编码、抓去间隔、重试次数
     */
    private Site site = Site.me().setCycleRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

    @Override
    public void process(Page page) {
        page.putField("readme", page.getHtml());
    }

    @Override
    public Site getSite() {
        return site;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
        Spider.create(new TestProcessor())
                .addUrl("http://www.rrys2020.com/html/top/week.html")
                //覆盖默认的实现 HttpClientDownloader，因为使用webmagic爬取 https 网站时，会
                .setDownloader(new HttpClientDownloader())
                .thread(5)
                .run();
    }
}