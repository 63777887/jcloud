
package com.jiwk.webmagic.webmagic;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ChromeDriveProcessor implements PageProcessor {

  /**
   * 部分一：抓去网站的相关配置，包括编码、抓去间隔、重试次数
   */
  private Site site = Site.me().setCycleRetryTimes(3).setSleepTime(1000).setTimeOut(10000);

  @Override
  public void process(Page page) {
    Selectable xpath = page.getHtml()
        .xpath("/html/body/div[2]/div/div/div[3]/ul/li/div[1]/div[2]/a/strong/text()");
    Selectable xpath1 = page.getHtml().$(".xy-list").nodes().get(1)
        .xpath("//div[@class='a0']//strong/text()");
    StringBuilder stringBuilders = new StringBuilder("\n");
    xpath1.all().forEach(t -> {
      stringBuilders.append(t).append("\n");
    });
    page.putField("readme", stringBuilders);
  }

  @Override
  public Site getSite() {
    return site;
  }

  public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException {
    Spider.create(new ChromeDriveProcessor())
        .addUrl("http://www.rrys2020.com/html/top/week.html")
        //覆盖默认的实现 HttpClientDownloader，因为使用webmagic爬取 https 网站时，会
        .setDownloader(new SeleniumDownloader("/Users/jwk/project/chromedriver"))
        .thread(5)
        .run();
  }
}