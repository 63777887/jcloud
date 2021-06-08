package com.jwk.down.web.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @program: test
 * @author: jwk
 * @create: {MONTH}-{HOUR}:2020/9/17--10:05 下午
 **/
@Controller
@RequestMapping("/down")
public class TestController {
    @GetMapping("/info")
    public String show() {
        return "downloadSome";
    }

    @GetMapping("/saveCookie")
    public void saveCookie(@RequestParam("nameParams") String nameParams, @RequestParam("nameEncSecKey") String nameEncSecKey) throws UnsupportedEncodingException {
        HashMap<String, Object> nameParamMap = new HashMap<>();
        nameParamMap.put("params", URLDecoder.decode(nameParams, "UTF-8"));
        nameParamMap.put("encSecKey", URLDecoder.decode(nameEncSecKey, "UTF-8"));
        String nameBody = HttpRequest
                .post("https://music.163.com/weapi/v3/song/detail?csrf_token=963a4ffbfce39942a40ca43e7620cee5")
                .form(nameParamMap)
                .header("origin", "https://music.163.com")
                .header("referer", "https://music.163.com/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .header("cookie", "mail_psc_fingerprint=37027d82b1028cb6d18dd1cf137db210; _ntes_nnid=4d4430fd1101fddac3307902680b9102,1598073516332; _ntes_nuid=4d4430fd1101fddac3307902680b9102; WM_TID=6IzxhmCnHpRBQBQVFBZvYVYwx4zbZRPK; _iuqxldmzr_=32; ntes_kaola_ad=1; JSESSIONID-WYYY=7BV5wkWTPB5YYdYeANa3%2BrhHe9FblAOiuUNPU7m%2BPViyiSnx90c1s%2BEoFvO6igddlHQ0qhAT9bt4z9ZWbvk0lCPJUVIF5tyZjerXy67DGY0bzdF4aAYHmf%5CegEG9TuXtCBepIXszc%5CUYn4dpyN%2F0CHEzd0bbdO%2FZEiTJecYIgtppfsfh%3A1601056279363; NMTID=00O5nhqaqyGQl7dt06vhiAsJGJzsIYAAAF0xkiUuQ; WM_NI=2snFWKrp9755s5%2B%2BWXALNnqKD4O0G2DMacFdBYmstxW0%2BrhVDfJflCTjlpaqX8jA%2FX%2Fh7WM%2B%2Fb53UQdpQFugMeo%2FMgTd33VQm6UqGxcoKZuESm2SyRcztQ6b2yBXM4BCMTY%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92f16eb2b196d3b25da8b88fa6c85e869a8eabf143a29bfb85cc5d85919ed5e62af0fea7c3b92af2ada2a7d641b2b0f7d8b83fb1ebe5ccd6548394828ff44188ec81d1c63a8893af84c5708287a394f77495f0e593eb5e88ba8687ef4998b6a491e47b9ce8a6a2ae54edb8f9b8d021edbefbd4d85ba8b09fa7f86d8fbba1d0d06d98bcfdd4d55bafea9b92c674a1b185bbf84f91b3feb4fc5aa1a6bcb2b56af2a78eb9d040829d83b5dc37e2a3; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb4803d347d587a4442f398165709d42a5133a649814e309366; __remember_me=true; __csrf=963a4ffbfce39942a40ca43e7620cee5")
                .execute().body();
        JSONObject jsonObject = new JSONObject(nameBody);
        String name = jsonObject.getJSONArray("songs").getJSONObject(0).get("name", String.class);
    }



    @GetMapping("/netease")
    public void downloadWY(@RequestParam("params") String params, @RequestParam("encSecKey") String encSecKey,
                           @RequestParam("nameParams") String nameParams, @RequestParam("nameEncSecKey") String nameEncSecKey,
                           HttpServletResponse response) throws IOException {
        HashMap<String, Object> nameParamMap = new HashMap<>();
        nameParamMap.put("params", URLDecoder.decode(nameParams, "UTF-8"));
        nameParamMap.put("encSecKey", URLDecoder.decode(nameEncSecKey, "UTF-8"));
        String nameBody = HttpRequest
                .post("https://music.163.com/weapi/v3/song/detail?csrf_token=963a4ffbfce39942a40ca43e7620cee5")
                .form(nameParamMap)
                .header("origin", "https://music.163.com")
                .header("referer", "https://music.163.com/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .header("cookie", "mail_psc_fingerprint=37027d82b1028cb6d18dd1cf137db210; _ntes_nnid=4d4430fd1101fddac3307902680b9102,1598073516332; _ntes_nuid=4d4430fd1101fddac3307902680b9102; WM_TID=6IzxhmCnHpRBQBQVFBZvYVYwx4zbZRPK; _iuqxldmzr_=32; ntes_kaola_ad=1; JSESSIONID-WYYY=7BV5wkWTPB5YYdYeANa3%2BrhHe9FblAOiuUNPU7m%2BPViyiSnx90c1s%2BEoFvO6igddlHQ0qhAT9bt4z9ZWbvk0lCPJUVIF5tyZjerXy67DGY0bzdF4aAYHmf%5CegEG9TuXtCBepIXszc%5CUYn4dpyN%2F0CHEzd0bbdO%2FZEiTJecYIgtppfsfh%3A1601056279363; NMTID=00O5nhqaqyGQl7dt06vhiAsJGJzsIYAAAF0xkiUuQ; WM_NI=2snFWKrp9755s5%2B%2BWXALNnqKD4O0G2DMacFdBYmstxW0%2BrhVDfJflCTjlpaqX8jA%2FX%2Fh7WM%2B%2Fb53UQdpQFugMeo%2FMgTd33VQm6UqGxcoKZuESm2SyRcztQ6b2yBXM4BCMTY%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92f16eb2b196d3b25da8b88fa6c85e869a8eabf143a29bfb85cc5d85919ed5e62af0fea7c3b92af2ada2a7d641b2b0f7d8b83fb1ebe5ccd6548394828ff44188ec81d1c63a8893af84c5708287a394f77495f0e593eb5e88ba8687ef4998b6a491e47b9ce8a6a2ae54edb8f9b8d021edbefbd4d85ba8b09fa7f86d8fbba1d0d06d98bcfdd4d55bafea9b92c674a1b185bbf84f91b3feb4fc5aa1a6bcb2b56af2a78eb9d040829d83b5dc37e2a3; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb4803d347d587a4442f398165709d42a5133a649814e309366; __remember_me=true; __csrf=963a4ffbfce39942a40ca43e7620cee5")
                .execute().body();
        JSONObject jsonObject = new JSONObject(nameBody);
        String name = jsonObject.getJSONArray("songs").getJSONObject(0).get("name", String.class);
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("params", URLDecoder.decode(params, "UTF-8"));
        paramMap.put("encSecKey", URLDecoder.decode(encSecKey, "UTF-8"));

        String body = HttpRequest
                .post("https://music.163.com/weapi/song/enhance/player/url/v1?csrf_token=963a4ffbfce39942a40ca43e7620cee5")
                .form(paramMap)
                .header("origin", "https://music.163.com")
                .header("referer", "https://music.163.com/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
//                .header("cookie", "_ntes_nnid=78ac28f12f7f9108f8d5f4843b4bf89a,1598418255454; _ntes_nuid=78ac28f12f7f9108f8d5f4843b4bf89a; UM_distinctid=174292703154d2-09110c5ef1e83a-5a472316-144000-17429270316507; vinfo_n_f_l_n3=756bac6c8e7cac54.1.0.1598418255463.0.1598418460821; _iuqxldmzr_=32; WM_TID=uzVMttSp%2FmpERRQBFEc6ZEiTca6%2BU9N1; ntes_kaola_ad=1; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb497552387d17f179ae59c7b1d6dc2388733a649814e309366; __remember_me=true; __csrf=02105a964e2faaa37e0362632fcdc30c; JSESSIONID-WYYY=NaKzdVoepnTOw1VoGxnGG%2F5YYa9QXkYcOsKYKuxddV%5C6CVfop2WwAxDCWGA5WatKZv8VXgbeEg4MxRqEOSmRwnm7UCK5nEehOG2ITU13E6necFV8Us%5CdfBq1gYCXDgICtjPKKhnShshGgEUEGXP6lquWw%5Cm%2FAzKy7SWxw2hc0m6Q0dIW%3A1600742081313; WM_NI=Fe2MrdSiX8Hc6E%2F32GCypNeTLssAwnaxtc90LDfAhcbNq2AgRKCM0lRhyZvxb7cw%2FhBQCvsTrbWL5Hhggv94AF%2BZa2qM3BYDCdwbs6d84fsVjJYrpIQH81VCFLChWf4SOGU%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee90c868f689abccea66b19e8ea6d14e968a9fbaf852a8ac8498e17dba9ae5b9f22af0fea7c3b92a86bbfeaef17091b798d9cb69f69be1d4b14d81a7c0afe86090adbda2f666a3a8bc84bc3c909caaa2eb69a292bfd3c46d92958daeb36892b8b7bbf574a2e9ad98ae64828f8d97f260b2be99adb844acb9a997bc40839ae5bbe43ab393fda3dc5081bcb983f23ea8e8fc87f25eaa969793f3439398f8d2d54986948d89d66ef88d81a8d837e2a3; playerid=15957215")
                .header("cookie", "mail_psc_fingerprint=37027d82b1028cb6d18dd1cf137db210; _ntes_nnid=4d4430fd1101fddac3307902680b9102,1598073516332; _ntes_nuid=4d4430fd1101fddac3307902680b9102; WM_TID=6IzxhmCnHpRBQBQVFBZvYVYwx4zbZRPK; _iuqxldmzr_=32; ntes_kaola_ad=1; JSESSIONID-WYYY=7BV5wkWTPB5YYdYeANa3%2BrhHe9FblAOiuUNPU7m%2BPViyiSnx90c1s%2BEoFvO6igddlHQ0qhAT9bt4z9ZWbvk0lCPJUVIF5tyZjerXy67DGY0bzdF4aAYHmf%5CegEG9TuXtCBepIXszc%5CUYn4dpyN%2F0CHEzd0bbdO%2FZEiTJecYIgtppfsfh%3A1601056279363; NMTID=00O5nhqaqyGQl7dt06vhiAsJGJzsIYAAAF0xkiUuQ; WM_NI=2snFWKrp9755s5%2B%2BWXALNnqKD4O0G2DMacFdBYmstxW0%2BrhVDfJflCTjlpaqX8jA%2FX%2Fh7WM%2B%2Fb53UQdpQFugMeo%2FMgTd33VQm6UqGxcoKZuESm2SyRcztQ6b2yBXM4BCMTY%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92f16eb2b196d3b25da8b88fa6c85e869a8eabf143a29bfb85cc5d85919ed5e62af0fea7c3b92af2ada2a7d641b2b0f7d8b83fb1ebe5ccd6548394828ff44188ec81d1c63a8893af84c5708287a394f77495f0e593eb5e88ba8687ef4998b6a491e47b9ce8a6a2ae54edb8f9b8d021edbefbd4d85ba8b09fa7f86d8fbba1d0d06d98bcfdd4d55bafea9b92c674a1b185bbf84f91b3feb4fc5aa1a6bcb2b56af2a78eb9d040829d83b5dc37e2a3; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb4803d347d587a4442f398165709d42a5133a649814e309366; __remember_me=true; __csrf=963a4ffbfce39942a40ca43e7620cee5")
                .execute().body();
        JSONObject json = new JSONObject(body);
        String resUrl = json.getJSONArray("data").getJSONObject(0).get("url").toString();
        byte[] data = HttpRequest
                .get(resUrl)
                .header("origin", "https://music.163.com")
                .header("referer", "https://music.163.com/")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .header("cookie", "mail_psc_fingerprint=37027d82b1028cb6d18dd1cf137db210; _ntes_nnid=4d4430fd1101fddac3307902680b9102,1598073516332; _ntes_nuid=4d4430fd1101fddac3307902680b9102; WM_TID=6IzxhmCnHpRBQBQVFBZvYVYwx4zbZRPK; _iuqxldmzr_=32; ntes_kaola_ad=1; JSESSIONID-WYYY=7BV5wkWTPB5YYdYeANa3%2BrhHe9FblAOiuUNPU7m%2BPViyiSnx90c1s%2BEoFvO6igddlHQ0qhAT9bt4z9ZWbvk0lCPJUVIF5tyZjerXy67DGY0bzdF4aAYHmf%5CegEG9TuXtCBepIXszc%5CUYn4dpyN%2F0CHEzd0bbdO%2FZEiTJecYIgtppfsfh%3A1601056279363; NMTID=00O5nhqaqyGQl7dt06vhiAsJGJzsIYAAAF0xkiUuQ; WM_NI=2snFWKrp9755s5%2B%2BWXALNnqKD4O0G2DMacFdBYmstxW0%2BrhVDfJflCTjlpaqX8jA%2FX%2Fh7WM%2B%2Fb53UQdpQFugMeo%2FMgTd33VQm6UqGxcoKZuESm2SyRcztQ6b2yBXM4BCMTY%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92f16eb2b196d3b25da8b88fa6c85e869a8eabf143a29bfb85cc5d85919ed5e62af0fea7c3b92af2ada2a7d641b2b0f7d8b83fb1ebe5ccd6548394828ff44188ec81d1c63a8893af84c5708287a394f77495f0e593eb5e88ba8687ef4998b6a491e47b9ce8a6a2ae54edb8f9b8d021edbefbd4d85ba8b09fa7f86d8fbba1d0d06d98bcfdd4d55bafea9b92c674a1b185bbf84f91b3feb4fc5aa1a6bcb2b56af2a78eb9d040829d83b5dc37e2a3; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb4803d347d587a4442f398165709d42a5133a649814e309366; __remember_me=true; __csrf=963a4ffbfce39942a40ca43e7620cee5")
                .execute().bodyBytes();

        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1)+".m4a");
        response.setContentType("application/octet-stream;charset=UTF-8");
        outputStream.write(data);
        outputStream.close();
    }

    @GetMapping("/douyin")
    public void getSec(@RequestParam("url") String url, HttpServletResponse response) throws IOException {
        String videoPath = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";
        Connection con = Jsoup.connect(url);
        con.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 Version/12.0 Safari/604.1");
        Connection.Response resp = con.method(Connection.Method.GET).execute();
        String strUrl = resp.url().toString();
        //获得视频地址
        String videoUrl = videoPath + strUrl.substring(strUrl.indexOf("video/") + 6, strUrl.indexOf("/?"));
        String body = HttpUtil.createGet(videoUrl).execute().body();
        JSONObject json = new JSONObject(body);
        String name = json.getJSONArray("item_list").getJSONObject(0).get("desc",String.class);
        //获得无水印视频地址
        String trueUrl = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").get(0).toString();
        String trueUrlWithOutWm = trueUrl.replace("/playwm/", "/play/");
        System.out.println(trueUrlWithOutWm);
        //得到无水印视频
        CloseableHttpClient build = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet(trueUrlWithOutWm);
        // 模拟手机
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 12_1_4 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/16D57 Version/12.0 Safari/604.1");
        HttpResponse execute = build.execute(httpGet);
        HttpEntity entity = execute.getEntity();
        byte[] data = EntityUtils.toByteArray(entity);
        //下载
        ServletOutputStream outputStream = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) +".mp4");
        response.setContentType("application/octet-stream;charset=UTF-8");
        outputStream.write(data);
        outputStream.close();
    }


}
