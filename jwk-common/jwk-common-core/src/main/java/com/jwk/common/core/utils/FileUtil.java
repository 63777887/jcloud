package com.jwk.common.core.utils;

import com.jwk.common.core.exception.ServiceException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 *
 */
public class FileUtil {

  //记录各个文件头信息及对应的文件类型

  private static Map<String, String> mFileTypes = new ConcurrentHashMap<>();

  static {
    // images
    mFileTypes.put("FFD8FFE0", "jpg");
    mFileTypes.put("FFD8FFE1", "jpg");
    mFileTypes.put("89504E47", "png");
    mFileTypes.put("47494638", "gif");
    mFileTypes.put("49492A00", "tif");
    mFileTypes.put("424D", "bmp");
    mFileTypes.put("424DD62E", "bmp");

    //PS和CAD
    mFileTypes.put("38425053", "psd");
    // CAD
    mFileTypes.put("41433130", "dwg");
    mFileTypes.put("252150532D41646F6265", "ps");

    //办公文档类

    //ppt、doc、xls
    mFileTypes.put("D0CF11E0", "doc");
    //pptx、docx、xlsx
    mFileTypes.put("504B0304", "docx");
    mFileTypes.put("255044", "pdf");
    mFileTypes.put("25504446", "pdf");
    mFileTypes.put("255044462D312E", "pdf");

    //注意由于文本文档录入内容过多，则读取文件头时较为多变-START
    //txt
    mFileTypes.put("0D0A0D0A", "txt");
    //txt
    mFileTypes.put("0D0A2D2D", "txt");
    //txt
    mFileTypes.put("0D0AB4B4", "txt");
    //文件头部为汉字
    mFileTypes.put("B4B4BDA8", "txt");
    //txt,文件头部为英文字母
    mFileTypes.put("73646673", "txt");
    //txt,文件头部内容为数字
    mFileTypes.put("32323232", "txt");
    //txt,文件头部内容为数字
    mFileTypes.put("0D0A09B4", "txt");
    //txt,文件头部内容为数字
    mFileTypes.put("3132330D", "txt");
    mFileTypes.put("C6BDCCA8", "txt");
    mFileTypes.put("B0B2C8AB", "txt");
    mFileTypes.put("EFBBBF23", "txt");

    /**注意由于文本文档录入内容过多，则读取文件头时较为多变-END**/

    // 日记本
    mFileTypes.put("7B5C727466", "rtf");

    //视频或音频类
    mFileTypes.put("3026B275", "wma");
    mFileTypes.put("57415645", "wav");
    mFileTypes.put("41564920", "avi");
    mFileTypes.put("4D546864", "mid");
    mFileTypes.put("2E524D46", "rm");
    mFileTypes.put("000001BA", "mpg");
    mFileTypes.put("000001B3", "mpg");
    mFileTypes.put("6D6F6F76", "mov");
    mFileTypes.put("3026B2758E66CF11", "asf");

    //压缩包
    mFileTypes.put("52617221", "rar");
    mFileTypes.put("1F8B08", "gz");

    //程序文件
    mFileTypes.put("3C3F786D6C", "xml");
    mFileTypes.put("68746D6C3E", "html");
    mFileTypes.put("7061636B", "java");
    //3C252D2D 3C254020
    mFileTypes.put("3C252D2D", "jsp");
    mFileTypes.put("4D5A9000", "exe");

    // 邮件
    mFileTypes.put("44656C69766572792D646174653A", "eml");
    //Access数据库文件
    mFileTypes.put("5374616E64617264204A", "mdb");

    mFileTypes.put("46726F6D", "mht");
    mFileTypes.put("4D494D45", "mhtml");

    mFileTypes.put("B0B4CAB1", "apk");
  }

  /**
   * 根据文件的输入流获取文件头信息
   *
   * @param bytes 文件内容前4位
   * @return 文件头信息
   */
  public static String getFileType(byte[] bytes) {
    return mFileTypes.get(getFileHeader(bytes));
  }


  /**
   * 根据文件转换成的字节数组获取文件头信息
   *
   * @param bytes 文件内容前4位
   * @return 文件头信息
   */
  public static String getFileHeader(byte[] bytes) {
    String value = bytesToHexString(bytes);
    return value;
  }


  /**
   * 将要读取文件头信息的文件的byte数组转换成string类型表示
   * <p/>
   * 下面这段代码就是用来对文件类型作验证的方法，
   * <p/>
   * 将字节数组的前四位转换成16进制字符串，并且转换的时候，要先和0xFF做一次与运算。
   * <p/>
   * 这是因为，整个文件流的字节数组中，有很多是负数，进行了与运算后，可以将前面的符号位都去掉，
   * <p/>
   * 这样转换成的16进制字符串最多保留两位，如果是正数又小于10，那么转换后只有一位，
   * <p/>
   * 需要在前面补0，这样做的目的是方便比较，取完前四位这个循环就可以终止了
   *
   * @param src 要读取文件头信息的文件的byte数组
   * @return 文件头信息
   */
  private static String bytesToHexString(byte[] src) {
    StringBuilder builder = new StringBuilder();
    if (src == null || src.length <= 0) {
      return null;
    }
    String hv;
    for (int i = 0; i < src.length; i++) {
      // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
      hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
      if (hv.length() < 2) {
        builder.append(0);
      }
      builder.append(hv);
    }
    return builder.toString();
  }

  public static File transToFile(MultipartFile multipartFile) {
    // 获取文件名
    String fileName = multipartFile.getOriginalFilename();
    // 获取文件后缀
    String prefix = fileName.substring(fileName.lastIndexOf("."));
    // 用uuid作为文件名，防止生成的临时文件重复
    final File file;
    try {
      file = File.createTempFile(UUID.randomUUID().toString(), prefix);
      multipartFile.transferTo(file);
    } catch (IOException e) {
      throw new ServiceException("文件转换失败");
    }

    //你的业务逻辑

    //程序结束时，删除临时文件
    deleteFile(file);
    return file;
  }

  /**
   * 删除
   *
   * @param files
   */
  private static void deleteFile(File... files) {
    for (File file : files) {
      if (file.exists()) {
        file.delete();
      }
    }
  }
}
