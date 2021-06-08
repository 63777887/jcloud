package com.jwk.down.scheduled;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: test
 * @author: jwk
 * @create: {MONTH}-{HOUR}:2020/9/26--1:36 上午
 **/
@Service
public class saveCookie {

    static String params = "puWyTYKib+27kv5kdOd6waVyOvejDWL527FPsDcoWeKT/zyOuW+DLdyaGqhbU0o8nNvHx4c/iyO6WUOerNO2K3nkQVZsihqPsm2Sb3WQ2+6k2EnPp0QVMzPSdkAucLip5+79dWF0PvE59N08397qtA==";
    static String encSecKey = "cc49d0059dfd523d19f1d960ea8b4a19ad0b802d823bdbe3f3588d83099b14456b64039b68ff81cbaba93ebc0b33cc3dc60236f89ea33d3c59628dc2a01d2f23900cd660e969f6447c92e9abaaf30a4f599dfa89597a289c93bee2c3a9cac5344102b27eb60ac1d477895aa62b1153870b0815b4f757ecc53da150caca38453d";
    public final static String[] JSPATH = { "./static/down/js/mi1.js","./static/down/js/mi2.js","./static/down/js/mi3.js", "./static/down/js/crypto-js.js", "./static/down/js/test1.js" };

    @Scheduled(cron = "0 0/10 * * * ?")
    public void saveCookie(){
        Map<String, Object> map = new HashMap<>();
        map.put("params", params);
        map.put("encSecKey", encSecKey);
//        map.put("params", map.get("encText"));
//        map.put("encSecKey", map.get("encSecKey"));
        map.remove("encText");
        String nameBody = HttpRequest
                .post("https://music.163.com/weapi/v3/song/detail?csrf_token=963a4ffbfce39942a40ca43e7620cee5")
                .form(map)
                .header("origin", "https://music.163.com")
                .header("referer", "https://music.163.com")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
                .header("cookie", "mail_psc_fingerprint=37027d82b1028cb6d18dd1cf137db210; _ntes_nnid=4d4430fd1101fddac3307902680b9102,1598073516332; _ntes_nuid=4d4430fd1101fddac3307902680b9102; WM_TID=6IzxhmCnHpRBQBQVFBZvYVYwx4zbZRPK; _iuqxldmzr_=32; ntes_kaola_ad=1; JSESSIONID-WYYY=7BV5wkWTPB5YYdYeANa3%2BrhHe9FblAOiuUNPU7m%2BPViyiSnx90c1s%2BEoFvO6igddlHQ0qhAT9bt4z9ZWbvk0lCPJUVIF5tyZjerXy67DGY0bzdF4aAYHmf%5CegEG9TuXtCBepIXszc%5CUYn4dpyN%2F0CHEzd0bbdO%2FZEiTJecYIgtppfsfh%3A1601056279363; NMTID=00O5nhqaqyGQl7dt06vhiAsJGJzsIYAAAF0xkiUuQ; WM_NI=2snFWKrp9755s5%2B%2BWXALNnqKD4O0G2DMacFdBYmstxW0%2BrhVDfJflCTjlpaqX8jA%2FX%2Fh7WM%2B%2Fb53UQdpQFugMeo%2FMgTd33VQm6UqGxcoKZuESm2SyRcztQ6b2yBXM4BCMTY%3D; WM_NIKE=9ca17ae2e6ffcda170e2e6ee92f16eb2b196d3b25da8b88fa6c85e869a8eabf143a29bfb85cc5d85919ed5e62af0fea7c3b92af2ada2a7d641b2b0f7d8b83fb1ebe5ccd6548394828ff44188ec81d1c63a8893af84c5708287a394f77495f0e593eb5e88ba8687ef4998b6a491e47b9ce8a6a2ae54edb8f9b8d021edbefbd4d85ba8b09fa7f86d8fbba1d0d06d98bcfdd4d55bafea9b92c674a1b185bbf84f91b3feb4fc5aa1a6bcb2b56af2a78eb9d040829d83b5dc37e2a3; MUSIC_U=bd8924b7ab46c8a18a95cf552535ffb4803d347d587a4442f398165709d42a5133a649814e309366; __remember_me=true; __csrf=963a4ffbfce39942a40ca43e7620cee5")
                .execute().body();
        JSONObject jsonObject = new JSONObject(nameBody);
        String name = jsonObject.getJSONArray("songs").getJSONObject(0).get("name", String.class);
//        System.out.println(name);

    }

}
