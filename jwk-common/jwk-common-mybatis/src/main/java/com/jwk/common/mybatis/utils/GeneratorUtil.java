package com.jwk.common.mybatis.utils;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * 生成orm文件
 */
public class GeneratorUtil {

  public static void main(String[] args) {
    AutoGenerator mpg = new AutoGenerator();
    // 选择 freemarker 引擎，默认 Veloctiy
    mpg.setTemplateEngine(new FreemarkerTemplateEngine());



    // 全局配置
    GlobalConfig gc = new GlobalConfig();
    gc.setAuthor("jiwk");
    gc.setOutputDir("/Users/jiweikuan/learnProjects/demo/src/main/java/");
    // 是否覆盖同名文件，默认是false
    gc.setFileOverride(true);
    // 不需要ActiveRecord特性的请改为false
    gc.setActiveRecord(true);
    // XML 二级缓存
    gc.setEnableCache(false);
    // XML ResultMap
    gc.setBaseResultMap(true);
    // XML columList
    gc.setBaseColumnList(false);
    //去掉Service接口的首字母I
    gc.setServiceName("%sService");
    /* 自定义文件命名，注意 %s 会自动填充表实体属性！ */
    // gc.setMapperName("%sDao");
    // gc.setXmlName("%sDao");
    // gc.setServiceName("MP%sService");
    // gc.setServiceImplName("%sServiceDiy");
    // gc.setControllerName("%sAction");
    mpg.setGlobalConfig(gc);


    // 数据源配置
    DataSourceConfig dsc = new DataSourceConfig();
    dsc.setDbType(DbType.MYSQL);
    dsc.setTypeConvert(new MySqlTypeConvert(){
                         @Override
                         public IColumnType processTypeConvert(GlobalConfig config, String fieldType) {
                           if ( fieldType.toLowerCase().contains( "tinyint" ) ) {
                             System.out.println("转换类型：" + fieldType);
                             return DbColumnType.BYTE;
                           }
                           // 注意！！processTypeConvert 存在默认类型转换，如果不是你要的效果请自定义返回、非如下直接返回。
                           return super.processTypeConvert(config, fieldType);
                         }
                       }
    );
    dsc.setDriverName("com.mysql.jdbc.Driver");
    dsc.setUsername("root");
    dsc.setPassword("Awert159");
    dsc.setUrl("jdbc:mysql://127.0.0.1:3306/jcloud?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowMultiQueries=true&rewriteBatchedStatements=true&useSSL=false&allowPublicKeyRetrieval=true");
    mpg.setDataSource(dsc);


    // 包配置
    PackageConfig pc = new PackageConfig();
    pc.setParent("com.example.demo.web");
//        pc.setModuleName("test");
    pc.setController("controller");
    pc.setEntity("entity");
    pc.setService("service");
    pc.setMapper("mapper");
    mpg.setPackageInfo(pc);

    // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                Map<String, Object> map = new HashMap<String, Object>();
//                map.put("abc", this.getConfig().getGlobalConfig().getAuthor() + "-mp");
//                this.setMap(map);
//            }
//        };
//
//        // 自定义 xxList.jsp 生成
//        List<FileOutConfig> focList = new ArrayList<>();
//        focList.add(new FileOutConfig("/template/list.jsp.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                // 自定义输入文件名称
//                return "D://my_" + tableInfo.getEntityName() + ".jsp";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 调整 xml 生成目录演示
//        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "/develop/code/xml/" + tableInfo.getEntityName() + ".xml";
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        mpg.setCfg(cfg);
//
//        // 关闭默认 xml 生成，调整生成 至 根目录
//        TemplateConfig tc = new TemplateConfig();
//        tc.setXml(null);
//        mpg.setTemplate(tc);

    // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
    // 放置自己项目的 src/main/resources/templates 目录下, 默认名称一下可以不配置，也可以自定义模板名称
    // TemplateConfig tc = new TemplateConfig();
    // tc.setController("...");
    // tc.setEntity("...");
    // tc.setMapper("...");
    // tc.setXml("...");
    // tc.setService("...");
    // tc.setServiceImpl("...");
    // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
    // mpg.setTemplate(tc);


    // 5、策略配置
    StrategyConfig strategy = new StrategyConfig();
    //对那一张表生成代码
    strategy.setInclude(new String[] { "sys_user" });
    //数据库表映射到实体的命名策略
    strategy.setNaming(NamingStrategy.underline_to_camel);
    //生成实体时去掉表前缀
    strategy.setTablePrefix(pc.getModuleName() + "_");

    //数据库表字段映射到实体的命名策略
    strategy.setColumnNaming(NamingStrategy.underline_to_camel);
    // lombok 模型 @Accessors(chain = true) setter链式操作
    strategy.setEntityLombokModel(true);
    //是否为构建者模型
    strategy.setChainModel(true);

    //restful api风格控制器
    strategy.setRestControllerStyle(true);
//    strategy.setControllerMappingHyphenStyle(true); //url中驼峰转连字符


    mpg.setStrategy(strategy);


    // 执行生成

    mpg.execute();
  }

}