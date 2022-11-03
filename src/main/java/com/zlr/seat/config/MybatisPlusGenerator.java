//package com.zlr.seat.config;
//
//import com.baomidou.mybatisplus.core.toolkit.StringPool;
//import com.baomidou.mybatisplus.generator.AutoGenerator;
//import com.baomidou.mybatisplus.generator.InjectionConfig;
//import com.baomidou.mybatisplus.generator.config.*;
//import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
//import com.baomidou.mybatisplus.generator.config.po.TableInfo;
//import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
//import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
//import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * @author Zenglr
// * @program: seat
// * @packagename: com.zlr.seat.config
// * @Description
// * @create 2022-09-18-下午2:53
// */
//public class MybatisPlusGenerator {
//    public static void main(String[] args) {
//        AutoGenerator autoGenerator = new AutoGenerator();
//        // 全局配置
//        GlobalConfig gc = new GlobalConfig();
//        gc.setOutputDir("/Users/sskura/Documents/seat/src/main/java");
//        gc.setAuthor("zenglr");
//        gc.setOpen(false);
//        gc.setSwagger2(true);
//        gc.isSwagger2();
//        autoGenerator.setGlobalConfig(gc);
//        // 数据源配置
//        DataSourceConfig dataSource = new DataSourceConfig();
//        dataSource.setUrl("jdbc:mysql://localhost:3306/seat_sys?useSSL=false");
//        dataSource.setDriverName("com.mysql.jdbc.Driver");
//        dataSource.setUsername("root");
//        dataSource.setPassword("201379");
//        dataSource.setTypeConvert(new MySqlTypeConvert() {
//            @Override
//            public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
//                String tinyint = "tinyint";
//                if (fieldType.toLowerCase().contains(tinyint)) {
//                    return DbColumnType.INTEGER;
//                }
//                return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
//            }
//        });
//        autoGenerator.setDataSource(dataSource);
//        // 包名配置
//        PackageConfig pc = new PackageConfig();
//        pc.setParent("com.zlr.seat");
//        autoGenerator.setPackageInfo(pc);
//        InjectionConfig cfg = new InjectionConfig() {
//            @Override
//            public void initMap() {
//                // to do nothing
//            }
//        };
//        String templatePath = "/templates/mapper.xml.ftl";
//        List<FileOutConfig> focList = new ArrayList<>();
//        // 自定义配置会被优先输出
//        focList.add(new FileOutConfig(templatePath) {
//            @Override
//            public String outputFile(TableInfo tableInfo) {
//                return "/Users/sskura/Documents/seat" + "/src/main/resources/mapper/"
//                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
//            }
//        });
//        cfg.setFileOutConfigList(focList);
//        autoGenerator.setCfg(cfg);
//        // 配置模板
//        TemplateConfig templateConfig = new TemplateConfig();
//        templateConfig.setXml(null);
//        autoGenerator.setTemplate(templateConfig);
//        // 策略配置
//        StrategyConfig strategy = new StrategyConfig();
//        strategy.setNaming(NamingStrategy.underline_to_camel);
//        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setEntityLombokModel(true);
//        strategy.setRestControllerStyle(true);
////        strategy.setInclude("article_collect");
//        // 设置
//        // strategy.setInclude("")
//        // 公共父类
////        strategy.setSuperControllerClass("com.zlr.seat.controller.BaseController");
//        autoGenerator.setStrategy(strategy);
//        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
//        autoGenerator.execute();
//    }
//}
